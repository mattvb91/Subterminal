package mavonie.subterminal.unit;

import org.junit.Test;

import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.unit.Base.BaseDBUnitTest;

import static org.junit.Assert.*;

public class ExitUnitTest extends BaseDBUnitTest{

    @Test
    public void testGetExit() {
        Exit exit = (Exit) new Exit().getOneById(1);

        assertNotNull(exit);
    }
}