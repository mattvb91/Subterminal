package mavonie.subterminal.models;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mavon on 15/10/16.
 */

public class Jump extends Model {

    private String description;
    private Date date;
    private int gear_id;
    private int exit_id;
    private int pc_size;
    private int slider;
    private int delay;

    static int SLIDER_DOWN = 0;
    static int SLIDER_UP = 1;

    private static final Map<Integer, String> slider_config;

    static {
        slider_config = new HashMap<Integer, String>();
        slider_config.put(SLIDER_DOWN, "Down");
        slider_config.put(SLIDER_UP, "Up");
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Gear _gear;

    public Gear getGear() {
        if (this._gear == null) {
            this._gear = (Gear) new Gear().getOneById(this.getGear_id());
        }

        return this._gear;
    }

    public int getGear_id() {
        return gear_id;
    }

    public void setGear_id(int gear_id) {
        this.gear_id = gear_id;
    }

    private Exit _exit;

    public Exit getExit() {
        if (this._exit == null) {
            this._exit = (Exit) new Exit().getOneById(this.getExit_id());
        }

        return this._exit;
    }

    public int getExit_id() {
        return exit_id;
    }

    public void setExit_id(int exit_id) {
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
    Model populateFromCursor(Cursor cursor) {
        return null;
    }

    @Override
    void populateContentValues(ContentValues contentValues) {

    }

    @Override
    String getTableName() {
        return TABLE_NAME;
    }
}
