package mavonie.subterminal.unit;


import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.io.IOException;
import java.util.List;

import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Utils.API;
import mavonie.subterminal.Utils.Synchronized;
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
    public void setUp() {
        super.setUp();
        this.api = new API(RuntimeEnvironment.application);
    }

    @Test
    public void testPublicExitsCall() throws IOException {
        Call exitsCall = this.api.getEndpoints().listPublicExits();

        Response response = exitsCall.execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body() instanceof List);

        List<Exit> exits = (List) response.body();
        assertTrue(exits.get(0) instanceof Exit);
    }

    @Test
    public void testDropzonesCall() throws IOException {
        Call dropzonesCall = this.api.getEndpoints().getDropzones(Synchronized.getLastSyncPref(Synchronized.PREF_LAST_SYNC_DROPZONES));

        Response response = dropzonesCall.execute();

        assertTrue(response.isSuccessful());
        assertTrue(response.body() instanceof List);

        List<Dropzone> dropzones = (List) response.body();
        assertTrue(dropzones.get(0) instanceof Dropzone);
    }

    //TODO test authorized method
    @Test
    public void testUnauthorizedUserGetCall() throws IOException {
        Call userGet = this.api.getEndpoints().getUser();
        Response response = userGet.execute();

        assertTrue(response.code() == 401);
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
        List<Exit> exits = (List) response.body();

        for (Exit exit : exits) {
            assertTrue(exit.save());
            assertNotNull(exit.getDetails().getExitId());
            assertTrue(exit.getDetails().exists());
        }

    }
}
