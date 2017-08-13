package mavonie.subterminal.unit.Base;

import android.content.pm.PackageManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import mavonie.subterminal.BuildConfig;
import mavonie.subterminal.DB.DatabaseHandler;
import mavonie.subterminal.DB.VersionUtils;
import mavonie.subterminal.Models.Model;

@Ignore
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.DEFAULT_MANIFEST_NAME, sdk = 25, constants = BuildConfig.class)
public class BaseDBUnit extends BaseUnit {

    public DatabaseHandler _db = null;

    @Before
    public void setUp() {
        if (_db == null) {
            try {
                this._db = new DatabaseHandler(RuntimeEnvironment.application, "test_database", null, VersionUtils.getVersionCode(RuntimeEnvironment.application));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Model.setDB(this._db);
        }

        super.setUp();
    }
}
