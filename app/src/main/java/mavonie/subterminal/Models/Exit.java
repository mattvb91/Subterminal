package mavonie.subterminal.Models;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mavon on 13/10/16.
 */

public class Exit extends Model {

    private static Integer DIFFICULTY_BEGINNER = 1;
    private static Integer DIFFICULTY_INTERMEDIATE = 2;
    private static Integer DIFFICULTY_ADVANCED = 3;
    private static Integer DIFFICULTY_EXPERT = 4;

    private static final Map<Integer, String> difficulty_descriptor;
    private static final Map<Integer, String> difficulty_color;

    static {
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
    private Integer rockdrop_distance;
    private Integer altitude_to_landing;
    private Integer difficulty_tracking_exit;
    private Integer difficulty_tracking_freefall;
    private Integer difficulty_tracking_landing;
    private Integer difficulty_tracking_overall;
    private Integer difficulty_wingsuit_exit;
    private Integer difficulty_wingsuit_freefall;
    private Integer difficulty_wingsuit_landing;
    private Integer difficulty_wingsuit_overall;
    private String description;
    private String rules;
    private double latitude;
    private double longtitude;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "exit";

    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_ROCKDROP_DISTANCE = "rockdrop_distance";
    public static final String COLUMN_NAME_ALTITUDE_TO_LANDING = "altitude_to_landing";
    public static final String COLUMN_NAME_DIFFICULTY_TRACKING_EXIT = "difficulty_tracking_exit";
    public static final String COLUMN_NAME_DIFFICULTY_TRACKING_FREEFALL = "difficulty_tracking_freefall";
    public static final String COLUMN_NAME_DIFFICULTY_TRACKING_LANDING = "difficulty_tracking_landing";
    public static final String COLUMN_NAME_DIFFICULTY_TRACKING_OVERALL = "difficulty_tracking_overall";
    public static final String COLUMN_NAME_DIFFICULTY_WINGSUIT_EXIT = "difficulty_wingsuit_exit";
    public static final String COLUMN_NAME_DIFFICULTY_WINGSUIT_FREEFALL = "difficulty_wingsuit_freefall";
    public static final String COLUMN_NAME_DIFFICULTY_WINGSUIT_LANDING = "difficulty_wingsuit_landing";
    public static final String COLUMN_NAME_DIFFICULTY_WINGSUIT_OVERALL = "difficulty_wingsuit_overall";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_RULES = "rules";
    public static final String COLUMN_NAME_LATITUDE = "lat";
    public static final String COLUMN_NAME_LONGTITUDE = "long";


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

    public Integer getDifficulty_tracking_exit() {
        return difficulty_tracking_exit;
    }

    public void setDifficulty_tracking_exit(Integer difficulty_tracking_exit) {
        this.difficulty_tracking_exit = difficulty_tracking_exit;
    }

    public Integer getDifficulty_tracking_freefall() {
        return difficulty_tracking_freefall;
    }

    public void setDifficulty_tracking_freefall(Integer difficulty_tracking_freefall) {
        this.difficulty_tracking_freefall = difficulty_tracking_freefall;
    }

    public Integer getDifficulty_tracking_landing() {
        return difficulty_tracking_landing;
    }

    public void setDifficulty_tracking_landing(Integer difficulty_tracking_landing) {
        this.difficulty_tracking_landing = difficulty_tracking_landing;
    }

    public Integer getDifficulty_tracking_overall() {
        return difficulty_tracking_overall;
    }

    public void setDifficulty_tracking_overall(Integer difficulty_tracking_overall) {
        this.difficulty_tracking_overall = difficulty_tracking_overall;
    }

    public Integer getDifficulty_wingsuit_exit() {
        return difficulty_wingsuit_exit;
    }

    public void setDifficulty_wingsuit_exit(Integer difficulty_wingsuit_exit) {
        this.difficulty_wingsuit_exit = difficulty_wingsuit_exit;
    }

    public Integer getDifficulty_wingsuit_freefall() {
        return difficulty_wingsuit_freefall;
    }

    public void setDifficulty_wingsuit_freefall(Integer difficulty_wingsuit_freefall) {
        this.difficulty_wingsuit_freefall = difficulty_wingsuit_freefall;
    }

    public Integer getDifficulty_wingsuit_landing() {
        return difficulty_wingsuit_landing;
    }

    public void setDifficulty_wingsuit_landing(Integer difficulty_wingsuit_landing) {
        this.difficulty_wingsuit_landing = difficulty_wingsuit_landing;
    }

    public Integer getDifficulty_wingsuit_overall() {
        return difficulty_wingsuit_overall;
    }

    public void setDifficulty_wingsuit_overall(Integer difficulty_wingsuit_overall) {
        this.difficulty_wingsuit_overall = difficulty_wingsuit_overall;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    @Override
    public Exit populateFromCursor(Cursor cursor) {

        try {
            Exit exit = new Exit();

            int idIndex = cursor.getColumnIndexOrThrow(_ID);
            int name = cursor.getColumnIndexOrThrow(COLUMN_NAME_NAME);
            int rockDropDistance = cursor.getColumnIndexOrThrow(COLUMN_NAME_ROCKDROP_DISTANCE);
            int altitudeToLanding = cursor.getColumnIndexOrThrow(COLUMN_NAME_ALTITUDE_TO_LANDING);
            int difficultyTrackingExit = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_TRACKING_EXIT);
            int difficultyTrackingfreefall = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_TRACKING_FREEFALL);
            int difficultyTrackinglanding = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_TRACKING_LANDING);
            int difficultyTrackingOverall = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_TRACKING_OVERALL);
            int difficultyWingsuitExit = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_WINGSUIT_EXIT);
            int difficultyWingsuitfreefall = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_WINGSUIT_FREEFALL);
            int difficultyWingsuitlanding = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_WINGSUIT_LANDING);
            int difficultyWingsuitOverall = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_WINGSUIT_OVERALL);
            int description = cursor.getColumnIndexOrThrow(COLUMN_NAME_DESCRIPTION);
            int rules = cursor.getColumnIndexOrThrow(COLUMN_NAME_RULES);
            int latitude = cursor.getColumnIndexOrThrow(COLUMN_NAME_LATITUDE);
            int longtitude = cursor.getColumnIndexOrThrow(COLUMN_NAME_LONGTITUDE);

            exit.setId(cursor.getInt(idIndex));
            exit.setName(cursor.getString(name));
            exit.setRockdrop_distance(cursor.getInt(rockDropDistance));
            exit.setAltitude_to_landing(cursor.getInt(altitudeToLanding));
            exit.setDifficulty_tracking_exit(cursor.getInt(difficultyTrackingExit));
            exit.setDifficulty_tracking_freefall(cursor.getInt(difficultyTrackingfreefall));
            exit.setDifficulty_tracking_landing(cursor.getInt(difficultyTrackinglanding));
            exit.setDifficulty_tracking_overall(cursor.getInt(difficultyTrackingOverall));
            exit.setDifficulty_wingsuit_exit(cursor.getInt(difficultyWingsuitExit));
            exit.setDifficulty_wingsuit_freefall(cursor.getInt(difficultyWingsuitfreefall));
            exit.setDifficulty_wingsuit_landing(cursor.getInt(difficultyWingsuitlanding));
            exit.setDifficulty_wingsuit_overall(cursor.getInt(difficultyWingsuitOverall));
            exit.setDescription(cursor.getString(description));

            String rulesText = "";
            if (!cursor.isNull(rules)) {
                rulesText = cursor.getString(rules);
            }
            exit.setRules(rulesText);

            exit.setLatitude(Double.parseDouble(cursor.getString(latitude)));
            exit.setLongtitude(Double.parseDouble(cursor.getString(longtitude)));

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
    }

    @Override
    String getTableName() {
        return "exit";
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
}
