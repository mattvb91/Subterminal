package mavonie.subterminal.Models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.Synchronized;
import retrofit2.Call;

/**
 * Suit model
 */
public class Suit extends Synchronizable {

    private String manufacturer,
            model,
            serial,
            date_in_use;

    private int type;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "suits";

    public static final String COLUMN_NAME_MANUFACTURER = "manufacturer";
    public static final String COLUMN_NAME_MODEL = "model";
    public static final String COLUMN_NAME_DATE_IN_USE = "date_in_use";
    public static final String COLUMN_NAME_TYPE = "type";
    public static final String COLUMN_NAME_SERIAL = "serial";

    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<String, Integer>();

            dbColumns.put(COLUMN_NAME_MANUFACTURER, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_MODEL, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_DATE_IN_USE, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_TYPE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_SERIAL, TYPE_TEXT);

            Synchronizable.setDBColumns(dbColumns);
        }

        return dbColumns;
    }

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
        return date_in_use;
    }

    public void setDateInUse(String dateInUse) {
        this.date_in_use = dateInUse;
    }

    @Override
    protected String getTableName() {
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
        return date_in_use != null ? date_in_use.equals(suit.date_in_use) : suit.date_in_use == null;

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
                if (type != null && ((Suit) suit).getType() == type) {
                    results.put(Integer.toString(suit.getId()), ((Suit) suit).getManufacturer() + " - " + ((Suit) suit).getModel());
                }
            }
        }

        return results;
    }

    public static List<Suit> getSuitsForSync() {
        return new Suit().getItems(getSyncRequiredParams());
    }

    public static List<Suit> getSuitsForDelete() {
        return new Suit().getItems(getDeleteRequiredParams());
    }

    @Override
    public boolean delete() {
        if (this.getDeleted() == DELETED_FALSE) {
            for (Jump jump : this.getJumps()) {
                jump.setSuitId(null);
                jump.save();
            }

            for (Skydive skydive : this.getSkydives()) {
                skydive.setSuitId(null);
                skydive.save();
            }
        }

        return super.delete();
    }


    private List<Jump> _jumps;

    /**
     * All the jumps associated with this suit
     *
     * @return List<Jump>
     */
    public List<Jump> getJumps() {

        if (this._jumps == null) {
            this._jumps = new Jump().getItems(new Query(Jump.COLUMN_NAME_SUIT_ID, this.getId()).getParams());
        }

        return this._jumps;
    }

    private List<Skydive> _skydives;

    /**
     * All the skydives associated with this suit
     *
     * @return List<Skydive>
     */
    public List<Skydive> getSkydives() {
        if (this._skydives == null) {
            this._skydives = new Skydive().getItems(new Query(Skydive.COLUMN_NAME_SUIT_ID, this.getId()).getParams());
        }

        return this._skydives;
    }

    @Override
    public Call getSyncEndpoint() {
        return Subterminal.getApi().getEndpoints().syncSuit(this);
    }

    @Override
    public Call<Void> getDeleteEndpoint() {
        return Subterminal.getApi().getEndpoints().deleteSuit(this.getId());
    }

    @Override
    public Call<List<Suit>> getDownloadEndpoint() {
        return Subterminal.getApi().getEndpoints().downloadSuits(Synchronized.getLastSyncPref(this.getSyncIdentifier()));
    }

    @Override
    public String getSyncIdentifier() {
        return Synchronized.PREF_LAST_SYNC_SUIT;
    }

    /**
     * Get the number of jumps associated with this suit.
     *
     * @return int
     */
    public int getJumpCount() {
        int base = new Jump().count(new Query(Jump.COLUMN_NAME_SUIT_ID, this.getId()).getParams());
        int skydive = new Skydive().count(new Query(Skydive.COLUMN_NAME_SUIT_ID, this.getId()).getParams());

        return base + skydive;
    }
}
