package mavonie.subterminal.unit;


import org.junit.Test;

import mavonie.subterminal.Models.Gear;
import mavonie.subterminal.unit.Base.BaseDBUnit;

import static junit.framework.Assert.*;

public class GearTest extends BaseDBUnit {

    /**
     * Validate what we save into the database is what
     * we get out of it
     */
    @Test
    public void saveToDb() {

        Gear gear = createGear();
        assertTrue(gear.exists());
        Gear dbGear = (Gear) new Gear().getOneById(gear.getId());

        assertTrue(gear.equals(dbGear));
    }

    /**
     * Make sure item gets removed from db
     */
    @Test
    public void deleteFromDB() {
        Gear gear = createGear();

        assertTrue(gear.delete());
        assertNull(gear.getOneById(gear.getId()));
    }

    private Gear createGear() {
        Gear gear = new Gear();

        gear.setContainerManufacturer("Adrenaline");
        gear.setContainerType("LD3");
        gear.setContainerSerial("Container_Serial");
        gear.setContainerDateInUse("2010-06-05");
        gear.setCanopyDateInUse("2010-06-05");
        gear.setCanopyManufacturer("Atair");
        gear.setCanopySerial("atair_serial");
        gear.setCanopyType("Troll 285");
        gear.save();

        return gear;
    }
}