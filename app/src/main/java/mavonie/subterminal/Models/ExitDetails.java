package mavonie.subterminal.Models;

import java.util.HashMap;
import java.util.Map;

public class ExitDetails extends Model {

    private String rules;

    private int exit_id,
            difficulty_tracking_exit,
            difficulty_tracking_freefall,
            difficulty_tracking_landing,
            difficulty_tracking_overall,
            difficulty_wingsuit_exit,
            difficulty_wingsuit_freefall,
            difficulty_wingsuit_landing,
            difficulty_wingsuit_overall;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "exit_details";

    public static final String COLUMN_NAME_EXIT_ID = "exit_id";
    public static final String COLUMN_NAME_DIFFICULTY_TRACKING_EXIT = "difficulty_tracking_exit";
    public static final String COLUMN_NAME_DIFFICULTY_TRACKING_FREEFALL = "difficulty_tracking_freefall";
    public static final String COLUMN_NAME_DIFFICULTY_TRACKING_LANDING = "difficulty_tracking_landing";
    public static final String COLUMN_NAME_DIFFICULTY_TRACKING_OVERALL = "difficulty_tracking_overall";
    public static final String COLUMN_NAME_DIFFICULTY_WINGSUIT_EXIT = "difficulty_wingsuit_exit";
    public static final String COLUMN_NAME_DIFFICULTY_WINGSUIT_FREEFALL = "difficulty_wingsuit_freefall";
    public static final String COLUMN_NAME_DIFFICULTY_WINGSUIT_LANDING = "difficulty_wingsuit_landing";
    public static final String COLUMN_NAME_DIFFICULTY_WINGSUIT_OVERALL = "difficulty_wingsuit_overall";
    public static final String COLUMN_NAME_RULES = "rules";

    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<>();

            dbColumns.put(COLUMN_NAME_EXIT_ID, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DIFFICULTY_TRACKING_EXIT, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DIFFICULTY_TRACKING_FREEFALL, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DIFFICULTY_TRACKING_LANDING, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DIFFICULTY_TRACKING_OVERALL, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DIFFICULTY_WINGSUIT_EXIT, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DIFFICULTY_WINGSUIT_FREEFALL, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DIFFICULTY_WINGSUIT_LANDING, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DIFFICULTY_WINGSUIT_OVERALL, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_RULES, TYPE_TEXT);
        }

        return dbColumns;
    }
    /* END DB DEFINITIONS */

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public int getDifficultyTrackingExit() {
        return difficulty_tracking_exit;
    }

    public void setDifficultyTrackingExit(int difficulty_tracking_exit) {
        this.difficulty_tracking_exit = difficulty_tracking_exit;
    }

    public int getDifficultyTrackingFreefall() {
        return difficulty_tracking_freefall;
    }

    public void setDifficultyTrackingFreefall(int difficulty_tracking_freefall) {
        this.difficulty_tracking_freefall = difficulty_tracking_freefall;
    }

    public int getDifficultyTrackingLanding() {
        return difficulty_tracking_landing;
    }

    public void setDifficultyTrackingLanding(int difficulty_tracking_landing) {
        this.difficulty_tracking_landing = difficulty_tracking_landing;
    }

    public int getDifficultyTrackingOverall() {
        return difficulty_tracking_overall;
    }

    public void setDifficultyTrackingOverall(int difficulty_tracking_overall) {
        this.difficulty_tracking_overall = difficulty_tracking_overall;
    }

    public int getDifficultyWingsuitExit() {
        return difficulty_wingsuit_exit;
    }

    public void setDifficultyWingsuitExit(int difficulty_wingsuit_exit) {
        this.difficulty_wingsuit_exit = difficulty_wingsuit_exit;
    }

    public int getDifficultyWingsuitFreefall() {
        return difficulty_wingsuit_freefall;
    }

    public void setDifficultyWingsuitFreefall(int difficulty_wingsuit_freefall) {
        this.difficulty_wingsuit_freefall = difficulty_wingsuit_freefall;
    }

    public int getDifficultyWingsuitLanding() {
        return difficulty_wingsuit_landing;
    }

    public void setDifficultyWingsuitLanding(int difficulty_wingsuit_landing) {
        this.difficulty_wingsuit_landing = difficulty_wingsuit_landing;
    }

    public int getDifficultyWingsuitOverall() {
        return difficulty_wingsuit_overall;
    }

    public void setDifficultyWingsuitOverall(int difficulty_wingsuit_overall) {
        this.difficulty_wingsuit_overall = difficulty_wingsuit_overall;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public int getExitId() {
        return exit_id;
    }

    public void setExitId(int exit_id) {
        this.exit_id = exit_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExitDetails that = (ExitDetails) o;

        if (difficulty_tracking_exit != that.difficulty_tracking_exit) return false;
        if (difficulty_tracking_freefall != that.difficulty_tracking_freefall) return false;
        if (difficulty_tracking_landing != that.difficulty_tracking_landing) return false;
        if (difficulty_tracking_overall != that.difficulty_tracking_overall) return false;
        if (difficulty_wingsuit_exit != that.difficulty_wingsuit_exit) return false;
        if (difficulty_wingsuit_freefall != that.difficulty_wingsuit_freefall) return false;
        if (difficulty_wingsuit_landing != that.difficulty_wingsuit_landing) return false;
        if (difficulty_wingsuit_overall != that.difficulty_wingsuit_overall) return false;
        return rules != null ? rules.equals(that.rules) : that.rules == null;

    }
}