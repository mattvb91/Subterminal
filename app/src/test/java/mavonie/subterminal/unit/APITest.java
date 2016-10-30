package mavonie.subterminal.unit;


import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.io.IOException;

import mavonie.subterminal.Models.Api.Exits;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Utils.API;
import mavonie.subterminal.unit.Base.BaseDBUnit;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Testing API related functionality
 */
public class APITest extends BaseDBUnit {

    private API api;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.api = new API(RuntimeEnvironment.application);
    }

    @Test
    public void testPublicExitsCall() throws IOException {
        Call exitsCall = this.api.getEndpoints().listPublicExits();

        Response response = exitsCall.execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body() instanceof Exits);

        Exits exits = (Exits) response.body();
        assertTrue(exits.getExits().get(0) instanceof Exit);
    }

    /**
     * Make sure exit details get saved on first init
     * https://github.com/mattvb91/Subterminal/issues/6
     */
    @Test
    public void testIssue6() throws IOException {
        //Clear table to make sure we are initializing these exits
        this._db.getWritableDatabase().execSQL("DELETE FROM " + Exit.TABLE_NAME);

        Call exitsCall = this.api.getEndpoints().listPublicExits();

        Response response = exitsCall.execute();
        Exits exits = (Exits) response.body();

        for (Exit exit : exits.getExits()) {
            assertTrue(exit.save());
            assertNotNull(exit.getDetails().getExit_id());
            assertTrue(exit.getDetails().exists());
        }

    }
}
