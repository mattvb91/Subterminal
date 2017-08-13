package mavonie.subterminal.DB;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class VersionUtils {

    public static int getVersionCode(Context context) throws NameNotFoundException {
        PackageInfo manager = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return manager.versionCode;
    }
}