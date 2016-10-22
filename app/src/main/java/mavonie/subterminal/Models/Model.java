package mavonie.subterminal.Models;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

    protected LinkedHashMap<String, String> itemsForSelect;

    //Declare db once
    public Model() {

        if (_db == null) {
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
    }

    public static void setDB(DatabaseHandler db) {
        _db = db;
    }

    public Model getOneById(int id) {
        SQLiteDatabase db = _db.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + getTableName() + " where " + _ID + " = " + id, null);
        if (cursor.moveToFirst()) {
            return populateFromCursor(cursor);
        }

        cursor.close();

        return null;
    }

    public static final String FILTER_ORDER_FIELD = "order_field";
    public static final String FILTER_ORDER_DIR = "order_dir";
    public static final String FILTER_ORDER_DIR_DESC = "DESC";
    public static final String FILTER_ORDER_DIR_ASC = "ASC";
    public static final String FILTER_WHERE = "WHERE";
    public static final String FILTER_WHERE_FIELD = "where_field";
    public static final String FILTER_WHERE_VALUE = "where_value";
    public static final String FILTER_LIMIT = "LIMIT";

    /**
     * Get items associated with this model
     *
     * @param filter
     * @return List
     * <p>
     * TODO refactor/improve WHERE filtering to get rid of heavy hashmap usage
     */
    public List getItems(HashMap<String, Object> filter) {

        String query = "select * from " + getTableName();

        if (filter != null) {

            String orderDir = (String) filter.get(FILTER_ORDER_DIR);
            String orderField = (String) filter.get(FILTER_ORDER_FIELD);

            boolean multipleWhere = filter.containsKey(FILTER_WHERE);

            if (!multipleWhere) {
                String whereField = (String) filter.get(FILTER_WHERE_FIELD);
                String whereValue = (String) filter.get(FILTER_WHERE_VALUE);

                if (whereField != null) {
                    query += " WHERE " + whereField + " = " + whereValue;
                }
            } else {
                HashMap wheres = (HashMap<Integer, HashMap>) filter.get(FILTER_WHERE);

                for (int i = 0; i < wheres.size(); i++) {
                    HashMap where = (HashMap) wheres.get(i);

                    String whereField = (String) where.get(FILTER_WHERE_FIELD);
                    Object whereValue = where.get(FILTER_WHERE_VALUE);

                    if (i == 0) {
                        query += " WHERE " + whereField + " = " + whereValue.toString();
                    } else {
                        query += " AND " + whereField + " = " + whereValue.toString();
                    }
                }
            }

            if (orderField != null) {
                query += " ORDER BY LOWER(" + orderField + ") " + orderDir + "," + _ID + " DESC";
            } else if (orderDir != null) {
                query += " ORDER BY " + _ID + " " + orderDir;
            }

            if (filter.containsKey(FILTER_LIMIT)) {
                query += " " + FILTER_LIMIT + " " + filter.get(FILTER_LIMIT);
            }

        }

        Cursor cursor = _db.getReadableDatabase().rawQuery(query, null);

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
            this.setId(Integer.parseInt(Long.toString(res)));
        }

        return res > 0;
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

    public LinkedHashMap<String, String> getItemsForSelect(String fieldName) {

        if (this.itemsForSelect == null) {
            this.itemsForSelect = new LinkedHashMap<String, String>();
        }

        Cursor cursor = _db.getReadableDatabase().rawQuery("select " + _ID + ", " + fieldName + " from " + getTableName(), null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                int id = cursor.getInt(0);
                String string = cursor.getString(1);
                itemsForSelect.put(Integer.toString(id), string);
                cursor.moveToNext();
            }
        }

        cursor.close();

        return this.itemsForSelect;
    }

    public Model getItem(Pair<String, String> filter) {
        String field = filter.first;
        String[] keyword = {filter.second};

        Cursor cursor = _db.getReadableDatabase().rawQuery("select * from " + getTableName() + " WHERE " + field + "=?", keyword);

        Model item = null;

        if (cursor.moveToFirst()) {
            item = populateFromCursor(cursor);
        }

        return item;
    }

    /**
     * We may be adding remote global entities into the database.
     * Check that the user can edit these locally.
     *
     * @return boolean
     */
    public boolean canEdit() {
        return true;
    }
}
