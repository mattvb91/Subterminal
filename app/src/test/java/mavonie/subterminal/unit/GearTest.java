package mavonie.subterminal.unit;


import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.util.LinkedHashMap;
import java.util.Map;

import mavonie.subterminal.Models.Gear;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
import mavonie.subterminal.unit.Base.BaseDBUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

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

    /**
     * Check getItemsForSelect along with the linkedHashMapAdapter
     */
    @Test
    public void testGetItemsForSelect() {
        Gear gear = createGear();
        Gear gear2 = createGear();

        LinkedHashMap gearItems = new Gear().getItemsForSelect(Gear.COLUMN_NAME_CONTAINER_MANUFACTURER);

        assertNotNull(gearItems);
        assertEquals(gearItems.size(), new Gear().count());

        LinkedHashMapAdapter exitsAdapter = new LinkedHashMapAdapter<String, String>(
                RuntimeEnvironment.application,
                R.layout.item_simple,
                gearItems,
                LinkedHashMapAdapter.FLAG_FILTER_ON_VALUE
        );

        Map.Entry entry = exitsAdapter.getItem(1);
        assertEquals(gear2.getId(), Integer.parseInt((String) entry.getKey()));

        int id = gear2.getId();

        Integer position = exitsAdapter.findPositionFromKey(id);
        Map.Entry entry2 = exitsAdapter.getItem(position);

        assertEquals(id, Integer.parseInt((String) entry2.getKey()));
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