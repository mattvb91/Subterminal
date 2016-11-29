package mavonie.subterminal.Models;

import android.content.ContentValues;

import com.pixplicity.easyprefs.library.Prefs;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Jobs.SyncJump;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Preference;
import mavonie.subterminal.Utils.Date.DateFormat;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Jump Model
 */
public class Jump extends Synchronizable {

    private String description;
    private String date;

    private Integer gear_id;
    private Integer exit_id;
    private Integer type;
    private Integer suit_id;
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

    public static final int TYPE_SLICK = 0;
    public static final int TYPE_TRACKING = 1;
    public static final int TYPE_WINGSUIT = 2;

    private static final HashMap<Integer, String> jump_type;

    static {
        slider_config = new HashMap<Integer, String>();
        slider_config.put(SLIDER_OFF, "Off");
        slider_config.put(SLIDER_DOWN, "Down");
        slider_config.put(SLIDER_UP, "Up");

        jump_type = new HashMap<Integer, String>();
        jump_type.put(TYPE_SLICK, "Slick");
        jump_type.put(TYPE_TRACKING, "Tracking");
        jump_type.put(TYPE_WINGSUIT, "Wingsuit");
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
    public static final String COLUMN_NAME_TYPE = "type";
    public static final String COLUMN_NAME_SUIT_ID = "suit_id";


    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<String, Integer>();

            dbColumns.put(COLUMN_NAME_DESCRIPTION, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_DATE, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_EXIT_ID, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_GEAR_ID, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_PC_SIZE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_SLIDER, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DELAY, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_TYPE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_SUIT_ID, TYPE_INTEGER);

            Synchronizable.setDBColumns(dbColumns);
        }

        return dbColumns;
    }

    public static String[] getTypeArray() {
        String[] typeArray = new String[jump_type.size()];

        for (int i = 0; i < jump_type.size(); i++) {
            typeArray[i] = jump_type.get(i);
        }

        return typeArray;
    }


    /* END DB DEFINITIONS */

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Format date correctly
     *
     * @return String
     */
    public String getDate() {

        if (this.date != null) {
            try {
                DateFormat df = new DateFormat();
                this.date = df.format(df.parse(this.date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSuit_id() {
        return suit_id;
    }

    public void setSuit_id(Integer suit_id) {
        this.suit_id = suit_id;
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
        contentValues.put(COLUMN_NAME_TYPE, this.getType());
        contentValues.put(COLUMN_NAME_SUIT_ID, this.getSuit_id());

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
        if (type != jump.type) return false;
        if (suit_id != jump.suit_id) return false;

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

    public String getFormattedType() {
        if (this.getType() != null) {
            return getTypeArray()[this.getType()];
        }

        return null;
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
