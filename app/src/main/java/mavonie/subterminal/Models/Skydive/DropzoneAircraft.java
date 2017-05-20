package mavonie.subterminal.Models.Skydive;

import java.util.HashMap;
import java.util.Map;

import mavonie.subterminal.Models.Model;


/**
 * Pivot class between dropzones/aircraft
 */
public class DropzoneAircraft extends Model {

    private int dropzone_id,
            aircraft_id;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "skydive_dropzone_aircraft";

    public static final String COLUMN_NAME_DROPZONE_ID = "dropzone_id";
    public static final String COLUMN_NAME_AIRCRAFT_ID = "aircraft_id";
    /* END DB DEFINITIONS */

    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<>();

            dbColumns.put(COLUMN_NAME_DROPZONE_ID, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_AIRCRAFT_ID, TYPE_INTEGER);
        }

        return dbColumns;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public int getDropzoneId() {
        return dropzone_id;
    }

    public void setDropzoneId(int dropzone_id) {
        this.dropzone_id = dropzone_id;
    }

    public int getAircraftId() {
        return aircraft_id;
    }

    public void setAircraftId(int aircraft_id) {
        this.aircraft_id = aircraft_id;
    }
}
