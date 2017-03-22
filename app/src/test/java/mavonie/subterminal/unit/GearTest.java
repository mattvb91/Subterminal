package mavonie.subterminal.unit;


import junit.framework.Assert;

import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.util.LinkedHashMap;
import java.util.Map;

import mavonie.subterminal.Models.Gear;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
import mavonie.subterminal.unit.Base.BaseDBUnit;
import mavonie.subterminal.unit.Skydive.SkydiveTest;

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
        Gear gear2 = (Gear) gear.getOneById(gear.getId());

        assertEquals(gear2.getDeleted(), Synchronizable.DELETED_TRUE);

        gear2.delete();
        assertNull(gear.getOneById(gear2.getId()));
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

    public static Suit createSuit() {
        Suit suit = new Suit();

        suit.setManufacturer("Manufacturer");
        suit.setType(Suit.TYPE_WINGSUIT);
        suit.setDateInUse("2010-06-05");
        suit.setModel("Model");
        suit.setSerial("Serial");
        suit.save();

        return suit;
    }

    @Test
    public void testSaveSuitToDB() {

        Suit suit = createSuit();
        assertTrue(suit.exists());
        Suit dbSuit = (Suit) new Suit().getOneById(suit.getId());

        assertTrue(suit.equals(dbSuit));
    }

    /**
     * Make sure item gets removed from db
     */
    @Test
    public void deleteSuitFromDB() {
        Suit suit = createSuit();

        Jump jump = JumpTest.createJump();
        jump.setSuitId(suit.getId());
        jump.save();

        Skydive skydive = SkydiveTest.createSkydive();
        skydive.setSuitId(suit.getId());
        skydive.save();

        assertTrue(suit.delete());
        Suit dbSuit = (Suit) new Suit().getOneById(suit.getId());

        assertEquals(dbSuit.getDeleted(), Synchronizable.DELETED_TRUE);

        Jump dbJump = (Jump) new Jump().getOneById(jump.getId());
        assertNull(dbJump.getSuitId());

        Skydive dbSkydive = (Skydive) new Skydive().getOneById(skydive.getId());
        assertNull(dbSkydive.getSuitId());

        dbSuit.delete();
        assertNull(dbSuit.getOneById(dbSuit.getId()));
    }

    @Test
    public void testSuitGetJumps() {
        Suit suit = createSuit();

        Jump jump = JumpTest.createJump();
        jump.setSuitId(suit.getId());
        jump.save();

        Jump jump2 = JumpTest.createJump();
        jump2.setSuitId(suit.getId());
        jump2.save();

        assertEquals(2, suit.getJumps().size());

        assertEquals(suit.getJumps().get(0), jump);
        assertEquals(suit.getJumps().get(1), jump2);
    }

    @Test
    public void testSuitGetSkydives() {
        Suit suit = createSuit();

        Skydive jump = SkydiveTest.createSkydive();
        jump.setSuitId(suit.getId());
        jump.save();

        Skydive jump2 = SkydiveTest.createSkydive();
        jump2.setSuitId(suit.getId());
        jump2.save();

        assertEquals(2, suit.getSkydives().size());

        assertEquals(suit.getSkydives().get(0), jump);
        assertEquals(suit.getSkydives().get(1), jump2);
    }

    @Test
    public void testSuitJumpCount() {
        Suit suit = createSuit();
        suit.save();

        Assert.assertEquals(0, suit.getJumpCount());

        Jump jump = JumpTest.createJump();
        jump.setSuitId(suit.getId());
        jump.save();

        Assert.assertEquals(1, suit.getJumpCount());

        Skydive skydive = SkydiveTest.createSkydive();
        skydive.setSuitId(suit.getId());
        skydive.save();

        Assert.assertEquals(2, suit.getJumpCount());

        Skydive skydive2 = SkydiveTest.createSkydive();
        skydive2.setSuitId(suit.getId());
        skydive2.save();

        Skydive skydive3 = SkydiveTest.createSkydive();
        skydive3.setSuitId(suit.getId());
        skydive3.save();

        Assert.assertEquals(4, suit.getJumpCount());
    }
}