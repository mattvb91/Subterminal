package mavonie.subterminal.Models;

import android.content.ContentValues;
import android.support.annotation.Nullable;
import android.util.Pair;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Jobs.SyncExit;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Utils.Subterminal;


/**
 * Exit Model
 */
public class Exit extends Synchronizable {

    public static final Integer TYPE_BUILDING = 1;
    public static final Integer TYPE_ANTENNA = 2;
    public static final Integer TYPE_SPAN = 3;
    public static final Integer TYPE_EARTH = 4;
    public static final Integer TYPE_OTHER = 5;

    public static final Integer DIFFICULTY_BEGINNER = 1;
    public static final Integer DIFFICULTY_INTERMEDIATE = 2;
    public static final Integer DIFFICULTY_ADVANCED = 3;
    public static final Integer DIFFICULTY_EXPERT = 4;

    private static final LinkedHashMap<String, String> object_types;
    private static final Map<Integer, String> difficulty_descriptor;
    private static final Map<Integer, String> difficulty_color;

    static {
        object_types = new LinkedHashMap<>();
        object_types.put(TYPE_BUILDING.toString(), "Building");
        object_types.put(TYPE_ANTENNA.toString(), "Antenna");
        object_types.put(TYPE_SPAN.toString(), "Span");
        object_types.put(TYPE_EARTH.toString(), "Earth");
        object_types.put(TYPE_OTHER.toString(), "Other");

        difficulty_descriptor = new HashMap<Integer, String>();
        difficulty_descriptor.put(DIFFICULTY_BEGINNER, "Beginner");
        difficulty_descriptor.put(DIFFICULTY_INTERMEDIATE, "Intermediate");
        difficulty_descriptor.put(DIFFICULTY_ADVANCED, "Advanced");
        difficulty_descriptor.put(DIFFICULTY_EXPERT, "Expert");

        difficulty_color = new HashMap<Integer, String>();
        difficulty_color.put(DIFFICULTY_BEGINNER, "#1dff00");
        difficulty_color.put(DIFFICULTY_INTERMEDIATE, "#0055ff");
        difficulty_color.put(DIFFICULTY_ADVANCED, "#ff0000");
        difficulty_color.put(DIFFICULTY_EXPERT, "#4f4f4f");
    }

    private String name;
    private String global_id;
    private Integer rockdrop_distance;
    private Integer altitude_to_landing;
    private String description;
    private double latitude;
    private double longtitude;
    private Integer object_type;

    private ExitDetails details;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "exit";

    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_ROCKDROP_DISTANCE = "rockdrop_distance";
    public static final String COLUMN_NAME_ALTITUDE_TO_LANDING = "altitude_to_landing";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_LATITUDE = "latitude";
    public static final String COLUMN_NAME_LONGTITUDE = "longtitude";
    public static final String COLUMN_NAME_OBJECT_TYPE = "object_type";
    public static final String COLUMN_NAME_GLOBAL_ID = "global_id";

    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<String, Integer>();

            dbColumns.put(COLUMN_NAME_NAME, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_ROCKDROP_DISTANCE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_ALTITUDE_TO_LANDING, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DESCRIPTION, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_LATITUDE, TYPE_DOUBLE);
            dbColumns.put(COLUMN_NAME_LONGTITUDE, TYPE_DOUBLE);
            dbColumns.put(COLUMN_NAME_OBJECT_TYPE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_GLOBAL_ID, TYPE_TEXT);

            Synchronizable.setDBColumns(dbColumns);
        }

        return dbColumns;
    }

    /* END DB DEFINITIONS */

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public Integer getRockdropDistance() {
        return rockdrop_distance;
    }

    public void setRockdropDistance(Integer rockdrop_distance) {
        this.rockdrop_distance = rockdrop_distance;
    }

    public Integer getAltitudeToLanding() {
        return altitude_to_landing;
    }

    public void setAltitudeToLanding(Integer altitude_to_landing) {
        this.altitude_to_landing = altitude_to_landing;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getObjectType() {
        return object_type;
    }

    public void setObjectType(Integer object_type) {
        this.object_type = object_type;
    }

    public static LinkedHashMap<String, String> getObjectTypes() {
        return object_types;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public String getFormatedAltitudeToLanding() {
        if (this.getAltitudeToLanding() == null) {
            return "";
        }

        return this.getAltitudeToLanding() + "m (" + Math.round(this.getAltitudeToLanding() * 3.28) + "ft)";
    }

    public String getFormatedRockdrop() {
        if (this.getRockdropDistance() == null) {
            return "";
        }

        return this.getRockdropDistance() + "m (" + Math.round(this.getRockdropDistance() * 3.28) + "ft)";
    }

    public static String getDifficultyColor(int difficulty) {
        return difficulty_color.get(difficulty);
    }

    public static String getDifficultyDescriptor(int difficulty) {
        return difficulty_descriptor.get(difficulty);
    }

    public String getFormattedObjectType() {
        if (this.getObjectType() == null) {
            return "";
        }

        return object_types.get(Integer.toString(this.getObjectType()));
    }

    //TODO make sure this is right
    public String getFormattedRockdropTime() {
        if (this.getRockdropDistance() == null) {
            return "";
        }

        double time = Math.sqrt(2 * this.getRockdropDistance() / 9.8);
        DecimalFormat df = new DecimalFormat("#.#");

        return df.format(time) + "s";
    }

    /**
     * Check to see if the map should be active for this Exit
     *
     * @return boolean
     */
    public boolean isMapActive() {
        if (this.getLatitude() != 0.00 && this.getLongtitude() != 0.00) {
            return true;
        }

        return false;
    }

    /**
     * Just get the first image we can find
     *
     * @return Image
     */
    public Image getThumbImage() {
        return Image.loadThumbForEntity(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Exit exit = (Exit) o;

        if (rockdrop_distance.intValue() != exit.rockdrop_distance.intValue()) return false;
        if (altitude_to_landing.intValue() != exit.altitude_to_landing.intValue()) return false;
        if (Double.compare(exit.latitude, latitude) != 0) return false;
        if (Double.compare(exit.longtitude, longtitude) != 0) return false;
        if (object_type.intValue() != exit.object_type.intValue()) return false;
        if (name != null ? !name.equals(exit.name) : exit.name != null) return false;
        if (global_id != null ? !global_id.equals(exit.global_id) : exit.global_id != null)
            return false;
        if (description != null ? !description.equals(exit.description) : exit.description != null)
            return false;
        return details != null ? details.equals(exit.details) : exit.details == null;

    }

    public String getGlobalId() {
        return global_id;
    }

    public void setGlobalId(String global_id) {
        this.global_id = global_id;
    }

    public boolean isGlobal() {
        return this.getGlobalId() != null;
    }

    /**
     * Check if we already have matching exit and insert/update
     * as appropriate.
     *
     * @param exit
     */
    public static void createOrUpdatePublicExit(Exit exit) {

        if (exit.isGlobal()) {
            //Its a global exit check if we already have it or if an exit with same name exists
            Exit dbExit = (Exit) new Exit().getItem(new Pair<String, String>(COLUMN_NAME_GLOBAL_ID, exit.getGlobalId()));

            //If we have a private exit with the same name lets skip this.
            Exit privateExit = (Exit) new Exit().getItem(new Pair<String, String>(COLUMN_NAME_NAME, exit.getName()));
            if (privateExit != null && privateExit.getGlobalId() == null) {
                return;
            }

            //Check if it equals
            if (dbExit != null) {
                if (!dbExit.equals(exit)) {
                    //Update if it doesnt
                    exit.setId(dbExit.getId());
                    exit.markSynced();
                }
            } else {
                exit.markSynced();
            }
        }
    }

    /**
     * Get the exit details
     *
     * @return
     */
    public ExitDetails getDetails() {
        if (this.details == null) {
            this.details = (ExitDetails) new ExitDetails().getItem(
                    new Pair<String, String>("exit_id", Integer.toString(this.getId()))
            );
        }
        return details;
    }

    public void setDetails(ExitDetails details) {
        this.details = details;
    }

    /**
     * Override because we need to also save the associated
     * details if its a global
     *
     * @return
     */
    public boolean save() {

        boolean res = super.save();
        if (res && this.isGlobal() && this.getDetails() != null) {
            this.getDetails().setExitId(this.getId());
            this.getDetails().save();
        }

        return res;
    }

    @Override
    public void addSyncJob() {
        Subterminal.getJobManager(MainActivity.getActivity())
                .addJobInBackground(new SyncExit(this));
    }

    /**
     * Get all the jumps associated with this exit
     *
     * @return List
     */
    public List<Jump> getJumps() {

        HashMap<String, Object> whereExitID = new HashMap<>();
        whereExitID.put(Model.FILTER_WHERE_FIELD, Jump.COLUMN_NAME_EXIT_ID);
        whereExitID.put(Model.FILTER_WHERE_VALUE, Integer.toString(this.getId()));

        return new Jump().getItems(whereExitID);
    }

    public static List<Exit> getExitsForSync() {

        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> whereGlobalIdNull = new HashMap<>();
        whereGlobalIdNull.put(Model.FILTER_WHERE_FIELD, COLUMN_NAME_GLOBAL_ID);
        whereGlobalIdNull.put(Model.FILTER_WHERE_VALUE, null);

        HashMap<String, Object> whereSyncRequired = new HashMap<>();
        whereSyncRequired.put(Model.FILTER_WHERE_FIELD, COLUMN_SYNCED);
        whereSyncRequired.put(Model.FILTER_WHERE_VALUE, SYNC_REQUIRED);

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), whereGlobalIdNull);
        wheres.put(wheres.size(), whereSyncRequired);

        params.put(Model.FILTER_WHERE, wheres);

        return new Exit().getItems(params);
    }

    //TODO these 2 methods should be merged
    public static List<Exit> getExitsForDelete() {
        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> whereGlobalIdNull = new HashMap<>();
        whereGlobalIdNull.put(Model.FILTER_WHERE_FIELD, COLUMN_NAME_GLOBAL_ID);
        whereGlobalIdNull.put(Model.FILTER_WHERE_VALUE, null);

        HashMap<String, Object> whereDeleteRequired = new HashMap<>();
        whereDeleteRequired.put(Model.FILTER_WHERE_FIELD, COLUMN_DELETED);
        whereDeleteRequired.put(Model.FILTER_WHERE_VALUE, DELETED_TRUE);

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), whereGlobalIdNull);
        wheres.put(wheres.size(), whereDeleteRequired);

        params.put(Model.FILTER_WHERE, wheres);

        return new Exit().getItems(params);
    }

    public boolean delete(boolean updateJumps) {

        //We need to update jumps before deleting
        if (updateJumps) {
            for (Jump jump : this.getJumps()) {
                jump.setExitId(null);
                jump.save();
            }
        }

        return super.delete();
    }

    @Override
    protected void populateContentValues(ContentValues contentValues) {

        if (this._id > 0) {
            contentValues.put(_ID, this._id);
        }

        moveExistingExit();
        super.populateContentValues(contentValues);
    }

    /**
     * This method is only used when a remote_id attribute is set.
     * We need to check if we already have an exit in this position and shuffle it out to a new unused ID
     */
    private void moveExistingExit() {
        if (this.remote_id != null) {
            //Check if we need to move an existing item out of the way.
            Exit existingExit = (Exit) new Exit().getOneById(this.remote_id);

            if (existingExit != null) {
                //get the newId BEFORE deleting existing exit to prevent overlapping
                int newId = new Exit().getNextAutoIncrement();

                existingExit.setDeleted(DELETED_TRUE);
                existingExit.delete(false);

                if (existingExit.getDetails() != null) {
                    existingExit.getDetails().setExitId(newId);
                    existingExit.getDetails().save();
                }

                existingExit._id = newId;
                existingExit.markSynced();
            }

            //Check if we have a global exit with the same incoming name, if we do, remove it
            Exit globalExit = (Exit) new Exit().getItem(new Pair<String, String>(COLUMN_NAME_NAME, this.getName()));
            if (globalExit != null && globalExit.isGlobal()) {
                if (globalExit.getDetails() != null) {
                    globalExit.getDetails().delete();
                }
                globalExit.setDeleted(DELETED_TRUE);
                globalExit.delete();
            }
        }
    }
}
