package mavonie.subterminal.Models;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Pair;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.DB.DatabaseHandler;
import mavonie.subterminal.DB.VersionUtils;
import mavonie.subterminal.MainActivity;

/**
 * Created by mavon on 12/10/16.
 */
abstract public class Model implements BaseColumns, Serializable {

    protected static final int TYPE_TEXT = 0;
    protected static final int TYPE_INTEGER = 1;
    protected static final int TYPE_DOUBLE = 2;

    public abstract Map<String, Integer> getDbColumns();

    protected int _id;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    /**
     * Check if a record exists in the database
     *
     * @return
     */
    public boolean exists() {

        String query = "SELECT " + _ID + " FROM " + getTableName() + " WHERE " + _ID + " = " + this.getId();
        Cursor cursor = _db.getReadableDatabase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
        }

        return false;
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

            query += this.buildWhere(filter);


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
                list.add(populateFromCursor(cursor));
                cursor.moveToNext();
            }
        }

        cursor.close();

        return list;
    }

    public String buildWhere(HashMap<String, Object> filter) {

        String query = "";

        boolean multipleWhere = filter.containsKey(FILTER_WHERE);

        if (!multipleWhere) {
            String whereField = (String) filter.get(FILTER_WHERE_FIELD);
            String whereValue = (String) filter.get(FILTER_WHERE_VALUE);

            if (whereValue == null) {
                query += " WHERE " + whereField + " IS NULL";
            } else {
                if (whereField != null) {
                    query += " WHERE " + whereField + " = " + whereValue;
                }
            }
        } else {
            HashMap wheres = (HashMap<Integer, HashMap>) filter.get(FILTER_WHERE);

            for (int i = 0; i < wheres.size(); i++) {
                HashMap where = (HashMap) wheres.get(i);

                String whereField = (String) where.get(FILTER_WHERE_FIELD);
                Object whereValue = where.get(FILTER_WHERE_VALUE);

                if (i == 0) {
                    query += " WHERE ";
                } else {
                    query += " AND ";
                }

                if (whereValue == null) {
                    query += whereField + " IS NULL";
                } else {
                    query += whereField + " = " + whereValue.toString();
                }
            }
        }
        return query;
    }

    private Model populateFromCursor(Cursor cursor) {
        Model model = null;
        try {
            model = getClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Integer> entry : getDbColumns().entrySet()) {

            Field field = null;
            int index = cursor.getColumnIndex(entry.getKey());

            Class<?> current = getClass();

            while (field == null) {
                try {
                    field = current.getDeclaredField(entry.getKey());
                } catch (NoSuchFieldException e) {
                    current = current.getSuperclass();
                }
            }

            field.setAccessible(true);

            try {
                switch (entry.getValue()) {
                    case Model.TYPE_TEXT:
                        String string = cursor.getString(index);

                        if (!cursor.isNull(index) && string.length() > 0) {
                            field.set(model, cursor.getString(index));
                        }
                        break;
                    case Model.TYPE_INTEGER:
                        Integer integer = cursor.getInt(index);

                        if (!cursor.isNull(index) && integer != null) {
                            field.set(model, integer);
                        }
                        break;
                    case Model.TYPE_DOUBLE:
                        Double value = cursor.getDouble(index);

                        if (!cursor.isNull(index) && value != null) {
                            field.set(model, value);
                        }
                        break;
                }
            } catch (IllegalAccessException e) {

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        return model;
    }

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

        //Delete any associated images with this entity
        boolean result = Image.deleteForEntity(this);

        if (result) {
            long dbDelete = _db.getWritableDatabase().delete(getTableName(), _ID + " = " + this.getId(), null);
            return dbDelete == 1;
        }

        return result;
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

    /**
     * Count with filter
     *
     * @param filter
     * @return
     */
    public int count(HashMap<String, Object> filter) {
        String query = "SELECT count(" + _ID + ") FROM " + getTableName();
        query += this.buildWhere(filter);

        Cursor mCount = _db.getReadableDatabase().rawQuery(query, null);

        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();

        return count;
    }

    /**
     * Get items for a select spinner.
     *
     * @param fieldName to select
     * @return LinkedHashMap<String, String>
     */
    public LinkedHashMap<String, String> getItemsForSelect(String fieldName) {

        if (this.itemsForSelect == null) {
            this.itemsForSelect = new LinkedHashMap<String, String>();
        }

        String query = "SELECT " + _ID + ", " + fieldName + " FROM " + getTableName();

        if (this.isSynchronizable()) {
            //Make sure we only get active instances
            query += " WHERE " + Synchronizable.COLUMN_DELETED + " = " + Synchronizable.DELETED_FALSE;
        }

        Cursor cursor = _db.getReadableDatabase().rawQuery(query, null);

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

    /**
     * Check if current model is synchronizable
     *
     * @return
     */
    public boolean isSynchronizable() {
        return this instanceof Synchronizable;
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    /**
     * Add the db columns common to all models
     *
     * @param dbColumns
     */
    public static void setDBColumns(Map<String, Integer> dbColumns) {
        dbColumns.put(_ID, TYPE_INTEGER);
    }
}
