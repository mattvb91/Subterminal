package mavonie.subterminal.Models;

import android.content.ContentValues;
import android.database.Cursor;

public class ExitDetails extends Model {

    private int exit_id;
    private String rules;
    private int difficulty_tracking_exit;
    private int difficulty_tracking_freefall;
    private int difficulty_tracking_landing;
    private int difficulty_tracking_overall;
    private int difficulty_wingsuit_exit;
    private int difficulty_wingsuit_freefall;
    private int difficulty_wingsuit_landing;
    private int difficulty_wingsuit_overall;

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
    /* END DB DEFINITIONS */


    /**
     * @return The rules
     */
    public String getRules() {
        return rules;
    }

    /**
     * @param rules The rules
     */
    public void setRules(String rules) {
        this.rules = rules;
    }

    /**
     * @return The difficulty_tracking_exit
     */
    public int getDifficulty_tracking_exit() {
        return difficulty_tracking_exit;
    }

    /**
     * @param difficulty_tracking_exit The difficulty_tracking_exit
     */
    public void setDifficulty_tracking_exit(int difficulty_tracking_exit) {
        this.difficulty_tracking_exit = difficulty_tracking_exit;
    }

    /**
     * @return The difficulty_tracking_freefall
     */
    public int getDifficulty_tracking_freefall() {
        return difficulty_tracking_freefall;
    }

    /**
     * @param difficulty_tracking_freefall The difficulty_tracking_freefall
     */
    public void setDifficulty_tracking_freefall(int difficulty_tracking_freefall) {
        this.difficulty_tracking_freefall = difficulty_tracking_freefall;
    }

    /**
     * @return The difficulty_tracking_landing
     */
    public int getDifficulty_tracking_landing() {
        return difficulty_tracking_landing;
    }

    /**
     * @param difficulty_tracking_landing The difficulty_tracking_landing
     */
    public void setDifficulty_tracking_landing(int difficulty_tracking_landing) {
        this.difficulty_tracking_landing = difficulty_tracking_landing;
    }

    /**
     * @return The difficulty_tracking_overall
     */
    public int getDifficulty_tracking_overall() {
        return difficulty_tracking_overall;
    }

    /**
     * @param difficulty_tracking_overall The difficulty_tracking_overall
     */
    public void setDifficulty_tracking_overall(int difficulty_tracking_overall) {
        this.difficulty_tracking_overall = difficulty_tracking_overall;
    }

    /**
     * @return The difficulty_wingsuit_exit
     */
    public int getDifficulty_wingsuit_exit() {
        return difficulty_wingsuit_exit;
    }

    /**
     * @param difficulty_wingsuit_exit The difficulty_wingsuit_exit
     */
    public void setDifficulty_wingsuit_exit(int difficulty_wingsuit_exit) {
        this.difficulty_wingsuit_exit = difficulty_wingsuit_exit;
    }

    /**
     * @return The difficulty_wingsuit_freefall
     */
    public int getDifficulty_wingsuit_freefall() {
        return difficulty_wingsuit_freefall;
    }

    /**
     * @param difficulty_wingsuit_freefall The difficulty_wingsuit_freefall
     */
    public void setDifficulty_wingsuit_freefall(int difficulty_wingsuit_freefall) {
        this.difficulty_wingsuit_freefall = difficulty_wingsuit_freefall;
    }

    /**
     * @return The difficulty_wingsuit_landing
     */
    public int getDifficulty_wingsuit_landing() {
        return difficulty_wingsuit_landing;
    }

    /**
     * @param difficulty_wingsuit_landing The difficulty_wingsuit_landing
     */
    public void setDifficulty_wingsuit_landing(int difficulty_wingsuit_landing) {
        this.difficulty_wingsuit_landing = difficulty_wingsuit_landing;
    }

    /**
     * @return The difficulty_wingsuit_overall
     */
    public int getDifficulty_wingsuit_overall() {
        return difficulty_wingsuit_overall;
    }

    /**
     * @param difficulty_wingsuit_overall The difficulty_wingsuit_overall
     */
    public void setDifficulty_wingsuit_overall(int difficulty_wingsuit_overall) {
        this.difficulty_wingsuit_overall = difficulty_wingsuit_overall;
    }

    @Override
    Model populateFromCursor(Cursor cursor) {
        try {
            ExitDetails details = new ExitDetails();

            int exitId = cursor.getColumnIndexOrThrow(COLUMN_NAME_EXIT_ID);
            int difficultyTrackingExit = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_TRACKING_EXIT);
            int difficultyTrackingfreefall = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_TRACKING_FREEFALL);
            int difficultyTrackinglanding = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_TRACKING_LANDING);
            int difficultyTrackingOverall = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_TRACKING_OVERALL);
            int difficultyWingsuitExit = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_WINGSUIT_EXIT);
            int difficultyWingsuitfreefall = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_WINGSUIT_FREEFALL);
            int difficultyWingsuitlanding = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_WINGSUIT_LANDING);
            int difficultyWingsuitOverall = cursor.getColumnIndexOrThrow(COLUMN_NAME_DIFFICULTY_WINGSUIT_OVERALL);
            int rules = cursor.getColumnIndexOrThrow(COLUMN_NAME_RULES);

            details.setExit_id(cursor.getInt(exitId));
            details.setDifficulty_tracking_exit(cursor.getInt(difficultyTrackingExit));
            details.setDifficulty_tracking_freefall(cursor.getInt(difficultyTrackingfreefall));
            details.setDifficulty_tracking_landing(cursor.getInt(difficultyTrackinglanding));
            details.setDifficulty_tracking_overall(cursor.getInt(difficultyTrackingOverall));
            details.setDifficulty_wingsuit_exit(cursor.getInt(difficultyWingsuitExit));
            details.setDifficulty_wingsuit_freefall(cursor.getInt(difficultyWingsuitfreefall));
            details.setDifficulty_wingsuit_landing(cursor.getInt(difficultyWingsuitlanding));
            details.setDifficulty_wingsuit_overall(cursor.getInt(difficultyWingsuitOverall));
            details.setRules(cursor.getString(rules));

            return details;
        } catch (Exception e) {

        }

        return null;
    }

    @Override
    void populateContentValues(ContentValues contentValues) {
        contentValues.put(COLUMN_NAME_DIFFICULTY_TRACKING_EXIT, this.getDifficulty_tracking_exit());
        contentValues.put(COLUMN_NAME_DIFFICULTY_TRACKING_FREEFALL, this.getDifficulty_tracking_freefall());
        contentValues.put(COLUMN_NAME_DIFFICULTY_TRACKING_LANDING, this.getDifficulty_tracking_landing());
        contentValues.put(COLUMN_NAME_DIFFICULTY_TRACKING_OVERALL, this.getDifficulty_tracking_overall());
        contentValues.put(COLUMN_NAME_DIFFICULTY_WINGSUIT_EXIT, this.getDifficulty_wingsuit_exit());
        contentValues.put(COLUMN_NAME_DIFFICULTY_WINGSUIT_FREEFALL, this.getDifficulty_wingsuit_freefall());
        contentValues.put(COLUMN_NAME_DIFFICULTY_WINGSUIT_LANDING, this.getDifficulty_wingsuit_landing());
        contentValues.put(COLUMN_NAME_DIFFICULTY_WINGSUIT_OVERALL, this.getDifficulty_wingsuit_overall());
        contentValues.put(COLUMN_NAME_RULES, this.getRules());
        contentValues.put(COLUMN_NAME_EXIT_ID, this.getExit_id());
    }

    @Override
    String getTableName() {
        return TABLE_NAME;
    }

    public int getExit_id() {
        return exit_id;
    }

    public void setExit_id(int exit_id) {
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