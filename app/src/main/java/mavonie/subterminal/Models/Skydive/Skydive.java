package mavonie.subterminal.Models.Skydive;

import android.database.Cursor;

import com.pixplicity.easyprefs.library.Prefs;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Jobs.Skydive.SyncSkydive;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Signature;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Preference;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Skydive Model
 */
public class Skydive extends Synchronizable {

    private String description,
            date;

    private Integer dropzone_id,
            exit_altitude,
            deploy_altitude,
            delay,
            jump_type,
            aircraft_id,
            rig_id,
            height_unit,
            suit_id;

    private Integer cutaway = CUTAWAY_NO;

    //use this to get the current position from the query
    private int row_id;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "skydive";

    public static final String COLUMN_NAME_DROPZONE_ID = "dropzone_id";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_EXIT_ALTITUDE = "exit_altitude";
    public static final String COLUMN_NAME_DEPLOY_ALTITUDE = "deploy_altitude";
    public static final String COLUMN_NAME_DELAY = "delay";
    public static final String COLUMN_NAME_AIRCRAFT_ID = "aircraft_id";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_JUMP_TYPE = "jump_type";
    public static final String COLUMN_NAME_JUMP_RIG_ID = "rig_id";
    public static final String COLUMN_NAME_JUMP_HEIGHT_UNIT = "height_unit";
    public static final String COLUMN_NAME_SUIT_ID = "suit_id";
    public static final String COLUMN_NAME_CUTAWAY = "cutaway";
    /* END DB DEFINITIONS*/

    @Override
    public void addSyncJob() {
        Subterminal.getJobManager(MainActivity.getActivity())
                .addJobInBackground(new SyncSkydive(this));
    }

    private static Map<String, Integer> dbColumns = null;


    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<String, Integer>();

            dbColumns.put(COLUMN_NAME_DROPZONE_ID, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DATE, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_EXIT_ALTITUDE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DEPLOY_ALTITUDE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DELAY, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DESCRIPTION, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_JUMP_TYPE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_JUMP_RIG_ID, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_AIRCRAFT_ID, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_JUMP_HEIGHT_UNIT, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_SUIT_ID, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_CUTAWAY, TYPE_INTEGER);
            dbColumns.put("row_id", TYPE_OTHER);

            Synchronizable.setDBColumns(dbColumns);
        }

        return dbColumns;
    }

    @Override
    protected void populateCustomFromCursor(Field field, Cursor cursor) {
        switch (field.getName()) {
            case "row_id":
                this.setRowId(cursor.getCount() - cursor.getPosition());
                break;
        }
    }

    public static final int CUTAWAY_YES = 1;
    public static final int CUTAWAY_NO = 0;

    public static final int SKYDIVE_TYPE_BELLY = 1;
    public static final int SKYDIVE_TYPE_FREEFLY = 2;
    public static final int SKYDIVE_TYPE_HYBRID = 3;
    public static final int SKYDIVE_TYPE_WINGSUIT = 4;
    public static final int SKYDIVE_TYPE_TRACKING = 5;
    public static final int SKYDIVE_TYPE_ANGLE = 6;
    public static final int SKYDIVE_TYPE_HEADUP = 7;
    public static final int SKYDIVE_TYPE_HEADDOWN = 8;
    public static final int SKYDIVE_TYPE_BIGWAY = 9;
    public static final int SKYDIVE_TYPE_FREESTYLE = 10;
    public static final int SKYDIVE_TYPE_FORMATION = 11;

    public static final int SKYDIVE_TYPE_CANOPY_SWOOP = 20;
    public static final int SKYDIVE_TYPE_CANOPY_ACCURACY = 21;
    public static final int SKYDIVE_TYPE_CANOPY_HOPNPOP = 22;
    public static final int SKYDIVE_TYPE_CANOPY_CREW = 23;
    public static final int SKYDIVE_TYPE_CANOPY_CROSSCOUNTRY = 24;
    public static final int SKYDIVE_TYPE_CANOPY_HIGHPULL = 25;
    public static final int SKYDIVE_TYPE_CANOPY_XRW = 26;
    public static final int SKYDIVE_TYPE_CANOPY_FLAG = 27;

    public static final int SKYDIVE_TYPE_VIDEO = 40;

    public static final int SKYDIVE_TYPE_INSTRUCTOR_TANDEM = 50;
    public static final int SKYDIVE_TYPE_INSTRUCTOR_AFF = 51;
    public static final int SKYDIVE_TYPE_INSTRUCTOR_LOADORGANIZER = 52;
    public static final int SKYDIVE_TYPE_INSTRUCTOR_COACH = 53;
    public static final int SKYDIVE_TYPE_INSTRUCTOR_STATICLINE = 54;

    public static final int SKYDIVE_TYPE_MILITARY_HALO = 60;
    public static final int SKYDIVE_TYPE_MILITARY_SLICK = 61;
    public static final int SKYDIVE_TYPE_MILITARY_COMBAT = 62;

    public static final int SKYDIVE_TYPE_OTHER_NAKED = 70;
    public static final int SKYDIVE_TYPE_OTHER_NIGHT = 71;
    public static final int SKYDIVE_TYPE_OTHER_MR_BILL = 72;
    public static final int SKYDIVE_TYPE_OTHER_DEMO = 73;

    public static final int SKYDIVE_TYPE_STUDENT_AFF = 80;
    public static final int SKYDIVE_TYPE_STUDENT_STATICLINE = 81;
    public static final int SKYDIVE_TYPE_STUDENT_TANDEM = 82;
    public static final int SKYDIVE_TYPE_STUDENT_CONSOLE = 83;

    private static LinkedHashMap<Integer, String> jumpTypes = null;

    static {
        jumpTypes = new LinkedHashMap<Integer, String>();

        jumpTypes.put(SKYDIVE_TYPE_BELLY, "Belly");
        jumpTypes.put(SKYDIVE_TYPE_FREEFLY, "Freefly");
        jumpTypes.put(SKYDIVE_TYPE_HYBRID, "Hybrid");
        jumpTypes.put(SKYDIVE_TYPE_WINGSUIT, "Wingsuit");
        jumpTypes.put(SKYDIVE_TYPE_TRACKING, "Tracking");
        jumpTypes.put(SKYDIVE_TYPE_ANGLE, "Angle");
        jumpTypes.put(SKYDIVE_TYPE_HEADUP, "Headup");
        jumpTypes.put(SKYDIVE_TYPE_HEADDOWN, "Headdown");
        jumpTypes.put(SKYDIVE_TYPE_BIGWAY, "Bigway");
        jumpTypes.put(SKYDIVE_TYPE_FREESTYLE, "Freestyle");
        jumpTypes.put(SKYDIVE_TYPE_FORMATION, "Formation");

        jumpTypes.put(SKYDIVE_TYPE_CANOPY_SWOOP, "Canopy - Swoop");
        jumpTypes.put(SKYDIVE_TYPE_CANOPY_ACCURACY, "Canopy - Accuracy");
        jumpTypes.put(SKYDIVE_TYPE_CANOPY_HOPNPOP, "Canopy - Hop-n-Pop");
        jumpTypes.put(SKYDIVE_TYPE_CANOPY_CREW, "Canopy - CReW");
        jumpTypes.put(SKYDIVE_TYPE_CANOPY_CROSSCOUNTRY, "Canopy - Cross Country");
        jumpTypes.put(SKYDIVE_TYPE_CANOPY_HIGHPULL, "Canopy - High Pull");
        jumpTypes.put(SKYDIVE_TYPE_CANOPY_XRW, "Canopy - XRW");
        jumpTypes.put(SKYDIVE_TYPE_CANOPY_FLAG, "Canopy - Flag");

        jumpTypes.put(SKYDIVE_TYPE_VIDEO, "Video");

        jumpTypes.put(SKYDIVE_TYPE_INSTRUCTOR_TANDEM, "Instructor - Tandem");
        jumpTypes.put(SKYDIVE_TYPE_INSTRUCTOR_AFF, "Instructor - AFF");
        jumpTypes.put(SKYDIVE_TYPE_INSTRUCTOR_LOADORGANIZER, "Instructor - Load Organizer");
        jumpTypes.put(SKYDIVE_TYPE_INSTRUCTOR_COACH, "Instructor - Coach");
        jumpTypes.put(SKYDIVE_TYPE_INSTRUCTOR_STATICLINE, "Instructor - Static Line");

        jumpTypes.put(SKYDIVE_TYPE_MILITARY_HALO, "Military - HALO ");
        jumpTypes.put(SKYDIVE_TYPE_MILITARY_SLICK, "Military - Slick ");
        jumpTypes.put(SKYDIVE_TYPE_MILITARY_COMBAT, "Military - Combat");

        jumpTypes.put(SKYDIVE_TYPE_OTHER_NAKED, "Other - Naked");
        jumpTypes.put(SKYDIVE_TYPE_OTHER_NIGHT, "Other - Night");
        jumpTypes.put(SKYDIVE_TYPE_OTHER_MR_BILL, "Other - Mr. Bill");
        jumpTypes.put(SKYDIVE_TYPE_OTHER_DEMO, "Other - Demo");

        jumpTypes.put(SKYDIVE_TYPE_STUDENT_AFF, "Student - AFF");
        jumpTypes.put(SKYDIVE_TYPE_STUDENT_STATICLINE, "Student - Static Line");
        jumpTypes.put(SKYDIVE_TYPE_STUDENT_TANDEM, "Student - Tanden");
        jumpTypes.put(SKYDIVE_TYPE_STUDENT_CONSOLE, "Student - Console");
    }

    public static LinkedHashMap<Integer, String> getJumpTypes() {
        return jumpTypes;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public Integer getDropzoneId() {
        return dropzone_id;
    }

    public void setDropzoneId(Integer dropzone_id) {
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

    public Integer getRigId() {
        return rig_id;
    }

    public void setRigId(Integer rig_id) {
        this.rig_id = rig_id;
    }

    public Integer getDeployAltitude() {
        return deploy_altitude;
    }

    public void setDeployAltitude(Integer deploy_altitude) {
        this.deploy_altitude = deploy_altitude;
    }

    public Integer getHeightUnit() {
        return height_unit;
    }

    public void setHeightUnit(Integer heightUnit) {
        this.height_unit = heightUnit;
    }

    public Integer getAircraftId() {
        return aircraft_id;
    }

    public void setAircraftId(Integer aircraft_id) {
        this.aircraft_id = aircraft_id;
    }

    public Integer getSuitId() {
        return suit_id;
    }

    public void setSuitId(Integer suit_id) {
        this.suit_id = suit_id;
    }

    public Integer getCutaway() {
        return cutaway;
    }

    public void setCutaway(Integer cutaway) {
        this.cutaway = cutaway;
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
        if (rig_id != null ? !rig_id.equals(skydive.rig_id) : skydive.rig_id != null) return false;
        if (aircraft_id != null ? !aircraft_id.equals(skydive.aircraft_id) : skydive.aircraft_id != null)
            return false;
        if (height_unit != null ? !height_unit.equals(skydive.height_unit) : skydive.height_unit != null)
            return false;
        if (suit_id != null ? !suit_id.equals(skydive.suit_id) : skydive.suit_id != null)
            return false;
        return jump_type != null ? jump_type.equals(skydive.jump_type) : skydive.jump_type == null;

    }

    private Dropzone _dropzone;

    /**
     * Get the associated Dropzone
     *
     * @return Dropzone|Null
     */
    public Dropzone getDropzone() {
        if (_dropzone == null && this.getDropzoneId() != null) {
            _dropzone = (Dropzone) new Dropzone().getOneById(this.getDropzoneId());
        }

        return _dropzone;
    }

    private Rig _rig;

    /**
     * Get the associated Rig model
     *
     * @return Rig|Null
     */
    public Rig getRig() {
        if (_rig == null && this.rig_id != null) {
            _rig = (Rig) new Rig().getOneById(this.rig_id);
        }

        return _rig;
    }

    /**
     * Get all signatures associated with this jump.
     *
     * @return
     */
    public List<Signature> getSignatures() {
        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> whereEntityType = new HashMap<>();
        whereEntityType.put(Model.FILTER_WHERE_FIELD, Signature.COLUMN_NAME_ENTITY_TYPE);
        whereEntityType.put(Model.FILTER_WHERE_VALUE, Signature.getEntityTypeFromModel(this));

        HashMap<String, Object> whereId = new HashMap<>();
        whereId.put(Model.FILTER_WHERE_FIELD, Signature.COLUMN_NAME_ENTITY_ID);
        whereId.put(Model.FILTER_WHERE_VALUE, this.getId());

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), whereId);
        wheres.put(wheres.size(), whereEntityType);

        params.put(Model.FILTER_WHERE, wheres);

        return new Signature().getItems(params);
    }

    private Aircraft _aircraft;

    public Aircraft getAircraft() {
        if (this._aircraft == null && this.getAircraftId() != null) {
            this._aircraft = (Aircraft) new Aircraft().getOneById(this.getAircraftId());
        }

        return this._aircraft;
    }

    public int getRowId() {
        int startJump = Prefs.getInt(Preference.PREFS_SKYDIVE_START_COUNT, 0);

        if (startJump > 0) {
            this.row_id += (startJump - 1);
        }

        return this.row_id;
    }

    public void setRowId(int row_id) {
        this.row_id = row_id;
    }

    public static List<Skydive> getSkydivesForSync() {
        return new Skydive().getItems(getSyncRequiredParams());
    }

    public static List<Skydive> getSkydivesForDelete() {
        return new Skydive().getItems(getDeleteRequiredParams());
    }
}
