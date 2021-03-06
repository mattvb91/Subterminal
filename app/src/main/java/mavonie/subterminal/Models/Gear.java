package mavonie.subterminal.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.Synchronized;
import retrofit2.Call;

/**
 * Gear model
 */
public class Gear extends Synchronizable {

    private String container_manufacturer,
            container_type,
            container_serial,
            container_date_in_use,
            canopy_manufacturer,
            canopy_type,
            canopy_serial,
            canopy_date_in_use;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "gear";

    public static final String COLUMN_NAME_CONTAINER_MANUFACTURER = "container_manufacturer";
    public static final String COLUMN_NAME_CONTAINER_TYPE = "container_type";
    public static final String COLUMN_NAME_CONTAINER_SERIAL = "container_serial";
    public static final String COLUMN_NAME_CONTAINER_DATE_IN_USE = "container_date_in_use";

    public static final String COLUMN_NAME_CANOPY_MANUFACTURER = "canopy_manufacturer";
    public static final String COLUMN_NAME_CANOPY_TYPE = "canopy_type";
    public static final String COLUMN_NAME_CANOPY_SERIAL = "canopy_serial";
    public static final String COLUMN_NAME_CANOPY_DATE_IN_USE = "canopy_date_in_use";

    private static Map<String, Integer> dbColumns = null;

    /* END DB DEFINITIONS */

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<>();

            dbColumns.put(COLUMN_NAME_CONTAINER_MANUFACTURER, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_CONTAINER_TYPE, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_CONTAINER_SERIAL, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_CONTAINER_DATE_IN_USE, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_CANOPY_MANUFACTURER, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_CANOPY_TYPE, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_CANOPY_SERIAL, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_CANOPY_DATE_IN_USE, TYPE_TEXT);

            Synchronizable.setDBColumns(dbColumns);
        }

        return dbColumns;
    }

    public Gear() {
    }

    public Gear(int id, String containerManufacturer, String containerSerial) {
        this.container_manufacturer = containerManufacturer;
        this.container_serial = containerSerial;
    }

    public String getContainerManufacturer() {
        return container_manufacturer;
    }

    public void setContainerManufacturer(String containerManufacturer) {
        this.container_manufacturer = containerManufacturer;
    }

    public String getContainerType() {
        return container_type;
    }

    public void setContainerType(String containerType) {
        this.container_type = containerType;
    }

    public String getContainerSerial() {
        return container_serial;
    }

    public void setContainerSerial(String containerSerial) {
        this.container_serial = containerSerial;
    }

    public String getContainerDateInUse() {
        return container_date_in_use;
    }

    public void setContainerDateInUse(String containerDateInUse) {
        this.container_date_in_use = containerDateInUse;
    }

    public String getCanopyManufacturer() {
        return canopy_manufacturer;
    }

    public void setCanopyManufacturer(String canopyManufacturer) {
        this.canopy_manufacturer = canopyManufacturer;
    }

    public String getCanopyType() {
        return canopy_type;
    }

    public void setCanopyType(String canopyType) {
        this.canopy_type = canopyType;
    }

    public String getCanopySerial() {
        return canopy_serial;
    }

    public void setCanopySerial(String canopySerial) {
        this.canopy_serial = canopySerial;
    }

    public String getCanopyDateInUse() {
        return canopy_date_in_use;
    }

    public void setCanopyDateInUse(String canopyDateInUse) {
        this.canopy_date_in_use = canopyDateInUse;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    //TODO add canopy type
    public String getDisplayName() {
        return this.getContainerManufacturer() + " " +
                this.getContainerType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gear gear = (Gear) o;

        if (this.getId() != gear.getId()) return false;
        if (container_manufacturer != null ? !container_manufacturer.equals(gear.container_manufacturer) : gear.container_manufacturer != null)
            return false;
        if (container_type != null ? !container_type.equals(gear.container_type) : gear.container_type != null)
            return false;
        if (container_serial != null ? !container_serial.equals(gear.container_serial) : gear.container_serial != null)
            return false;
        if (container_date_in_use != null ? !container_date_in_use.equals(gear.container_date_in_use) : gear.container_date_in_use != null)
            return false;
        if (canopy_manufacturer != null ? !canopy_manufacturer.equals(gear.canopy_manufacturer) : gear.canopy_manufacturer != null)
            return false;
        if (canopy_type != null ? !canopy_type.equals(gear.canopy_type) : gear.canopy_type != null)
            return false;
        if (canopy_serial != null ? !canopy_serial.equals(gear.canopy_serial) : gear.canopy_serial != null)
            return false;
        return canopy_date_in_use != null ? canopy_date_in_use.equals(gear.canopy_date_in_use) : gear.canopy_date_in_use == null;

    }

    public static List<Gear> getGearForSync() {
        return new Gear().getItems(getSyncRequiredParams());
    }

    public static List<Gear> getGearForDelete() {
        return new Gear().getItems(getDeleteRequiredParams());
    }

    @Override
    public boolean delete() {
        for (Jump jump : this.getJumps()) {
            jump.setGearId(null);
            jump.save();
        }

        return super.delete();

    }


    /**
     * Get all jumps associated with this piece of gear
     *
     * @return List<Jump>
     */
    public List<Jump> getJumps() {
        HashMap<String, Object> whereGearID = new HashMap<>();
        whereGearID.put(Model.FILTER_WHERE_FIELD, Jump.COLUMN_NAME_GEAR_ID);
        whereGearID.put(Model.FILTER_WHERE_VALUE, Integer.toString(this.getId()));

        return new Jump().getItems(whereGearID);
    }

    @Override
    public Call getSyncEndpoint() {
        return Subterminal.getApi().getEndpoints().syncGear(this);
    }

    @Override
    public Call<Void> getDeleteEndpoint() {
        return Subterminal.getApi().getEndpoints().deleteGear(this.getId());
    }

    @Override
    public Call<List<Gear>> getDownloadEndpoint() {
        return Subterminal.getApi().getEndpoints().downloadGear(Synchronized.getLastSyncPref(this.getSyncIdentifier()));
    }

    @Override
    public String getSyncIdentifier() {
        return Synchronized.PREF_LAST_SYNC_GEAR;
    }
}
