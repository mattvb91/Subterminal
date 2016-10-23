package mavonie.subterminal.unit.Base;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import mavonie.subterminal.BuildConfig;

@Ignore
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 23, constants = BuildConfig.class)
public class BaseUnit {

}
