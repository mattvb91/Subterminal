package mavonie.subterminal.Models.Skydive;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;

import mavonie.subterminal.Models.Synchronizable;

/**
 * Skydive Model
 */
public class Skydive extends Synchronizable {

    private String dropzone_id,
            description,
            date;

    private Integer exit_altitude,
            deploy_altitude,
            delay,
            jump_type;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "skydive";

    public static final String COLUMN_NAME_DROPZONE_ID = "dropzone_id";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_EXIT_ALTITUDE = "exit_altitude";
    public static final String COLUMN_NAME_DEPLOY_ALTITUDE = "deploy_altitude";
    public static final String COLUMN_NAME_DELAY = "delay";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_JUMP_TYPE = "jump_type";
    /* END DB DEFINITIONS*/

    @Override
    public void addSyncJob() {

    }

    private static Map<String, Integer> dbColumns = null;


    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<String, Integer>();

            dbColumns.put(COLUMN_NAME_DROPZONE_ID, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_DATE, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_EXIT_ALTITUDE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DEPLOY_ALTITUDE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DELAY, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DESCRIPTION, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_JUMP_TYPE, TYPE_INTEGER);

            Synchronizable.setDBColumns(dbColumns);
        }

        return dbColumns;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public String getDropzoneId() {
        return dropzone_id;
    }

    public void setDropzoneId(String dropzone_id) {
        this.dropzone_id = dropzone_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getExitAltitude() {
        return exit_altitude;
    }

    public void setExitAltitude(Integer exit_altitude) {
        this.exit_altitude = exit_altitude;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Integer getJumpType() {
        return jump_type;
    }

    public void setJumpType(Integer jump_type) {
        this.jump_type = jump_type;
    }


    public Integer getDeployAltitude() {
        return deploy_altitude;
    }

    public void setDeployAltitude(Integer deploy_altitude) {
        this.deploy_altitude = deploy_altitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Skydive skydive = (Skydive) o;

        if (dropzone_id != null ? !dropzone_id.equals(skydive.dropzone_id) : skydive.dropzone_id != null)
            return false;
        if (description != null ? !description.equals(skydive.description) : skydive.description != null)
            return false;
        if (date != null ? !date.equals(skydive.date) : skydive.date != null) return false;
        if (exit_altitude != null ? !exit_altitude.equals(skydive.exit_altitude) : skydive.exit_altitude != null)
            return false;
        if (delay != null ? !delay.equals(skydive.delay) : skydive.delay != null) return false;
        return jump_type != null ? jump_type.equals(skydive.jump_type) : skydive.jump_type == null;

    }

    private Dropzone _dropzone;

    /**
     * Get the associated Dropzone
     *
     * @return Dropzone|Null
     */
    public Dropzone getDropzone() {
        if (_dropzone != null) {
            _dropzone = (Dropzone) new Dropzone().getItem(new Pair<String, String>(Dropzone.COLUMN_NAME_GLOBAL_ID, getDropzoneId()));
        }

        return _dropzone;
    }
}
