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
}
