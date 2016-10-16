package mavonie.subterminal.models;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
        return this.getId() > 0;
    }

    protected static DatabaseHandler _db;

    protected ArrayList<String> itemsForSelect;

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

    public Model getOneById(int id) {
        SQLiteDatabase db = _db.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + getTableName() + "where _ID ='" + id + "'", null);
        if (cursor.moveToFirst()) {
            return populateFromCursor(cursor);
        }

        cursor.close();

        return null;
    }

    /**
     * Get items associated with this model
     *
     * @param filter
     * @return List
     */
    public List getItems(Array filter) {
        Cursor cursor = _db.getReadableDatabase().rawQuery("select * from " + getTableName(), null);

        List<Model> list = new ArrayList<Model>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                Model item = populateFromCursor(cursor);
                list.add(item);
                cursor.moveToNext();
            }
        }

        cursor.close();

        return list;
    }

    abstract Model populateFromCursor(Cursor cursor);

    abstract void populateContentValues(ContentValues contentValues);

    abstract String getTableName();

    /**
     * Write a model to the DB
     *
     * @return Boolean
     */
    public boolean save() {
        ContentValues contentValues = new ContentValues();

        try {
            this.populateContentValues(contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long res = 0;

        //If item exists we want to update associated row
        if (this.exists()) {
            res = _db.getWritableDatabase().update(getTableName(), contentValues, _ID + " = " + this.getId(), null);
        } else {
            res = _db.getWritableDatabase().insert(getTableName(), null, contentValues);
        }

        return res == 1;
    }

    /**
     * Delete a row
     *
     * @return Boolean
     */
    public boolean delete() {
        long res = _db.getWritableDatabase().delete(getTableName(), _ID + " = " + this.getId(), null);

        return res == 1;
    }

    /**
     * Count how many rows on this model
     *
     * @return int
     */
    public int count() {
        Cursor mCount = _db.getReadableDatabase().rawQuery("SELECT count(" + _ID + ") FROM " + getTableName() + ";", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();

        return count;
    }

    public ArrayList<String> getItemsForSelectArray(String fieldName) {

        if (this.itemsForSelect == null) {
            this.itemsForSelect = new ArrayList<String>();
        }

        Cursor cursor = _db.getReadableDatabase().rawQuery("select " + fieldName + " from " + getTableName(), null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String string = cursor.getString(cursor.getPosition());
                itemsForSelect.add(string);
                cursor.moveToNext();
            }
        }

        cursor.close();


        return this.itemsForSelect;
    }
}
