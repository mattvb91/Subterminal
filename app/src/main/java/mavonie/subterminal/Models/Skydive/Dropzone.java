package mavonie.subterminal.Models.Skydive;

import java.util.HashMap;
import java.util.Map;

import mavonie.subterminal.Models.Model;

/**
 * Dropzone Model
 */
public class Dropzone extends Model {

    private String global_id,
            name,
            description,
            website,
            phone,
            email,
            address_line_1,
            address_line_2,
            address_country;

    private double latitude,
            longtitude;

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
    public static final String COLUMN_NAME_ADDRESS_LINE_1 = "address_line_1";
    public static final String COLUMN_NAME_ADDRESS_LINE_2 = "address_line_2";
    public static final String COLUMN_NAME_ADDRESS_LINE_COUNTRY = "address_country";

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
            dbColumns.put(COLUMN_NAME_ADDRESS_LINE_1, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_ADDRESS_LINE_2, TYPE_TEXT);
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

    public String getAddressLine1() {
        return address_line_1;
    }

    public void setAddressLine1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddressLine2() {
        return address_line_2;
    }

    public void setAddressLine2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getAddressCountry() {
        return address_country;
    }

    public void setAddressCountry(String address_line_country) {
        this.address_country = address_line_country;
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
        if (address_line_1 != null ? !address_line_1.equals(dropzone.address_line_1) : dropzone.address_line_1 != null)
            return false;
        if (address_line_2 != null ? !address_line_2.equals(dropzone.address_line_2) : dropzone.address_line_2 != null)
            return false;
        return address_country != null ? address_country.equals(dropzone.address_country) : dropzone.address_country == null;

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
}
