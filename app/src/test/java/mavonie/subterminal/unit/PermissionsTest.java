package mavonie.subterminal.unit;

import org.junit.Test;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.Fs;

import static org.junit.Assert.*;

import java.util.HashSet;

import mavonie.subterminal.unit.Base.BaseUnitTest;

/**
 * Make sure permissions are as we expect and nothing else
 * has been introduced through third party libs
 */

public final class PermissionsTest extends BaseUnitTest {

    private static final String[] EXPECTED_PERMISSIONS = {
            "android.permission.INTERNET",
            "android.permission.ACCESS_GPS",
            "android.permission.ACCESS_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    private static final String MERGED_MANIFEST = "build/intermediates/manifests/full/debug/AndroidManifest.xml";

    @Test
    public void shouldMatchPermissions() {
        AndroidManifest manifest = new AndroidManifest(
                Fs.fileFromPath(MERGED_MANIFEST),
                null,
                null
        );

        HashSet permissions = new HashSet<>(manifest.getUsedPermissions());
        assertEquals(permissions.size(), EXPECTED_PERMISSIONS.length);

        for (String permission : EXPECTED_PERMISSIONS) {
            assertTrue(permissions.contains(permission));
        }
    }
}