package mavonie.subterminal.unit.Skydive;

import android.util.Pair;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.unit.Base.BaseDBUnit;
import mavonie.subterminal.unit.GearTest;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Dropzone unit tests
 */
public class SkydiveTest extends BaseDBUnit {

    @Test
    public void saveToDb() {

        Skydive skydive = createSkydive();
        assertTrue(skydive.exists());

        Skydive dbSkydive = (Skydive) new Skydive().getOneById(skydive.getId());

        Assert.assertTrue(skydive.equals(dbSkydive));

        Skydive skydive1 = (Skydive) new Skydive().getItem(new Pair<>(Model._ID, Integer.toString(dbSkydive.getId())));
        assertEquals(skydive1, dbSkydive);

        HashMap<String, Object> params = new HashMap<>();
        params.put(Model.FILTER_ORDER_DIR, Model.FILTER_ORDER_DIR_ASC);
        params.put(Model.FILTER_ORDER_FIELD, Skydive._ID);

        List skydives = new Skydive().getItems(params);

        assertNotNull(skydives);
    }

    @Test
    public void addRemoveSuitTest() {
        Skydive skydive = createSkydive();
        assertTrue(skydive.exists());

        Assert.assertTrue(skydive.equals(new Skydive().getOneById(skydive.getId())));

        Suit suit = GearTest.createSuit();
        skydive.setSuitId(suit.getId());
        skydive.save();

        Assert.assertTrue(skydive.equals(new Skydive().getOneById(skydive.getId())));

        skydive.setSuitId(null);
        skydive.save();

        Assert.assertTrue(skydive.equals(new Skydive().getOneById(skydive.getId())));
    }

    @Test
    public void testCutaway() {
        Skydive skydive = createSkydive();

        Assert.assertTrue(skydive.getCutaway().equals(Skydive.CUTAWAY_NO));

        skydive.setCutaway(Skydive.CUTAWAY_YES);
        skydive.save();

        Skydive dbSkydive = (Skydive) new Skydive().getOneById(skydive.getId());

        assertTrue(dbSkydive.getCutaway().equals(Skydive.CUTAWAY_YES));
    }

    @Test
    public void testDelaySum() {
        Skydive skydive = createSkydive();
        skydive.setDelay(12);
        skydive.save();

        assertEquals(12, new Skydive().sum(Skydive.COLUMN_NAME_DELAY));

        Skydive skydive2 = createSkydive();
        skydive2.setDelay(12);
        skydive2.save();

        assertEquals(24, new Skydive().sum(Skydive.COLUMN_NAME_DELAY));
    }

    /**
     * TODO update with dynamic aircrafts/dropzones/gear
     *
     * @return
     */
    public static Skydive createSkydive() {
        Skydive skydive = new Skydive();

        skydive.setJumpType(Skydive.SKYDIVE_TYPE_CANOPY_ACCURACY);
        skydive.setAircraftId(1);
        skydive.setDropzoneId(1);
        skydive.setDate("2001-01-02");
        skydive.setDeployAltitude(2300);
        skydive.setExitAltitude(12344);
        skydive.setDelay(54);
        skydive.setDescription("THis is the description");
        skydive.setRigId(1);
        skydive.setHeightUnit(Subterminal.HEIGHT_UNIT_IMPERIAL);

        skydive.save();

        return skydive;
    }
}
