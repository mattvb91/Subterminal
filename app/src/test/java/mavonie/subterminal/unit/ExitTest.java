package mavonie.subterminal.unit;

import android.util.Pair;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.unit.Base.BaseDBUnit;

import static org.junit.Assert.*;

public class ExitTest extends BaseDBUnit {

    @Test
    public void testGetExit() {
        Exit exit = (Exit) new Exit().getOneById(1);

        assertNotNull(exit);
    }

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

    public static Exit createExit() {
        Exit exit = new Exit();

        exit.setName("Test exit");
        exit.setRockdrop_distance(200);
        exit.setAltitude_to_landing(200);
        exit.setDifficulty_tracking_exit(exit.DIFFICULTY_BEGINNER);
        exit.setDifficulty_tracking_freefall(exit.DIFFICULTY_INTERMEDIATE);
        exit.setDifficulty_tracking_landing(exit.DIFFICULTY_ADVANCED);
        exit.setDifficulty_tracking_overall(exit.DIFFICULTY_EXPERT);
        exit.setDifficulty_wingsuit_exit(exit.DIFFICULTY_BEGINNER);
        exit.setDifficulty_wingsuit_freefall(exit.DIFFICULTY_INTERMEDIATE);
        exit.setDifficulty_wingsuit_landing(exit.DIFFICULTY_ADVANCED);
        exit.setDifficulty_wingsuit_overall(exit.DIFFICULTY_EXPERT);
        exit.setDescription("Test Description");
        exit.setRules("Test rules");
        exit.setLatitude(59.02342);
        exit.setLongtitude(24.30456);
        exit.setObject_type(Exit.TYPE_EARTH);
        exit.save();

        return exit;
    }
}