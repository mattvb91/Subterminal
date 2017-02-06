package mavonie.subterminal.Models;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Pair;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.DB.DatabaseHandler;
import mavonie.subterminal.DB.VersionUtils;
import mavonie.subterminal.MainActivity;

/**
 * Base Model
 * Handle all core functionality like retrieving/saving a model
 */
abstract public class Model implements BaseColumns, Serializable {

    public static String EMPTY_LIST_ITEM = " - ";

    /**
     * dbColumns data types
     */
    protected static final int TYPE_TEXT = 0;
    protected static final int TYPE_INTEGER = 1;
    protected static final int TYPE_DOUBLE = 2;
    protected static final int TYPE_OTHER = 3; //Custom, pass the cursor directly to the setter

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
     * @return boolean
     */
    public boolean exists() {

        String query = "SELECT " + _ID + " FROM " + getTableName() + " WHERE " + _ID + " = " + this.getId();
        Cursor cursor = _db.getReadableDatabase().rawQuery(query, null);

        try {
            return cursor.moveToFirst();

        } finally {
            cursor.close();
        }
    }

    protected static DatabaseHandler _db;

    protected LinkedHashMap<String, String> itemsForSelect;

    /**
     * Declare the db instance once
     */
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

    /**
     * Get a model by its ID
     *
     * @param id
     * @return Model|Null
     */
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
     * Get models matching passed in filter.
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

    /**
     * Build up the WHERE string with its various filters
     *
     * @param filter
     * @return String
     */
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

    /**
     * This will use the defined dbColumns to load the model.
     *
     * @param cursor
     * @return Model
     */
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
                    case Model.TYPE_OTHER:
                        model.populateCustomFromCursor(field, cursor);
                        break;
                }
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return model;
    }

    /**
     * Allow custom populating from cursor in child classes
     *
     * @param field
     * @param cursor
     */
    protected void populateCustomFromCursor(Field field, Cursor cursor) throws NoSuchMethodException {
        throw new NoSuchMethodException();
    }

    /**
     * Use the dbColumns to insert the contentValues
     *
     * @param contentValues
     */
    protected void populateContentValues(ContentValues contentValues) {

        for (Map.Entry<String, Integer> entry : getDbColumns().entrySet()) {

            //Skip if its the id, doesnt need to be set here
            if (entry.getKey().equals(_ID) || entry.getKey().equals(TYPE_OTHER)) {
                continue;
            }

            Method method = null;

            try {
                method = getClass().getMethod(fieldToGetter(entry.getKey()));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                Object obj = method.invoke(this);

                if (obj != null) {
                    switch (entry.getValue()) {
                        case TYPE_DOUBLE:
                            contentValues.put(entry.getKey(), (double) obj);
                            break;
                        case TYPE_INTEGER:
                            contentValues.put(entry.getKey(), (Integer) obj);
                            break;
                        case TYPE_TEXT:
                            contentValues.put(entry.getKey(), (String) obj);
                            break;
                    }
                } else {
                    contentValues.putNull(entry.getKey());
                }
            } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract String getTableName();

    /**
     * Save model to DB
     * If exists update, else insert
     *
     * @return Boolean
     */
    public boolean save() {
        ContentValues contentValues = new ContentValues();

        populateContentValues(contentValues);

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
     * Delete the current model
     *
     * @return boolean
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
     * Count how many rows on this table
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
     * Sum of given field
     *
     * @return int
     */
    public int sum(String fieldName) {
        String query = "SELECT sum(" + fieldName + ") FROM " + getTableName();

        if (this instanceof Synchronizable) {
            query += this.buildWhere(Synchronizable.getActiveParams());
        }

        Cursor cursor = _db.getReadableDatabase().rawQuery(query, null);
        cursor.moveToFirst();
        int sum = cursor.getInt(0);
        cursor.close();

        return sum;
    }

    /**
     * If this is an optional select box we include an empty option at the top
     *
     * @param fieldName
     * @param optional
     * @return
     */
    public LinkedHashMap<String, String> getItemsForSelect(String fieldName, boolean optional) {
        if (this.itemsForSelect == null) {
            this.itemsForSelect = new LinkedHashMap<String, String>();

            if (optional) {
                this.itemsForSelect.put(null, " - ");
            }
        }

        return getItemsForSelect(fieldName);
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

        query += " ORDER BY " + fieldName + " ASC";

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

        cursor.close();

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
     * @return boolean
     */
    public boolean isSynchronizable() {
        return this instanceof Synchronizable;
    }

    /**
     * Add the db columns common to all models
     *
     * @param dbColumns
     */
    public static void setDBColumns(Map<String, Integer> dbColumns) {
        dbColumns.put(_ID, TYPE_INTEGER);
    }

    /**
     * Quick way of generating the getter for associated instance variable name
     *
     * @param name
     * @return
     */
    private static String fieldToGetter(String name) {
        String[] parts = name.split("_");

        String result = "";

        for (String part : parts) {
            result += part.substring(0, 1).toUpperCase() + part.substring(1);
        }

        return "get" + result;
    }

    /**
     * Get the next available autoIncrement value
     */
    public int getNextAutoIncrement() {
        String query = "SELECT MAX(" + _ID + ") FROM " + this.getTableName() + ";";
        Cursor cursor = _db.getReadableDatabase().rawQuery(query, null);

        int nextIncrement = 0;

        if (cursor.moveToFirst()) {
            nextIncrement = cursor.getInt(0) + 1;
        }
        cursor.close();

        return nextIncrement;

    }
}
