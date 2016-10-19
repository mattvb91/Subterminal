package mavonie.subterminal.Models;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;

/**
 * Created by mavon on 15/10/16.
 */

public class Jump extends Model {

    private String description;
    private String date;
    private int gear_id;
    private int exit_id;
    private int pc_size;
    private int slider;
    private int delay;

    static int SLIDER_DOWN = 0;
    static int SLIDER_UP = 1;

    private static final Integer[] pc_sizes = {32, 36, 38, 40, 42, 46, 48};

    private static final HashMap<Integer, String> slider_config;


    static {
        slider_config = new HashMap<Integer, String>();
        slider_config.put(SLIDER_DOWN, "Down");
        slider_config.put(SLIDER_UP, "Up");
    }

    public String[] getSliderConfigArray() {

        String[] sliderArray = new String[slider_config.size()];

        for (int i = 0; i < slider_config.size(); i++) {
            sliderArray[i] = slider_config.get(i);
        }

        return sliderArray;
    }

    public Integer[] getPcSizeArray() {
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

    //TODO fill in properly
    @Override
    public Jump populateFromCursor(Cursor cursor) {
        try {
            Jump jump = new Jump();

            int idIndex = cursor.getColumnIndexOrThrow(_ID);
            int idExitId = cursor.getColumnIndexOrThrow(COLUMN_NAME_EXIT_ID);
            int idDateId = cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE);

            jump.setId(cursor.getInt(idIndex));
            jump.setExit_id(cursor.getInt(idExitId));
            jump.setDate(cursor.getString(idDateId));

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
    }

    @Override
    String getTableName() {
        return TABLE_NAME;
    }
}
