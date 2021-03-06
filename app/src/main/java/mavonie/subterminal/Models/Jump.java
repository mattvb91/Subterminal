package mavonie.subterminal.Models;

import android.database.Cursor;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Utils.Date.DateFormat;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.Synchronized;
import retrofit2.Call;

/**
 * Jump Model
 */
public class Jump extends Synchronizable {

    private String description,
            date;

    private Integer gear_id,
            exit_id,
            type,
            suit_id,
            pc_size,
            slider,
            pc_config;

    private Integer delay = 0;

    //use this to get the current position from the query
    private int row_id;

    public static final int SLIDER_DOWN = 0;
    public static final int SLIDER_UP = 1;
    public static final int SLIDER_OFF = 2;

    private static final Integer[] pc_sizes = {32, 36, 38, 40, 42, 46, 48};

    public static final HashMap<Integer, String> slider_config;

    public static final HashMap<Integer, String> pc_configs;

    public static final int PC_STOWED = 0;
    public static final int PC_HANDHELD = 1;
    public static final int PC_PCA = 2;
    public static final int PC_STATIC_LINE = 3;

    public static final int TYPE_SLICK = 0;
    public static final int TYPE_TRACKING = 1;
    public static final int TYPE_WINGSUIT = 2;

    private static final HashMap<Integer, String> jump_type;

    static {
        slider_config = new HashMap<>();
        slider_config.put(SLIDER_OFF, "Off");
        slider_config.put(SLIDER_DOWN, "Down");
        slider_config.put(SLIDER_UP, "Up");

        jump_type = new HashMap<>();
        jump_type.put(TYPE_SLICK, "Slick");
        jump_type.put(TYPE_TRACKING, "Tracking");
        jump_type.put(TYPE_WINGSUIT, "Wingsuit");

        pc_configs = new HashMap<>();
        pc_configs.put(PC_STOWED, "Stowed");
        pc_configs.put(PC_HANDHELD, "Handheld");
        pc_configs.put(PC_PCA, "PCA");
        pc_configs.put(PC_STATIC_LINE, "Static Line");
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

    public static HashMap<Integer, String> getTypes() {
        return jump_type;
    }

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "jump";

    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_EXIT_ID = "exit_id";
    public static final String COLUMN_NAME_GEAR_ID = "gear_id";
    public static final String COLUMN_NAME_PC_SIZE = "pc_size";
    public static final String COLUMN_NAME_PC_CONFIG = "pc_config";
    public static final String COLUMN_NAME_SLIDER = "slider";
    public static final String COLUMN_NAME_DELAY = "delay";
    public static final String COLUMN_NAME_TYPE = "type";
    public static final String COLUMN_NAME_SUIT_ID = "suit_id";
    /* END DB DEFINITIONS */


    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<>();

            dbColumns.put(COLUMN_NAME_DESCRIPTION, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_DATE, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_EXIT_ID, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_GEAR_ID, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_PC_SIZE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_PC_CONFIG, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_SLIDER, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DELAY, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_TYPE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_SUIT_ID, TYPE_INTEGER);
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

    public static String[] getTypeArray() {
        String[] typeArray = new String[jump_type.size()];

        for (int i = 0; i < jump_type.size(); i++) {
            typeArray[i] = jump_type.get(i);
        }

        return typeArray;
    }

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
        if (this._gear == null && this.getGearId() != null) {
            this._gear = (Gear) new Gear().getOneById(this.getGearId());
        }

        return this._gear;
    }

    public Integer getGearId() {
        return gear_id;
    }

    public void setGearId(Integer gear_id) {
        this.gear_id = gear_id;
    }

    private Exit _exit;

    public Exit getExit() {
        if (this._exit == null && this.getExitId() != null) {
            this._exit = (Exit) new Exit().getOneById(this.getExitId());
        }

        return this._exit;
    }

    private Suit _suit;

    /**
     * @return Suit|Null
     */
    public Suit getSuit() {
        if (this._suit == null && this.getSuitId() != null) {
            this._suit = (Suit) new Suit().getOneById(this.getSuitId());
        }

        return this._suit;
    }

    public Integer getExitId() {
        return exit_id;
    }

    public void setExitId(Integer exit_id) {
        this.exit_id = exit_id;
    }

    public int getPcSize() {
        return pc_size;
    }

    public void setPcSize(int pc_size) {
        this.pc_size = pc_size;
    }

    public int getSlider() {
        return slider;
    }

    public void setSlider(int slider) {
        this.slider = slider;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSuitId() {
        return suit_id;
    }

    public void setSuitId(Integer suit_id) {
        this.suit_id = suit_id;
    }

    public Integer getPcConfig() {
        return pc_config;
    }

    public void setPcConfig(Integer pc_config) {
        this.pc_config = pc_config;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    /**
     * @return String
     */
    public String getFormattedSlider() {
        return getSliderConfigArray()[this.getSlider()];
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
        if (pc_config != jump.pc_config) return false;
        if (slider != jump.slider) return false;
        if (delay != jump.delay) return false;
        if (description != null ? !description.equals(jump.description) : jump.description != null)
            return false;
        if (!date.equals(jump.date)) return false;
        if (_gear != null ? !_gear.equals(jump._gear) : jump._gear != null) return false;
        if (type != jump.type) return false;
        return suit_id == jump.suit_id;

    }

    /**
     * @return int
     */
    public int getRowId() {
        int startJump = Subterminal.getUser().getSettings().getBaseStartJumpNo();

        int res = this.row_id;

        if (startJump > 0) {
            res += (startJump - 1);
        }

        return res;
    }

    public void setRowId(int row_id) {
        this.row_id = row_id;
    }

    public String getFormattedDelay() {
        if (this.getDelay() == null) {
            return "";
        }

        return this.getDelay() + "s";
    }

    public String getFormattedType() {
        if (this.getType() != null) {
            return getTypeArray()[this.getType()];
        }

        return null;
    }

    public static List<Jump> getJumpsForSync() {
        return new Jump().getItems(getSyncRequiredParams());
    }

    public static List<Jump> getJumpsForDelete() {
        return new Jump().getItems(getDeleteRequiredParams());
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

    @Override
    public Call getSyncEndpoint() {
        return Subterminal.getApi().getEndpoints().syncJump(this);
    }

    @Override
    public Call<Void> getDeleteEndpoint() {
        return Subterminal.getApi().getEndpoints().deleteJump(this.getId());
    }

    @Override
    public Call<List<Jump>> getDownloadEndpoint() {
        return Subterminal.getApi().getEndpoints().downloadJumps(Synchronized.getLastSyncPref(this.getSyncIdentifier()));
    }

    @Override
    public String getSyncIdentifier() {
        return Synchronized.PREF_LAST_SYNC_JUMP;
    }

    /**
     * @return String|Null
     */
    public String getFormattedPcConfig() {
        if (this.getPcConfig() != null) {
            return pc_configs.get(this.getPcConfig());
        }

        return null;
    }
}
