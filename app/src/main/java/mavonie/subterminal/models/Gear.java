package mavonie.subterminal.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mavon on 11/10/16.
 */

public class Gear implements Serializable {
    private int id;
    private String containerManufacturer;
    private String containerType;
    private String containerSerial;
    private Date containerDateInUse;

    private String canopyManufacturer;
    private String canopyType;
    private String canopySerial;
    private Date canopyDateInUse;

    public Gear(int id, String s1, String s2) {
        this.id = id;
        containerManufacturer = s1;
        containerSerial = s2;
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

    public Date getContainerDateInUse() {
        return containerDateInUse;
    }

    public void setContainerDateInUse(Date containerDateInUse) {
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

    public Date getCanopyDateInUse() {
        return canopyDateInUse;
    }

    public void setCanopyDateInUse(Date canopyDateInUse) {
        this.canopyDateInUse = canopyDateInUse;
    }

    public int getId() {
        return id;
    }
}
