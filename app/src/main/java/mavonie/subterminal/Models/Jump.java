package mavonie.subterminal.Models;

import android.content.ContentValues;
import android.database.Cursor;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.HashMap;
import java.util.List;

import mavonie.subterminal.Jobs.SyncJump;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Preference;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Jump Model
 */
public class Jump extends Synchronizable {

    private String description;
    private String date;

    private Integer gear_id;
    private Integer exit_id;
    private int pc_size;
    private int slider;
    private int delay;

    //use this to get the current position from the query
    private int row_id;

    public static final int SLIDER_DOWN = 0;
    public static final int SLIDER_UP = 1;
    public static final int SLIDER_OFF = 2;

    private static final Integer[] pc_sizes = {32, 36, 38, 40, 42, 46, 48};

    private static final HashMap<Integer, String> slider_config;


    static {
        slider_config = new HashMap<Integer, String>();
        slider_config.put(SLIDER_OFF, "Off");
        slider_config.put(SLIDER_DOWN, "Down");
        slider_config.put(SLIDER_UP, "Up");
    }

    public static String[] getSliderConfigArray() {

        String[] sliderArray = new String[slider_config.size()];

        for (int i = 0; i < slider_config.size(); i++) {
            sliderArray[i] = slider_config.get(i);
        }

        return sliderArray;
    }

    public static Integer[] getPcSizeArray() {
        return pc_sizes;
    }

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "jump";

    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_EXIT_ID = "exit_id";
    public static final String COLUMN_NAME_GEAR_ID = "gear_id";
    public static final String COLUMN_NAME_PC_SIZE = "pc_size";
    public static final String COLUMN_NAME_SLIDER = "slider";
    public static final String COLUMN_NAME_DELAY = "delay";

    /* END DB DEFINITIONS */

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

    private Gear _gear;

    public Gear getGear() {
        if (this._gear == null && this.getGear_id() != null) {
            this._gear = (Gear) new Gear().getOneById(this.getGear_id());
        }

        return this._gear;
    }

    public Integer getGear_id() {
        return gear_id;
    }

    public void setGear_id(Integer gear_id) {
        this.gear_id = gear_id;
    }

    private Exit _exit;

    public Exit getExit() {
        if (this._exit == null && this.getExit_id() != null) {
            this._exit = (Exit) new Exit().getOneById(this.getExit_id());
        }

        return this._exit;
    }

    public Integer getExit_id() {
        return exit_id;
    }

    public void setExit_id(Integer exit_id) {
        this.exit_id = exit_id;
    }

    public int getPc_size() {
        return pc_size;
    }

    public void setPc_size(int pc_size) {
        this.pc_size = pc_size;
    }

    public int getSlider() {
        return slider;
    }

    public void setSlider(int slider) {
        this.slider = slider;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public Jump populateFromCursor(Cursor cursor) {
        try {
            Jump jump = new Jump();

            int idIndex = cursor.getColumnIndexOrThrow(_ID);
            int idExitId = cursor.getColumnIndexOrThrow(COLUMN_NAME_EXIT_ID);
            int idDateId = cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE);
            int idPcSize = cursor.getColumnIndexOrThrow(COLUMN_NAME_PC_SIZE);
            int idDescription = cursor.getColumnIndexOrThrow(COLUMN_NAME_DESCRIPTION);
            int idDelay = cursor.getColumnIndexOrThrow(COLUMN_NAME_DELAY);
            int idGear = cursor.getColumnIndexOrThrow(COLUMN_NAME_GEAR_ID);
            int idSlider = cursor.getColumnIndexOrThrow(COLUMN_NAME_SLIDER);

            jump.setId(cursor.getInt(idIndex));

            Integer exitId = cursor.getInt(idExitId);
            if (exitId != null && exitId != 0) {
                jump.setExit_id(cursor.getInt(idExitId));
            }

            jump.setDate(cursor.getString(idDateId));
            jump.setPc_size(cursor.getInt(idPcSize));
            jump.setDescription(cursor.getString(idDescription));
            jump.setDelay(cursor.getInt(idDelay));

            if (!cursor.isNull(idGear) & !cursor.isNull(cursor.getInt(idGear))) {
                jump.setGear_id(cursor.getInt(idGear));
            }

            jump.setSlider(cursor.getInt(idSlider));

            jump.setRow_id(cursor.getCount() - cursor.getPosition());

            jump.populateSynchronizationFromCursor(cursor);

            return jump;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    void populateContentValues(ContentValues contentValues) {
        contentValues.put(COLUMN_NAME_EXIT_ID, this.getExit_id());
        contentValues.put(COLUMN_NAME_DATE, this.getDate());
        contentValues.put(COLUMN_NAME_DESCRIPTION, this.getDescription());
        contentValues.put(COLUMN_NAME_DELAY, this.getDelay());
        contentValues.put(COLUMN_NAME_PC_SIZE, this.getPc_size());
        contentValues.put(COLUMN_NAME_GEAR_ID, this.getGear_id());
        contentValues.put(COLUMN_NAME_SLIDER, this.getSlider());

        this.populateSynchronizationContentValues(contentValues);
    }

    @Override
    public boolean save() {
        if (this.getExit() != null && this.getExit().getGlobal_id() != null) {

            //This was a public exit, make it a users exit
            this.getExit().setGlobal_id(null);
            this.getExit().save();
        }

        return super.save();
    }

    @Override
    String getTableName() {
        return TABLE_NAME;
    }

    /**
     * @return String
     */
    public String getFormattedSlider() {
        return this.getSliderConfigArray()[this.getSlider()];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jump jump = (Jump) o;

        if (this.getId() != jump.getId()) return false;
        if (gear_id != jump.gear_id) return false;
        if (exit_id != jump.exit_id) return false;
        if (pc_size != jump.pc_size) return false;
        if (slider != jump.slider) return false;
        if (delay != jump.delay) return false;
        if (description != null ? !description.equals(jump.description) : jump.description != null)
            return false;
        if (!date.equals(jump.date)) return false;
        if (_gear != null ? !_gear.equals(jump._gear) : jump._gear != null) return false;

        return true;
    }

    public int getRow_id() {

        int startJump = Prefs.getInt(Preference.PREFS_JUMP_START_COUNT, 0);

        if (startJump > 0) {
            row_id += (startJump - 1);
        }

        return row_id;
    }

    public void setRow_id(int row_id) {
        this.row_id = row_id;
    }

    public String getFormattedDelay() {
        return this.getDelay() + "s";
    }

    @Override
    public void addSyncJob() {
        Subterminal.getJobManager(MainActivity.getActivity())
                .addJobInBackground(new SyncJump(this));
    }

    public static List<Jump> getJumpsForSync() {

        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> whereSyncRequired = new HashMap<>();
        whereSyncRequired.put(Model.FILTER_WHERE_FIELD, COLUMN_SYNCED);
        whereSyncRequired.put(Model.FILTER_WHERE_VALUE, SYNC_REQUIRED);

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), whereSyncRequired);

        params.put(Model.FILTER_WHERE, wheres);

        return new Jump().getItems(params);
    }

    //TODO these 2 methods should be merged
    public static List<Jump> getJumpsForDelete() {
        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> whereDeleteRequired = new HashMap<>();
        whereDeleteRequired.put(Model.FILTER_WHERE_FIELD, COLUMN_DELETED);
        whereDeleteRequired.put(Model.FILTER_WHERE_VALUE, DELETED_TRUE);

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), whereDeleteRequired);

        params.put(Model.FILTER_WHERE, wheres);

        return new Jump().getItems(params);
    }

}
