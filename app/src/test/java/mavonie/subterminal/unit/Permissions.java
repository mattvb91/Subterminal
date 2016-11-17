package mavonie.subterminal.unit;

import org.junit.Test;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.Fs;

import java.util.HashSet;

import mavonie.subterminal.unit.Base.BaseUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Make sure permissions are as we expect and nothing else
 * has been introduced through third party libs
 */

public final class Permissions extends BaseUnit {

    private static final String[] EXPECTED_PERMISSIONS = {
            "android.permission.INTERNET",
            "android.permission.ACCESS_GPS",
            "android.permission.ACCESS_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.CAMERA",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "mavonie.subterminal.permission.C2D_MESSAGE",
            "com.google.android.c2dm.permission.RECEIVE",
            "android.permission.WAKE_LOCK",
            "android.permission.USE_FINGERPRINT",
            "android.permission.VIBRATE",
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