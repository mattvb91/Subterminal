package mavonie.subterminal.Models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import mavonie.subterminal.Jobs.SyncJump;
import mavonie.subterminal.Jobs.SyncSuit;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Suit model
 */
public class Suit extends Synchronizable {

    private String manufacturer;
    private int type;
    private String model;
    private String serial;

    @SerializedName("date_in_use")
    private String dateInUse;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "suits";

    public static final String COLUMN_NAME_MANUFACTURER = "manufacturer";
    public static final String COLUMN_NAME_MODEL = "model";
    public static final String COLUMN_NAME_DATE_IN_USE = "date_in_use";
    public static final String COLUMN_NAME_TYPE = "type";
    public static final String COLUMN_NAME_SERIAL = "serial";

    public static final int TYPE_WINGSUIT = 0;
    public static final int TYPE_TRACKING = 1;

    private static final HashMap<Integer, String> suit_type;

    static {
        suit_type = new HashMap<Integer, String>();
        suit_type.put(TYPE_WINGSUIT, "Wingsuit");
        suit_type.put(TYPE_TRACKING, "Tracking");
    }

    public static String[] getSuitTypeArray() {

        String[] sliderArray = new String[suit_type.size()];

        for (int i = 0; i < suit_type.size(); i++) {
            sliderArray[i] = suit_type.get(i);
        }

        return sliderArray;
    }

    public String getFormattedSuitType() {
        return getSuitTypeArray()[this.getType()];
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getDateInUse() {
        return dateInUse;
    }

    public void setDateInUse(String dateInUse) {
        this.dateInUse = dateInUse;
    }

    @Override
    public void addSyncJob() {
        Subterminal.getJobManager(MainActivity.getActivity())
                .addJobInBackground(new SyncSuit(this));
    }

    @Override
    Model populateFromCursor(Cursor cursor) {

        try {
            Suit suit = new Suit();

            int idIndex = cursor.getColumnIndexOrThrow(_ID);
            int idManufacturer = cursor.getColumnIndexOrThrow(COLUMN_NAME_MANUFACTURER);
            int idType = cursor.getColumnIndexOrThrow(COLUMN_NAME_TYPE);
            int idModel = cursor.getColumnIndexOrThrow(COLUMN_NAME_MODEL);
            int idDateInUse = cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE_IN_USE);
            int idSerial = cursor.getColumnIndexOrThrow(COLUMN_NAME_SERIAL);

            suit.setId(cursor.getInt(idIndex));
            suit.setManufacturer(cursor.getString(idManufacturer));
            suit.setType(cursor.getInt(idType));
            suit.setModel(cursor.getString(idModel));
            suit.setDateInUse(cursor.getString(idDateInUse));
            suit.setSerial(cursor.getString(idSerial));

            suit.populateSynchronizationFromCursor(cursor);

            return suit;

        } catch (Exception e) {

        }

        return null;
    }

    @Override
    void populateContentValues(ContentValues contentValues) {
        contentValues.put(COLUMN_NAME_TYPE, this.getType());
        contentValues.put(COLUMN_NAME_MODEL, this.getModel());
        contentValues.put(COLUMN_NAME_MANUFACTURER, this.getManufacturer());
        contentValues.put(COLUMN_NAME_DATE_IN_USE, this.getDateInUse());
        contentValues.put(COLUMN_NAME_SERIAL, this.getSerial());

        super.populateSynchronizationContentValues(contentValues);
    }

    @Override
    String getTableName() {
        return TABLE_NAME;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Suit suit = (Suit) o;

        if (type != suit.type) return false;
        if (manufacturer != null ? !manufacturer.equals(suit.manufacturer) : suit.manufacturer != null)
            return false;
        if (model != null ? !model.equals(suit.model) : suit.model != null) return false;
        if (serial != null ? !serial.equals(suit.serial) : suit.serial != null) return false;
        return dateInUse != null ? dateInUse.equals(suit.dateInUse) : suit.dateInUse == null;

    }

    /**
     * Get items available for a spinner
     *
     * @param type
     * @return
     */
    public LinkedHashMap<String, String> getItemsForSpinner(Integer type) {

        LinkedHashMap<String, String> results = new LinkedHashMap<String, String>();

        List<Model> items = this.getActiveItems();

        for (Model suit : items) {
            if (suit instanceof Suit) {
                if (((Suit) suit).getType() == type) {
                    results.put(Integer.toString(suit.getId()), ((Suit) suit).getManufacturer() + " - " + ((Suit) suit).getModel());
                }
            }
        }

        return results;
    }

    public static List<Suit> getSuitsForSync() {

        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> whereSyncRequired = new HashMap<>();
        whereSyncRequired.put(Model.FILTER_WHERE_FIELD, COLUMN_SYNCED);
        whereSyncRequired.put(Model.FILTER_WHERE_VALUE, SYNC_REQUIRED);

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), whereSyncRequired);

        params.put(Model.FILTER_WHERE, wheres);

        return new Suit().getItems(params);
    }

    //TODO these 2 methods should be merged
    public static List<Suit> getSuitsForDelete() {
        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> whereDeleteRequired = new HashMap<>();
        whereDeleteRequired.put(Model.FILTER_WHERE_FIELD, COLUMN_DELETED);
        whereDeleteRequired.put(Model.FILTER_WHERE_VALUE, DELETED_TRUE);

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), whereDeleteRequired);

        params.put(Model.FILTER_WHERE, wheres);

        return new Suit().getItems(params);
    }

    @Override
    public boolean delete() {

        if (this.getDeleted() == DELETED_FALSE) {
            List<Jump> jumps = this.getJumps();

            for (Jump jump : jumps) {
                jump.setSuit_id(null);
                jump.save();
            }
        }

        return super.delete();
    }


    private List<Jump> _jumps;

    public List<Jump> getJumps() {

        if (this._jumps == null) {
            HashMap<String, Object> params = new HashMap<>();

            HashMap<String, Object> whereSuitID = new HashMap<>();
            whereSuitID.put(Model.FILTER_WHERE_FIELD, Jump.COLUMN_NAME_SUIT_ID);
            whereSuitID.put(Model.FILTER_WHERE_VALUE, Integer.toString(this.getId()));

            HashMap<Integer, HashMap> wheres = new HashMap<>();
            wheres.put(wheres.size(), whereSuitID);

            params.put(Model.FILTER_WHERE, wheres);

            this._jumps = new Jump().getItems(params);
        }

        return this._jumps;
    }
}
