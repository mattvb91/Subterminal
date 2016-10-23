package mavonie.subterminal.unit;

import org.junit.Test;

import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.unit.Base.BaseDBUnit;

import static junit.framework.Assert.*;


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

    private Jump createJump() {
        Jump jump = new Jump();

        jump.setDelay(2);
        jump.setDescription("Jump description");
        jump.setExit_id(1);
        jump.setPc_size(32);
        jump.setGear_id(1);
        jump.setDate("2015-05-24");
        jump.setSlider(0);
        jump.save();

        return jump;
    }
}
