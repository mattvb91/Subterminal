package mavonie.subterminal.unit;

import org.junit.Test;

import mavonie.subterminal.Models.Exit;
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
    }

    /**
     * Make sure our count is correct
     */
    @Test
    public void testCount() {
        int size = createExit().getItems(null).size();
        assertEquals(size, new Exit().count());
    }

    private Exit createExit() {
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
        exit.save();

        return exit;
    }
}