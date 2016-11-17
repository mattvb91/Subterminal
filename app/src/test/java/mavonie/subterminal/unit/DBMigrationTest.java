package mavonie.subterminal.unit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Set;
import java.util.TreeSet;

import mavonie.subterminal.DB.DatabaseHandler;
import mavonie.subterminal.DB.VersionUtils;
import mavonie.subterminal.unit.Base.BaseDBUnit;


/**
 * Test database migrations
 * <p>
 * BEFORE RUNNING THIS UNIT TEST MAKE SURE TO REBUILD PROJECT
 * OTHERWISE CACHED SQL FILES COULD BE USED
 */
public class DBMigrationTest extends BaseDBUnit {

    private File newFile;
    private File upgradedFile;
    private File baseDir;

    @Before
    public void setup() throws IOException {
        try {
            super.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseDir = new File("build/tmp/migration");
        baseDir.mkdir();
        newFile = new File(baseDir, "new.db");
        upgradedFile = new File(baseDir, "upgraded.db");
    }


    @Test
    public void upgrade_should_be_the_same_as_create() throws Exception {
        try {
            Context context = RuntimeEnvironment.application;

            DatabaseHandler newDb = this._db;
            DatabaseHandler upgradedDb = new DatabaseHandler(
                    context,
                    "test_database_upgraded",
                    null,
                    VersionUtils.getVersionCode(context)
            );

            SQLiteDatabase newSqlite = SQLiteDatabase.openOrCreateDatabase(newFile, null);
            SQLiteDatabase upgradedSqlite = SQLiteDatabase.openOrCreateDatabase(upgradedFile.getAbsolutePath(), null);

            newDb.onCreate(newSqlite);
            upgradedDb.onUpgrade(upgradedSqlite, -1, VersionUtils.getVersionCode(context));

            Set<String> newSchema = extractSchema(newFile.getAbsolutePath());
            Set<String> upgradedSchema = extractSchema(upgradedFile.getAbsolutePath());

            Assert.assertEquals(newSchema, upgradedSchema);
        } finally {
            newFile.delete();
            upgradedFile.delete();
            baseDir.delete();
        }
    }

    private Set<String> extractSchema(String url) throws Exception {
        Connection conn = null;

        final Set<String> schema = new TreeSet<>();
        ResultSet tables = null;
        ResultSet columns = null;

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + url);

            tables = conn.getMetaData().getTables(null, null, null, null);
            while (tables.next()) {

                String tableName = tables.getString("TABLE_NAME");
                String tableType = tables.getString("TABLE_TYPE");
                schema.add(tableType + " " + tableName);

                columns = conn.getMetaData().getColumns(null, null, tableName, null);
                while (columns.next()) {

                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");
                    String columnNullable = columns.getString("IS_NULLABLE");
                    String columnDefault = columns.getString("COLUMN_DEF");
                    schema.add("TABLE " + tableName +
                            " COLUMN " + columnName + " " + columnType +
                            " NULLABLE=" + columnNullable +
                            " DEFAULT=" + columnDefault);
                }
            }

            return schema;
        } finally {
            conn.close();
        }
    }
}
