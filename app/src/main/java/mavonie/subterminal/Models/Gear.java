package mavonie.subterminal.Models;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by mavon on 11/10/16.
 */

public class Gear extends Model {

    private String containerManufacturer;
    private String containerType;
    private String containerSerial;
    private String containerDateInUse;

    private String canopyManufacturer;
    private String canopyType;
    private String canopySerial;
    private String canopyDateInUse;

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
    /* END DB DEFINITIONS */

    public Gear() {
    }

    public Gear(int id, String containerManufacturer, String containerSerial) {
        this.containerManufacturer = containerManufacturer;
        this.containerSerial = containerSerial;
    }

    public String getContainerManufacturer() {
        return containerManufacturer;
    }

    public void setContainerManufacturer(String containerManufacturer) {
        this.containerManufacturer = containerManufacturer;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getContainerSerial() {
        return containerSerial;
    }

    public void setContainerSerial(String containerSerial) {
        this.containerSerial = containerSerial;
    }

    public String getContainerDateInUse() {
        return containerDateInUse;
    }

    public void setContainerDateInUse(String containerDateInUse) {
        this.containerDateInUse = containerDateInUse;
    }

    public String getCanopyManufacturer() {
        return canopyManufacturer;
    }

    public void setCanopyManufacturer(String canopyManufacturer) {
        this.canopyManufacturer = canopyManufacturer;
    }

    public String getCanopyType() {
        return canopyType;
    }

    public void setCanopyType(String canopyType) {
        this.canopyType = canopyType;
    }

    public String getCanopySerial() {
        return canopySerial;
    }

    public void setCanopySerial(String canopySerial) {
        this.canopySerial = canopySerial;
    }

    public String getCanopyDateInUse() {
        return canopyDateInUse;
    }

    public void setCanopyDateInUse(String canopyDateInUse) {
        this.canopyDateInUse = canopyDateInUse;
    }

    //TODO abstract down to model to handle this in one go for every future model
    public Gear populateFromCursor(Cursor cursor) {
        try {
            Gear gear = new Gear();

            int idIndex = cursor.getColumnIndexOrThrow(_ID);
            int containerManufacturerIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_CONTAINER_MANUFACTURER);
            int containerTypeIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_CONTAINER_TYPE);
            int containerSerialIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_CONTAINER_SERIAL);
            int containerDateIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_CONTAINER_DATE_IN_USE);

            int canopyManufacturerIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_CANOPY_MANUFACTURER);
            int canopyTypeIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_CANOPY_TYPE);
            int canopySerialIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_CANOPY_SERIAL);
            int canopyDateIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_CANOPY_DATE_IN_USE);

            gear.setId(cursor.getInt(idIndex));
            gear.setContainerManufacturer(cursor.getString(containerManufacturerIndex));
            gear.setContainerType(cursor.getString(containerTypeIndex));
            gear.setContainerSerial(cursor.getString(containerSerialIndex));
            gear.setContainerDateInUse(cursor.getString(containerDateIndex));

            gear.setCanopyManufacturer(cursor.getString(canopyManufacturerIndex));
            gear.setCanopyType(cursor.getString(canopyTypeIndex));
            gear.setCanopySerial(cursor.getString(canopySerialIndex));
            gear.setCanopyDateInUse(cursor.getString(canopyDateIndex));

            return gear;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public void populateContentValues(ContentValues contentValues) {
        contentValues.put(COLUMN_NAME_CONTAINER_MANUFACTURER, this.getContainerManufacturer());
        contentValues.put(COLUMN_NAME_CONTAINER_TYPE, this.getContainerType());
        contentValues.put(COLUMN_NAME_CONTAINER_SERIAL, this.getContainerSerial());
        contentValues.put(COLUMN_NAME_CONTAINER_DATE_IN_USE, this.getContainerDateInUse().toString());
        contentValues.put(COLUMN_NAME_CANOPY_MANUFACTURER, this.getCanopyManufacturer());
        contentValues.put(COLUMN_NAME_CANOPY_TYPE, this.getCanopyType());
        contentValues.put(COLUMN_NAME_CANOPY_SERIAL, this.getCanopySerial());
        contentValues.put(COLUMN_NAME_CANOPY_DATE_IN_USE, this.getCanopyDateInUse());

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
        if (containerManufacturer != null ? !containerManufacturer.equals(gear.containerManufacturer) : gear.containerManufacturer != null)
            return false;
        if (containerType != null ? !containerType.equals(gear.containerType) : gear.containerType != null)
            return false;
        if (containerSerial != null ? !containerSerial.equals(gear.containerSerial) : gear.containerSerial != null)
            return false;
        if (containerDateInUse != null ? !containerDateInUse.equals(gear.containerDateInUse) : gear.containerDateInUse != null)
            return false;
        if (canopyManufacturer != null ? !canopyManufacturer.equals(gear.canopyManufacturer) : gear.canopyManufacturer != null)
            return false;
        if (canopyType != null ? !canopyType.equals(gear.canopyType) : gear.canopyType != null)
            return false;
        if (canopySerial != null ? !canopySerial.equals(gear.canopySerial) : gear.canopySerial != null)
            return false;
        return canopyDateInUse != null ? canopyDateInUse.equals(gear.canopyDateInUse) : gear.canopyDateInUse == null;

    }
}
