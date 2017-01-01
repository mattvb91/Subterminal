package mavonie.subterminal.Models.Skydive;


import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Models.Model;

/**
 * Dropzone Model
 */
public class Dropzone extends Model {

    private int id; //THIS IS JUST TO SYNC TO REMOTE DB ID

    private String global_id,
            name,
            description,
            website,
            phone,
            email,
            formatted_address,
            local,
            country;

    private double latitude,
            longtitude;

    private List<DropzoneAircraft> dropzone_aircraft;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "skydive_dropzone";

    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_GLOBAL_ID = "global_id";
    public static final String COLUMN_NAME_LATITUDE = "latitude";
    public static final String COLUMN_NAME_LONGTITUDE = "longtitude";
    public static final String COLUMN_NAME_WEBSITE = "website";
    public static final String COLUMN_NAME_PHONE = "phone";
    public static final String COLUMN_NAME_EMAIL = "email";
    public static final String COLUMN_NAME_ADDRESS_FORMATTED_ADDRESS = "formatted_address";
    public static final String COLUMN_NAME_ADDRESS_LEVEL_1 = "local";
    public static final String COLUMN_NAME_ADDRESS_LINE_COUNTRY = "country";

    /* END DB DEFINITIONS */

    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<String, Integer>();

            dbColumns.put(COLUMN_NAME_NAME, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_DESCRIPTION, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_GLOBAL_ID, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_LATITUDE, TYPE_DOUBLE);
            dbColumns.put(COLUMN_NAME_LONGTITUDE, TYPE_DOUBLE);
            dbColumns.put(COLUMN_NAME_WEBSITE, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_PHONE, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_EMAIL, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_ADDRESS_FORMATTED_ADDRESS, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_ADDRESS_LEVEL_1, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_ADDRESS_LINE_COUNTRY, TYPE_TEXT);

            Model.setDBColumns(dbColumns);
        }

        return dbColumns;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public String getGlobalId() {
        return global_id;
    }

    public void setGlobalId(String global_id) {
        this.global_id = global_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getFormattedAddress() {
        return formatted_address;
    }

    public void setFormattedAddress(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dropzone dropzone = (Dropzone) o;

        if (Double.compare(dropzone.latitude, latitude) != 0) return false;
        if (Double.compare(dropzone.longtitude, longtitude) != 0) return false;
        if (global_id != null ? !global_id.equals(dropzone.global_id) : dropzone.global_id != null)
            return false;
        if (name != null ? !name.equals(dropzone.name) : dropzone.name != null) return false;
        if (description != null ? !description.equals(dropzone.description) : dropzone.description != null)
            return false;
        if (website != null ? !website.equals(dropzone.website) : dropzone.website != null)
            return false;
        if (phone != null ? !phone.equals(dropzone.phone) : dropzone.phone != null) return false;
        if (email != null ? !email.equals(dropzone.email) : dropzone.email != null) return false;
        if (formatted_address != null ? !formatted_address.equals(dropzone.formatted_address) : dropzone.formatted_address != null)
            return false;
        if (local != null ? !local.equals(dropzone.local) : dropzone.local != null)
            return false;
        return country != null ? country.equals(dropzone.country) : dropzone.country == null;
    }

    /**
     * Check to see if the map should be active for this Dropzone
     *
     * @return boolean
     */
    public boolean isMapActive() {
        if (this.getLatitude() != 0.00 && this.getLongtitude() != 0.00) {
            return true;
        }

        return false;
    }

    public static void createOrUpdate(Dropzone dropzone) {
        dropzone.setId(dropzone.id);
        dropzone.save();

        dropzone.updateAircraft(dropzone.dropzone_aircraft);
    }

    /**
     * Check we dont already have this added to system
     *
     * @param dropzone_aircraft
     */
    private void updateAircraft(List<DropzoneAircraft> dropzone_aircraft) {

        for (DropzoneAircraft dzAircraft : dropzone_aircraft) {
            HashMap<String, Object> params = new HashMap<>();

            HashMap<String, Object> whereDropzoneId = new HashMap<>();
            whereDropzoneId.put(Model.FILTER_WHERE_FIELD, DropzoneAircraft.COLUMN_NAME_DROPZONE_ID);
            whereDropzoneId.put(Model.FILTER_WHERE_VALUE, this.getId());

            HashMap<String, Object> whereAircraftId = new HashMap<>();
            whereAircraftId.put(Model.FILTER_WHERE_FIELD, DropzoneAircraft.COLUMN_NAME_AIRCRAFT_ID);
            whereAircraftId.put(Model.FILTER_WHERE_VALUE, dzAircraft.getAircraftId());

            HashMap<Integer, HashMap> wheres = new HashMap<>();
            wheres.put(wheres.size(), whereDropzoneId);
            wheres.put(wheres.size(), whereAircraftId);
            params.put(Model.FILTER_WHERE, wheres);

            int count = new DropzoneAircraft().count(params);

            if (count == 0) {
                dzAircraft.setDropzoneId(this.getId());
                dzAircraft.save();
            }
        }
    }

    /**
     * Get all aircrafts associated with this dropzone
     *
     * @return
     */
    public List<Aircraft> getAircraft() {
        List<Aircraft> aircraft = new ArrayList<Aircraft>();

        HashMap<String, Object> params = aircraftParemeters();
        List<DropzoneAircraft> dzAircrafts = new DropzoneAircraft().getItems(params);

        for (DropzoneAircraft dzAircraft : dzAircrafts) {
            Aircraft item = (Aircraft) new Aircraft().getOneById(dzAircraft.getAircraftId());
            if (item != null) {
                aircraft.add(item);
            }
        }

        return aircraft;
    }

    @NonNull
    public HashMap<String, Object> aircraftParemeters() {
        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> whereDropzoneId = new HashMap<>();
        whereDropzoneId.put(Model.FILTER_WHERE_FIELD, DropzoneAircraft.COLUMN_NAME_DROPZONE_ID);
        whereDropzoneId.put(Model.FILTER_WHERE_VALUE, this.getId());

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), whereDropzoneId);

        params.put(Model.FILTER_WHERE, wheres);
        return params;
    }

    public String getFormattedAircraft() {
        String result = "";

        for (Aircraft aircraft : this.getAircraft()) {
            result += aircraft.getName() + ", ";
        }

        return result.length() > 0 ? result.substring(0, result.length() - 2) : " - ";
    }

    /**
     * Override so we can insert the ID before it gets to the parent method.
     *
     * @param contentValues
     */
    @Override
    protected void populateContentValues(ContentValues contentValues) {
        contentValues.put(_ID, this.id);
        super.populateContentValues(contentValues);
    }

    public static ArrayList<String> getCountriesForSelect() {
        ArrayList<String> itemsForSelect = new ArrayList<String>();

        String query = "SELECT " + _ID + ", country FROM " + TABLE_NAME;

        query += " GROUP BY country ORDER BY country ASC";

        Cursor cursor = _db.getReadableDatabase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String string = cursor.getString(1);
                if (string != null) {
                    itemsForSelect.add(string);
                }
                cursor.moveToNext();
            }
        }

        cursor.close();

        return itemsForSelect;
    }

    public static ArrayList<String> getCountiesForSelect(String country) {
        ArrayList<String> itemsForSelect = new ArrayList<String>();

        String query = "SELECT " + _ID + ", local FROM " + TABLE_NAME;

        query += " WHERE country = '" + country + "' GROUP BY local ORDER BY local ASC";

        Cursor cursor = _db.getReadableDatabase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String string = cursor.getString(1);
                if (string != null) {
                    itemsForSelect.add(string);
                }
                cursor.moveToNext();
            }
        }

        cursor.close();

        return itemsForSelect;
    }
}
