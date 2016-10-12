package mavonie.subterminal.models;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.io.Serializable;

import mavonie.subterminal.DB.DatabaseHandler;
import mavonie.subterminal.DB.VersionUtils;
import mavonie.subterminal.MainActivity;

/**
 * Created by mavon on 12/10/16.
 */
abstract public class Model implements BaseColumns, Serializable {

    protected static final String TEXT_TYPE = " TEXT";
    protected static final String COMMA_SEP = ",";

    protected int _id;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public boolean exists() {
        return true;
    }

    protected static DatabaseHandler _db;

    //Declare db once
    static {
        try {
            _db = new DatabaseHandler(
                    MainActivity.getActivity().getApplicationContext(),
                    "database",
                    null,
                    VersionUtils.getVersionCode(MainActivity.getActivity().getApplicationContext())
            );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Model getOneById(int id) throws Exception {
        SQLiteDatabase db = _db.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + getTableName() + "where _ID ='" + id + "'", null);
        if (cursor.moveToFirst()) {
            return populateFromCursor(cursor);
        }

        return null;
    }

    abstract Model populateFromCursor(Cursor cursor);

    abstract void populateContentValues(ContentValues contentValues);

    abstract String getTableName();

    /**
     * Write a model to the DB
     *
     * @return
     */
    public boolean save() {
        ContentValues contentValues = new ContentValues();

        try {
            this.populateContentValues(contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long res = _db.getWritableDatabase().insert(getTableName(), null, contentValues);

        return true;
    }
}
