package mavonie.subterminal.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * http://www.michenux.net/android-database-sqlite-creation-upgrade-245.html
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final Logger log = Logger.getLogger(DatabaseHandler.class.toString());

    private static final String SQL_DIR = "sql";

    private static final String CREATEFILE = "create.sql";

    private static final String UPGRADEFILE_PREFIX = "upgrade-";

    private static final String UPGRADEFILE_SUFFIX = ".sql";

    private Context context;

    public DatabaseHandler(Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            log.info("create database");
            execSqlFile(CREATEFILE, db);
        } catch (IOException exception) {
            throw new RuntimeException("Database creation failed", exception);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            log.info("upgrade database from {} to {}" + oldVersion + " - " + newVersion);
            for (String sqlFile : AssetUtils.list(SQL_DIR, this.context.getAssets())) {
                if (sqlFile.startsWith(UPGRADEFILE_PREFIX)) {
                    int fileVersion = Integer.parseInt(sqlFile.substring(UPGRADEFILE_PREFIX.length(), sqlFile.length() - UPGRADEFILE_SUFFIX.length()));
                    if (fileVersion > oldVersion && fileVersion <= newVersion) {
                        execSqlFile(sqlFile, db);
                    }
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException("Database upgrade failed", exception);
        }
    }

    protected void execSqlFile(String sqlFile, SQLiteDatabase db) throws SQLException, IOException {
        log.info("  exec sql file: {}" + sqlFile);
        for (String sqlInstruction : SqlParser.parseSqlFile(SQL_DIR + "/" + sqlFile, this.context.getAssets())) {
            log.severe("    sql: {}" + sqlInstruction);
            db.execSQL(sqlInstruction);
        }
    }
}