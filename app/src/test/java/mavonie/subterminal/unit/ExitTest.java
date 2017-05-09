package mavonie.subterminal.unit;

import android.util.Pair;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.ExitDetails;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.Subterminal;
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
        Exit exit = new Exit();

        exit.setName("Test exit");
        exit.setRockdropDistance(200);
        exit.setAltitudeToLanding(200);
        exit.setDescription("Test Description");
        exit.setLatitude(59.02342);
        exit.setLongtitude(24.30456);
        exit.setGlobalId("testing");
        exit.setObjectType(Exit.TYPE_EARTH);
        exit.setHeightUnit(Subterminal.HEIGHT_UNIT_IMPERIAL);

        ExitDetails details = new ExitDetails();
        details.setExitId(exit.getId());
        details.setRules("Test rules");
        details.setDifficultyTrackingExit(Exit.DIFFICULTY_BEGINNER);
        details.setDifficultyTrackingFreefall(Exit.DIFFICULTY_INTERMEDIATE);
        details.setDifficultyTrackingLanding(Exit.DIFFICULTY_ADVANCED);
        details.setDifficultyTrackingOverall(Exit.DIFFICULTY_EXPERT);
        details.setDifficultyWingsuitExit(Exit.DIFFICULTY_BEGINNER);
        details.setDifficultyWingsuitFreefall(Exit.DIFFICULTY_INTERMEDIATE);
        details.setDifficultyWingsuitLanding(Exit.DIFFICULTY_ADVANCED);
        details.setDifficultyWingsuitOverall(Exit.DIFFICULTY_EXPERT);

        exit.setDetails(details);
        //Set the global_id so the details get saved
        Exit.createOrUpdatePublicExit(exit);

        Exit exit2 = (Exit) new Exit().getOneById(exit.getId());
        assertNotNull(exit2.getDetails());
        assertEquals(exit2.getDetails(), details);
    }

    public static Exit createExit() {
        Exit exit = new Exit();

        exit.setName("Test exit");
        exit.setRockdropDistance(200);
        exit.setAltitudeToLanding(200);
        exit.setDescription("Test Description");
        exit.setLatitude(59.02342);
        exit.setLongtitude(24.30456);
        exit.setObjectType(Exit.TYPE_EARTH);
        exit.setHeightUnit(Subterminal.HEIGHT_UNIT_IMPERIAL);
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
            assertNull(exit.getGlobalId());
        }
    }

    @Test
    public void testGetExitsForSync() {
        this.setUp();

        Exit exit1 = createExit();
        Exit exit2 = createExit();

        List<Exit> list = Exit.getExitsForSync();
        assertEquals(2, list.size());

        exit1.markSynced();

        List<Exit> listSynced = Exit.getExitsForSync();
        assertEquals(1, listSynced.size());
    }

    @Test
    public void autoIncrementTest() {

        createExit();
        Assert.assertEquals(new Exit().getNextAutoIncrement(), 2);
        createExit();
        createExit();
        createExit();
        Assert.assertEquals(new Exit().getNextAutoIncrement(), 5);

        Assert.assertEquals(new Jump().getNextAutoIncrement(), 1);
        Jump jump = JumpTest.createJump();
        Assert.assertEquals(new Jump().getNextAutoIncrement(), 2);
        JumpTest.createJump();
        JumpTest.createJump();
        Assert.assertEquals(new Jump().getNextAutoIncrement(), 4);
    }

    @Test
    public void updateJumpsTest() {
        Exit exit = createExit();
        Exit exit2 = createExit();

        Jump jump1 = JumpTest.createJump();
        jump1.setExitId(exit.getId());
        jump1.save();

        Jump jump2 = JumpTest.createJump();
        jump2.setExitId(exit.getId());
        jump2.save();

        Jump jump3 = JumpTest.createJump();
        jump3.setExitId(exit2.getId());
        jump3.save();

        Jump dbJump = (Jump) new Jump().getOneById(jump1.getId());
        Jump dbJump2 = (Jump) new Jump().getOneById(jump2.getId());
        Jump dbJump3 = (Jump) new Jump().getOneById(jump3.getId());

        Assert.assertEquals(dbJump.getExitId().toString(), Integer.toString(exit.getId()));
        Assert.assertEquals(dbJump2.getExitId().toString(), Integer.toString(exit.getId()));
        Assert.assertEquals(dbJump3.getExitId().toString(), Integer.toString(exit2.getId()));
        Assert.assertEquals(2, new Jump().count(new Query(Jump.COLUMN_NAME_EXIT_ID, exit.getId()).getParams()));

        //Delete the exit
        exit.delete();

        //Reload the jumps
        dbJump = (Jump) new Jump().getOneById(jump1.getId());
        dbJump2 = (Jump) new Jump().getOneById(jump2.getId());
        dbJump3 = (Jump) new Jump().getOneById(jump3.getId());

        //Check it has updated the jumps
        Assert.assertEquals(dbJump.getExitId(), null);
        Assert.assertEquals(dbJump2.getExitId(),null);
        Assert.assertEquals(dbJump3.getExitId().toString(), Integer.toString(exit2.getId()));
        Assert.assertEquals(0, new Jump().count(new Query(Jump.COLUMN_NAME_EXIT_ID, exit.getId()).getParams()));
    }
}