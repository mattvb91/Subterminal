package mavonie.subterminal.unit.Base;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import mavonie.subterminal.BuildConfig;
import mavonie.subterminal.DB.DatabaseHandler;
import mavonie.subterminal.DB.VersionUtils;
import mavonie.subterminal.Models.Model;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 23, constants = BuildConfig.class)
public class BaseDBUnitTest extends BaseUnitTest {

    @Before
    public void setUp() throws Exception {
        DatabaseHandler db = new DatabaseHandler(RuntimeEnvironment.application, "test_database", null, VersionUtils.getVersionCode(RuntimeEnvironment.application));
        Model.setDB(db);
    }
}
