package mavonie.subterminal.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Jobs.PostExit;
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
    public static final String COLUMN_NAME_LATITUDE = "lat";
    public static final String COLUMN_NAME_LONGTITUDE = "long";
    public static final String COLUMN_NAME_OBJECT_TYPE = "object_type";
    public static final String COLUMN_NAME_GLOBAL_ID = "global_id";

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

    public Exit() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRockdrop_distance() {
        return rockdrop_distance;
    }

    public void setRockdrop_distance(Integer rockdrop_distance) {
        this.rockdrop_distance = rockdrop_distance;
    }

    public Integer getAltitude_to_landing() {
        return altitude_to_landing;
    }

    public void setAltitude_to_landing(Integer altitude_to_landing) {
        this.altitude_to_landing = altitude_to_landing;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getObject_type() {
        return object_type;
    }

    public void setObject_type(Integer object_type) {
        this.object_type = object_type;
    }

    public static LinkedHashMap<String, String> getObject_types() {
        return object_types;
    }

    @Override
    public Exit populateFromCursor(Cursor cursor) {

        try {
            Exit exit = new Exit();

            int idIndex = cursor.getColumnIndexOrThrow(_ID);
            int name = cursor.getColumnIndexOrThrow(COLUMN_NAME_NAME);
            int rockDropDistance = cursor.getColumnIndexOrThrow(COLUMN_NAME_ROCKDROP_DISTANCE);
            int altitudeToLanding = cursor.getColumnIndexOrThrow(COLUMN_NAME_ALTITUDE_TO_LANDING);
            int description = cursor.getColumnIndexOrThrow(COLUMN_NAME_DESCRIPTION);
            int latitude = cursor.getColumnIndexOrThrow(COLUMN_NAME_LATITUDE);
            int longtitude = cursor.getColumnIndexOrThrow(COLUMN_NAME_LONGTITUDE);
            int object_type = cursor.getColumnIndexOrThrow(COLUMN_NAME_OBJECT_TYPE);
            int global_id = cursor.getColumnIndexOrThrow(COLUMN_NAME_GLOBAL_ID);

            exit.setId(cursor.getInt(idIndex));
            exit.setName(cursor.getString(name));
            exit.setRockdrop_distance(cursor.getInt(rockDropDistance));
            exit.setAltitude_to_landing(cursor.getInt(altitudeToLanding));
            exit.setDescription(cursor.getString(description));
            exit.setGlobal_id(cursor.getString(global_id));

            exit.setLatitude(Double.parseDouble(cursor.getString(latitude)));
            exit.setLongtitude(Double.parseDouble(cursor.getString(longtitude)));
            exit.setObject_type(cursor.getInt(object_type));

            exit.populateSynchronizationFromCursor(cursor);

            return exit;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    void populateContentValues(ContentValues contentValues) {
        contentValues.put(COLUMN_NAME_DESCRIPTION, this.getDescription());
        contentValues.put(COLUMN_NAME_NAME, this.getName());
        contentValues.put(COLUMN_NAME_LATITUDE, this.getLatitude());
        contentValues.put(COLUMN_NAME_LONGTITUDE, this.getLongtitude());
        contentValues.put(COLUMN_NAME_ROCKDROP_DISTANCE, this.getRockdrop_distance());
        contentValues.put(COLUMN_NAME_ALTITUDE_TO_LANDING, this.getAltitude_to_landing());
        contentValues.put(COLUMN_NAME_OBJECT_TYPE, this.getObject_type());
        contentValues.put(COLUMN_NAME_GLOBAL_ID, this.getGlobal_id());

        this.populateSynchronizationContentValues(contentValues);
    }

    @Override
    String getTableName() {
        return TABLE_NAME;
    }

    public String getFormatedAltitudeToLanding() {
        return this.getAltitude_to_landing() + "m (" + Math.round(this.getAltitude_to_landing() * 3.28) + "ft)";
    }

    public String getFormatedRockdrop() {
        return this.getRockdrop_distance() + "m (" + Math.round(this.getRockdrop_distance() * 3.28) + "ft)";
    }

    public static String getDifficultyColor(int difficulty) {
        return difficulty_color.get(difficulty);
    }

    public static String getDifficultyDescriptor(int difficulty) {
        return difficulty_descriptor.get(difficulty);
    }

    public String getFormattedObjectType() {
        return object_types.get(Integer.toString(this.getObject_type()));
    }

    //TODO make sure this is right
    public String getFormattedRockdropTime() {
        double time = Math.sqrt(2 * this.getRockdrop_distance() / 9.8);
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

        if (Double.compare(exit.latitude, latitude) != 0) return false;
        if (Double.compare(exit.longtitude, longtitude) != 0) return false;
        if (!name.equals(exit.name)) return false;
        if (rockdrop_distance != null ? !rockdrop_distance.equals(exit.rockdrop_distance) : exit.rockdrop_distance != null)
            return false;
        if (altitude_to_landing != null ? !altitude_to_landing.equals(exit.altitude_to_landing) : exit.altitude_to_landing != null)
            return false;
        if (description != null ? !description.equals(exit.description) : exit.description != null)
            return false;
        if (object_type != null ? !object_type.equals(exit.object_type) : exit.object_type != null)
            return false;
        if (global_id != null ? !global_id.equals(exit.global_id) : exit.global_id != null)
            return false;
        if (this.getDetails() != null ? !details.equals(exit.details) : exit.details != null)
            return false;

        return true;
    }

    public String getGlobal_id() {
        return global_id;
    }

    public void setGlobal_id(String global_id) {
        this.global_id = global_id;
    }

    public boolean isGlobal() {
        return this.getGlobal_id() != null;
    }

    /**
     * Check if we already have matching exit and insert/update
     * as appropriate.
     *
     * @param exit
     */
    public static void createOrUpdate(Exit exit) {

        if (exit.isGlobal()) {
            //Its a global exit check if we already have it.
            Exit dbExit = (Exit) new Exit().getItem(new Pair<String, String>(COLUMN_NAME_GLOBAL_ID, exit.getGlobal_id()));

            //Check if it equals
            if (dbExit != null) {
                if (!dbExit.equals(exit)) {
                    //Update if it doesnt
                    exit.setId(dbExit.getId());
                    exit.save();
                }
            } else {
                exit.save();
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
        if (res && this.isGlobal()) {
            this.getDetails().setExit_id(this.getId());
            this.getDetails().save();
        }

        //Upload if user is premium
        if (Subterminal.getUser().isPremium() && !Subterminal.isTesting()) {
            Subterminal.getJobManager(MainActivity.getActivity()).addJobInBackground(
                    new PostExit(this)
            );
        }

        return res;
    }

    public String getJobTag() {
        return "exit_" + this.getId();
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
}
