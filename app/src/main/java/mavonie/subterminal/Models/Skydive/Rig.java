package mavonie.subterminal.Models.Skydive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Jobs.Skydive.SyncRig;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.Subterminal;


/**
 * Rig Model
 */
public class Rig extends Synchronizable {

    private String container_manufacturer,
            container_model,
            container_serial,
            container_date_in_use,
            main_manufacturer,
            main_model,
            main_serial,
            main_date_in_use,
            reserve_manufacturer,
            reserve_model,
            reserve_serial,
            reserve_date_in_use,
            aad_manufacturer,
            aad_model,
            aad_serial,
            aad_date_in_use;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "skydive_rig";

    public static final String COLUMN_NAME_CONTAINER_MANUFACTURER = "container_manufacturer";
    public static final String COLUMN_NAME_CONTAINER_MODEL = "container_model";
    public static final String COLUMN_NAME_CONTAINER_SERIAL = "container_serial";
    public static final String COLUMN_NAME_CONTAINER_DATE_IN_USE = "container_date_in_use";
    public static final String COLUMN_NAME_MAIN_MANUFACTURER = "main_manufacturer";
    public static final String COLUMN_NAME_MAIN_MODEL = "main_model";
    public static final String COLUMN_NAME_MAIN_SERIAL = "main_serial";
    public static final String COLUMN_NAME_MAIN_DATE_IN_USE = "main_date_in_use";
    public static final String COLUMN_NAME_RESERVE_MANUFACTURER = "reserve_manufacturer";
    public static final String COLUMN_NAME_RESERVE_MODEL = "reserve_model";
    public static final String COLUMN_NAME_RESERVE_SERIAL = "reserve_serial";
    public static final String COLUMN_NAME_RESERVE_DATE_IN_USE = "reserve_date_in_use";
    public static final String COLUMN_NAME_AAD_MANUFACTURER = "aad_manufacturer";
    public static final String COLUMN_NAME_AAD_MODEL = "aad_model";
    public static final String COLUMN_NAME_AAD_SERIAL = "aad_serial";
    public static final String COLUMN_NAME_AAD_DATE_IN_USE = "aad_date_in_use";

    @Override
    public void addSyncJob() {
        Subterminal.getJobManager(MainActivity.getActivity())
                .addJobInBackground(new SyncRig(this));
    }

    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<String, Integer>();

            dbColumns.put(COLUMN_NAME_CONTAINER_MANUFACTURER, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_CONTAINER_MODEL, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_CONTAINER_SERIAL, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_CONTAINER_DATE_IN_USE, TYPE_TEXT);

            dbColumns.put(COLUMN_NAME_MAIN_MANUFACTURER, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_MAIN_MODEL, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_MAIN_SERIAL, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_MAIN_DATE_IN_USE, TYPE_TEXT);

            dbColumns.put(COLUMN_NAME_RESERVE_MANUFACTURER, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_RESERVE_MODEL, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_RESERVE_SERIAL, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_RESERVE_DATE_IN_USE, TYPE_TEXT);

            dbColumns.put(COLUMN_NAME_AAD_MANUFACTURER, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_AAD_MODEL, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_AAD_SERIAL, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_AAD_DATE_IN_USE, TYPE_TEXT);

            Synchronizable.setDBColumns(dbColumns);
        }

        return dbColumns;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public String getContainerManufacturer() {
        return container_manufacturer;
    }

    public void setContainerManufacturer(String container_manufacturer) {
        this.container_manufacturer = container_manufacturer;
    }

    public String getContainerModel() {
        return container_model;
    }

    public void setContainerModel(String container_model) {
        this.container_model = container_model;
    }

    public String getContainerSerial() {
        return container_serial;
    }

    public void setContainerSerial(String container_serial) {
        this.container_serial = container_serial;
    }

    public String getContainerDateInUse() {
        return container_date_in_use;
    }

    public void setContainerDateInUse(String container_date_in_use) {
        this.container_date_in_use = container_date_in_use;
    }

    public String getMainManufacturer() {
        return main_manufacturer;
    }

    public void setMainManufacturer(String main_manufacturer) {
        this.main_manufacturer = main_manufacturer;
    }

    public String getMainModel() {
        return main_model;
    }

    public void setMainModel(String main_model) {
        this.main_model = main_model;
    }

    public String getMainSerial() {
        return main_serial;
    }

    public void setMainSerial(String main_serial) {
        this.main_serial = main_serial;
    }

    public String getMainDateInUse() {
        return main_date_in_use;
    }

    public void setMainDateInUse(String main_date_in_use) {
        this.main_date_in_use = main_date_in_use;
    }

    public String getReserveManufacturer() {
        return reserve_manufacturer;
    }

    public void setReserveManufacturer(String reserve_manufacturer) {
        this.reserve_manufacturer = reserve_manufacturer;
    }

    public String getReserveModel() {
        return reserve_model;
    }

    public void setReserveModel(String reserve_model) {
        this.reserve_model = reserve_model;
    }

    public String getReserveSerial() {
        return reserve_serial;
    }

    public void setReserveSerial(String reserve_serial) {
        this.reserve_serial = reserve_serial;
    }

    public String getReserveDateInUse() {
        return reserve_date_in_use;
    }

    public void setReserveDateInUse(String reserve_date_in_use) {
        this.reserve_date_in_use = reserve_date_in_use;
    }

    public String getAadManufacturer() {
        return aad_manufacturer;
    }

    public void setAadManufacturer(String aad_manufacturer) {
        this.aad_manufacturer = aad_manufacturer;
    }

    public String getAadModel() {
        return aad_model;
    }

    public void setAadModel(String aad_model) {
        this.aad_model = aad_model;
    }

    public String getAadSerial() {
        return aad_serial;
    }

    public void setAadSerial(String aad_serial) {
        this.aad_serial = aad_serial;
    }

    public String getAadDateInUse() {
        return aad_date_in_use;
    }

    public void setAadDateInUse(String aad_date_in_use) {
        this.aad_date_in_use = aad_date_in_use;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rig rig = (Rig) o;

        if (container_manufacturer != null ? !container_manufacturer.equals(rig.container_manufacturer) : rig.container_manufacturer != null)
            return false;
        if (container_model != null ? !container_model.equals(rig.container_model) : rig.container_model != null)
            return false;
        if (container_serial != null ? !container_serial.equals(rig.container_serial) : rig.container_serial != null)
            return false;
        if (container_date_in_use != null ? !container_date_in_use.equals(rig.container_date_in_use) : rig.container_date_in_use != null)
            return false;
        if (main_manufacturer != null ? !main_manufacturer.equals(rig.main_manufacturer) : rig.main_manufacturer != null)
            return false;
        if (main_model != null ? !main_model.equals(rig.main_model) : rig.main_model != null)
            return false;
        if (main_serial != null ? !main_serial.equals(rig.main_serial) : rig.main_serial != null)
            return false;
        if (main_date_in_use != null ? !main_date_in_use.equals(rig.main_date_in_use) : rig.main_date_in_use != null)
            return false;
        if (reserve_manufacturer != null ? !reserve_manufacturer.equals(rig.reserve_manufacturer) : rig.reserve_manufacturer != null)
            return false;
        if (reserve_model != null ? !reserve_model.equals(rig.reserve_model) : rig.reserve_model != null)
            return false;
        if (reserve_serial != null ? !reserve_serial.equals(rig.reserve_serial) : rig.reserve_serial != null)
            return false;
        if (reserve_date_in_use != null ? !reserve_date_in_use.equals(rig.reserve_date_in_use) : rig.reserve_date_in_use != null)
            return false;
        if (aad_manufacturer != null ? !aad_manufacturer.equals(rig.aad_manufacturer) : rig.aad_manufacturer != null)
            return false;
        if (aad_model != null ? !aad_model.equals(rig.aad_model) : rig.aad_model != null)
            return false;
        if (aad_serial != null ? !aad_serial.equals(rig.aad_serial) : rig.aad_serial != null)
            return false;
        return aad_date_in_use != null ? aad_date_in_use.equals(rig.aad_date_in_use) : rig.aad_date_in_use == null;
    }

    /**
     * Return formatted display title for rig
     *
     * @return String
     */
    public String getDisplayName() {
        return container_manufacturer + " - " + main_manufacturer + " " + main_model;
    }

    public static List<Rig> getRigsForSync() {
        return new Rig().getItems(getSyncRequiredParams());
    }

    public static List<Rig> getRigsForDelete() {
        return new Rig().getItems(getDeleteRequiredParams());
    }

    @Override
    public boolean delete() {
        for (Skydive skydive : this.getSkydives()) {
            skydive.setRigId(null);
            skydive.save();
        }

        return super.delete();
    }

    /**
     * Get all jumps associated with this piece of gear
     *
     * @return List
     */
    public List<Skydive> getSkydives() {
        HashMap<String, Object> whereRigId = new HashMap<>();
        whereRigId.put(Model.FILTER_WHERE_FIELD, Skydive.COLUMN_NAME_JUMP_RIG_ID);
        whereRigId.put(Model.FILTER_WHERE_VALUE, Integer.toString(this.getId()));

        return new Skydive().getItems(whereRigId);
    }
}
