package mavonie.subterminal.unit;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.unit.Base.BaseDBUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


public class JumpTest extends BaseDBUnit {

    /**
     * Validate what we save into the database is what
     * we get out of it
     */
    @Test
    public void saveToDb() {

        Jump jump = createJump();

        Jump dbJump = (Jump) new Jump().getOneById(jump.getId());

        assertTrue(jump.equals(dbJump));
    }

    @Test
    public void testFormattedSlider() {
        Jump jump = createJump();

        assertEquals(jump.getFormattedSlider(), "Down");
        jump.setSlider(jump.SLIDER_UP);
        assertEquals(jump.getFormattedSlider(), "Up");
    }

    /**
     * Test our jump/row count is working
     */
    @Test
    public void testJumpNumber() {
        Jump jump = createJump();

        Jump jump2 = createJump();
        jump2.setDate("2015-05-23");
        jump2.save();

        Jump jump3 = createJump();
        jump3.setDate("2015-05-22");
        jump3.save();

        HashMap<String, Object> params = new HashMap<>();
        params.put(Model.FILTER_ORDER_DIR, "DESC");
        params.put(Model.FILTER_ORDER_FIELD, mavonie.subterminal.Models.Jump.COLUMN_NAME_DATE);

        List jumps = jump.getItems(params);

        jump2 = (Jump) jumps.get(1);
        assertEquals(jump2.getRowId(), 2);

        jump = (Jump) jumps.get(2);
        assertEquals(jump.getRowId(), 1);

        //Change it to the most recent
        jump2.setDate("2015-05-25");
        jump2.save();

        jumps = jump.getItems(params);
        jump2 = (Jump) jumps.get(0);
        assertEquals(jump2.getRowId(), 3);
        assertEquals(jump2.getDate(), "2015-05-25");
    }

    public static Jump createJump() {
        Jump jump = new Jump();

        jump.setDelay(2);
        jump.setDescription("Jump description");
        jump.setExitId(1);
        jump.setPcSize(32);
        jump.setGearId(1);
        jump.setDate("2015-05-24");
        jump.setSlider(0);
        jump.save();

        return jump;
    }

    @Test
    public void addRemoveSuitTest() {
        Jump jump = createJump();

        Suit suit = GearTest.createSuit();
        jump.setSuitId(suit.getId());
        jump.save();

        Assert.assertTrue(jump.equals(new Jump().getOneById(jump.getId())));

        jump.setSuitId(null);
        jump.save();

        Assert.assertTrue(jump.equals(new Jump().getOneById(jump.getId())));
    }
}
