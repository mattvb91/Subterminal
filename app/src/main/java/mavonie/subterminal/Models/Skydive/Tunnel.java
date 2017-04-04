package mavonie.subterminal.Models.Skydive;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mavonie.subterminal.Models.Model;

/**
 * Tunnel model
 */
public class Tunnel extends Model {

    public int id; //THIS IS JUST TO SYNC TO REMOTE DB ID

    private String name,
            website,
            phone,
            email,
            description,
            formatted_address,
            local,
            country;

    private Integer featured = 0;

    private double latitude,
            longtitude,
            tunnel_diameter,
            tunnel_height;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "skydive_tunnel";

    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_LATITUDE = "latitude";
    public static final String COLUMN_NAME_LONGTITUDE = "longtitude";
    public static final String COLUMN_NAME_WEBSITE = "website";
    public static final String COLUMN_NAME_PHONE = "phone";
    public static final String COLUMN_NAME_EMAIL = "email";
    public static final String COLUMN_NAME_ADDRESS_FORMATTED_ADDRESS = "formatted_address";
    public static final String COLUMN_NAME_ADDRESS_LEVEL_1 = "local";
    public static final String COLUMN_NAME_ADDRESS_LINE_COUNTRY = "country";
    public static final String COLUMN_NAME_FEATURED = "featured";
    public static final String COLUMN_TUNNEL_DIAMETER = "tunnel_diameter";
    public static final String COLUMN_TUNNEL_HEIGHT = "tunnel_height";
    /* END DB DEFINITIONS */

    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<String, Integer>();

            dbColumns.put(COLUMN_NAME_NAME, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_DESCRIPTION, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_LATITUDE, TYPE_DOUBLE);
            dbColumns.put(COLUMN_NAME_LONGTITUDE, TYPE_DOUBLE);
            dbColumns.put(COLUMN_NAME_WEBSITE, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_PHONE, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_EMAIL, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_ADDRESS_FORMATTED_ADDRESS, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_ADDRESS_LEVEL_1, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_ADDRESS_LINE_COUNTRY, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_FEATURED, TYPE_INTEGER);
            dbColumns.put(COLUMN_TUNNEL_DIAMETER, TYPE_DOUBLE);
            dbColumns.put(COLUMN_TUNNEL_HEIGHT, TYPE_DOUBLE);

            Model.setDBColumns(dbColumns);
        }

        return dbColumns;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getFormattedAddress() {
        return formatted_address;
    }

    public String getLocal() {
        return local;
    }

    public String getCountry() {
        return country;
    }

    public Integer getFeatured() {
        return featured;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public double getTunnelDiameter() {
        return tunnel_diameter;
    }

    public double getTunnelHeight() {
        return tunnel_height;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFormattedAddress(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setFeatured(Integer featured) {
        this.featured = featured;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public void setTunnelDiameter(double tunnel_diameter) {
        this.tunnel_diameter = tunnel_diameter;
    }

    public void setTunnelHeight(double tunnel_height) {
        this.tunnel_height = tunnel_height;
    }

    /**
     * Check to see if the map should be active for this Tunnel
     *
     * @return boolean
     */
    public boolean isMapActive() {
        return this.getLatitude() != 0.00 && this.getLongtitude() != 0.00;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tunnel tunnel = (Tunnel) o;

        if (Double.compare(tunnel.latitude, latitude) != 0) return false;
        if (Double.compare(tunnel.longtitude, longtitude) != 0) return false;
        if (Double.compare(tunnel.tunnel_diameter, tunnel_diameter) != 0) return false;
        if (Double.compare(tunnel.tunnel_height, tunnel_height) != 0) return false;
        if (name != null ? !name.equals(tunnel.name) : tunnel.name != null) return false;
        if (website != null ? !website.equals(tunnel.website) : tunnel.website != null)
            return false;
        if (phone != null ? !phone.equals(tunnel.phone) : tunnel.phone != null) return false;
        if (email != null ? !email.equals(tunnel.email) : tunnel.email != null) return false;
        if (description != null ? !description.equals(tunnel.description) : tunnel.description != null)
            return false;
        if (formatted_address != null ? !formatted_address.equals(tunnel.formatted_address) : tunnel.formatted_address != null)
            return false;
        if (local != null ? !local.equals(tunnel.local) : tunnel.local != null) return false;
        if (country != null ? !country.equals(tunnel.country) : tunnel.country != null)
            return false;
        return featured != null ? featured.equals(tunnel.featured) : tunnel.featured == null;
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

        //Add empty selection
        itemsForSelect.add(EMPTY_LIST_ITEM);

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
