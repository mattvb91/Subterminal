package mavonie.subterminal.unit;

import android.util.Pair;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.ExitDetails;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.unit.Base.BaseDBUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExitTest extends BaseDBUnit {

    /**
     * Test the item we save comes back the same
     */
    @Test
    public void saveToDb() {
        Exit exit = createExit();
        assertTrue(exit.exists());

        Exit exitDb = (Exit) new Exit().getOneById(exit.getId());

        assertTrue(exit.equals(exitDb));

        Exit exit1 = (Exit) new Exit().getItem(new Pair<String, String>(Model._ID, Integer.toString(exitDb.getId())));
        assertEquals(exit1, exitDb);

        HashMap<String, Object> params = new HashMap<>();
        params.put(Model.FILTER_ORDER_DIR, Model.FILTER_ORDER_DIR_ASC);
        params.put(Model.FILTER_ORDER_FIELD, mavonie.subterminal.Models.Exit.COLUMN_NAME_NAME);

        List exits = new Exit().getItems(params);

        assertNotNull(exits);
    }

    /**
     * Make sure our count is correct
     */
    @Test
    public void testCount() {
        int size = createExit().getItems(null).size();
        assertEquals(size, new Exit().count());
    }

    @Test
    public void testExitDetails() {
        Exit exit = this.createExit();

        ExitDetails details = new ExitDetails();
        details.setExit_id(exit.getId());
        details.setRules("Test rules");
        details.setDifficulty_tracking_exit(exit.DIFFICULTY_BEGINNER);
        details.setDifficulty_tracking_freefall(exit.DIFFICULTY_INTERMEDIATE);
        details.setDifficulty_tracking_landing(exit.DIFFICULTY_ADVANCED);
        details.setDifficulty_tracking_overall(exit.DIFFICULTY_EXPERT);
        details.setDifficulty_wingsuit_exit(exit.DIFFICULTY_BEGINNER);
        details.setDifficulty_wingsuit_freefall(exit.DIFFICULTY_INTERMEDIATE);
        details.setDifficulty_wingsuit_landing(exit.DIFFICULTY_ADVANCED);
        details.setDifficulty_wingsuit_overall(exit.DIFFICULTY_EXPERT);

        exit.setDetails(details);
        //Set the global_id so the details get saved
        exit.setGlobal_id("testing");
        exit.save();

        Exit exit2 = (Exit) new Exit().getOneById(exit.getId());
        assertNotNull(exit2.getDetails());
        assertEquals(exit2.getDetails(), details);
    }

    public static Exit createExit() {
        Exit exit = new Exit();

        exit.setName("Test exit");
        exit.setRockdrop_distance(200);
        exit.setAltitude_to_landing(200);
        exit.setDescription("Test Description");
        exit.setLatitude(59.02342);
        exit.setLongtitude(24.30456);
        exit.setObject_type(Exit.TYPE_EARTH);
        exit.save();

        return exit;
    }

    @Test
    public void testNullGlobalId() {
        createExit();

        HashMap<String, Object> params = new HashMap<>();

        params.put(Model.FILTER_WHERE_FIELD, mavonie.subterminal.Models.Exit.COLUMN_NAME_GLOBAL_ID);
        params.put(Model.FILTER_WHERE_VALUE, null);

        List<Exit> exits = new Exit().getItems(params);

        for (Exit exit : exits) {
            assertNull(exit.getGlobal_id());
        }
    }
}