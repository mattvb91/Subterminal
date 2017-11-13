package mavonie.subterminal.Models;

import android.database.Cursor;
import android.support.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.Synchronized;
import retrofit2.Call;


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

        difficulty_descriptor = new HashMap<>();
        difficulty_descriptor.put(DIFFICULTY_BEGINNER, "Beginner");
        difficulty_descriptor.put(DIFFICULTY_INTERMEDIATE, "Intermediate");
        difficulty_descriptor.put(DIFFICULTY_ADVANCED, "Advanced");
        difficulty_descriptor.put(DIFFICULTY_EXPERT, "Expert");

        difficulty_color = new HashMap<>();
        difficulty_color.put(DIFFICULTY_BEGINNER, "#1dff00");
        difficulty_color.put(DIFFICULTY_INTERMEDIATE, "#0055ff");
        difficulty_color.put(DIFFICULTY_ADVANCED, "#ff0000");
        difficulty_color.put(DIFFICULTY_EXPERT, "#4f4f4f");
    }

    private String name,
            description;

    private Integer rockdrop_distance,
            altitude_to_landing,
            object_type,
            height_unit;

    private double latitude,
            longtitude;

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
    public static final String COLUMN_NAME_HEIGHT_UNIT = "height_unit";

    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<>();

            dbColumns.put(COLUMN_NAME_NAME, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_ROCKDROP_DISTANCE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_ALTITUDE_TO_LANDING, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DESCRIPTION, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_LATITUDE, TYPE_DOUBLE);
            dbColumns.put(COLUMN_NAME_LONGTITUDE, TYPE_DOUBLE);
            dbColumns.put(COLUMN_NAME_OBJECT_TYPE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_HEIGHT_UNIT, TYPE_INTEGER);

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

    public Integer getHeightUnit() {
        return height_unit;
    }

    public void setHeightUnit(Integer heightUnit) {
        this.height_unit = heightUnit;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
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
        return this.getLatitude() != 0.00 && this.getLongtitude() != 0.00;

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
        if (description != null ? !description.equals(exit.description) : exit.description != null)
            return false;
        if (height_unit != null ? !height_unit.equals(exit.height_unit) : exit.height_unit != null)
            return false;
        return details != null ? details.equals(exit.details) : exit.details == null;

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

        HashMap<String, Object> whereSyncRequired = new HashMap<>();
        whereSyncRequired.put(Model.FILTER_WHERE_FIELD, COLUMN_SYNCED);
        whereSyncRequired.put(Model.FILTER_WHERE_VALUE, SYNC_REQUIRED);

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), whereSyncRequired);

        params.put(Model.FILTER_WHERE, wheres);

        return new Exit().getItems(params);
    }

    //TODO these 2 methods should be merged
    public static List<Exit> getExitsForDelete() {
        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> whereDeleteRequired = new HashMap<>();
        whereDeleteRequired.put(Model.FILTER_WHERE_FIELD, COLUMN_DELETED);
        whereDeleteRequired.put(Model.FILTER_WHERE_VALUE, DELETED_TRUE);

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), whereDeleteRequired);

        params.put(Model.FILTER_WHERE, wheres);

        return new Exit().getItems(params);
    }

    @Override
    public boolean delete() {
        return this.delete(true);
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

    /**
     * Get the top jumped exits
     *
     * @return int
     */
    public static LinkedHashMap<Exit, Integer> getTop3Exits() {
        String query = "SELECT exit_id, count(exit_id) as total_count FROM " + Jump.TABLE_NAME + " GROUP BY exit_id ORDER BY total_count DESC LIMIT 3";

        Cursor cursor = _db.getReadableDatabase().rawQuery(query, null);
        LinkedHashMap<Exit, Integer> results = new LinkedHashMap<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int exitId = cursor.getInt(0);
                int jumpsFromExit = cursor.getInt(1);
                Exit exit = (Exit) new Exit().getOneById(exitId);

                if (exitId != 0 && exit != null) {
                    results.put(exit, jumpsFromExit);
                }
                cursor.moveToNext();
            }
        }

        cursor.close();

        return results;
    }

    @Override
    public Call getSyncEndpoint() {
        return Subterminal.getApi().getEndpoints().syncExit(this);
    }

    @Override
    public Call<Void> getDeleteEndpoint() {
        return Subterminal.getApi().getEndpoints().deleteExit(this.getId());
    }

    @Override
    public Call<List<Exit>> getDownloadEndpoint() {
        return Subterminal.getApi().getEndpoints().downloadExits(Synchronized.getLastSyncPref(this.getSyncIdentifier()));
    }

    @Override
    public String getSyncIdentifier() {
        return Synchronized.PREF_LAST_SYNC_EXITS;
    }
}
