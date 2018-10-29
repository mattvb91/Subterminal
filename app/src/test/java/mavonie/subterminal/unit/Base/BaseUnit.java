package mavonie.subterminal.unit.Base;

import android.content.ContextWrapper;

import com.facebook.FacebookSdk;
import com.pixplicity.easyprefs.library.Prefs;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import mavonie.subterminal.BuildConfig;

@Ignore
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.DEFAULT_MANIFEST_NAME, sdk = 25)
public class BaseUnit {

    @Before
    public void setUp() {
        new Prefs.Builder()
                .setContext(RuntimeEnvironment.application)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(RuntimeEnvironment.application.getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        FacebookSdk.setApplicationId("test");
        FacebookSdk.sdkInitialize(RuntimeEnvironment.application);
    }
}
