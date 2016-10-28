package mavonie.subterminal.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import mavonie.subterminal.Models.Model;

/**
 * Hold global variables and general access functions.
 */
public class Subterminal {

    private static Model activeModel;
    private static int activeFragment;


    /**
     * @return Model
     */
    public static Model getActiveModel() {
        return activeModel;
    }

    /**
     * Set the current active model
     *
     * @param model
     */
    public static void setActiveModel(Model model) {
        activeModel = model;
    }

    /**
     * Set id for our currently active fragment
     *
     * @param i
     */
    public static void setActiveFragment(int i) {
        activeFragment = i;
    }

    /**
     * Get our currently active fragment
     *
     * @return int
     */
    public static int getActiveFragment() {
        return activeFragment;
    }

    /**
     * Return the associated metadata
     *
     * @param context
     * @param string
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static String getMetaData(Context context, String string) throws PackageManager.NameNotFoundException {

        ApplicationInfo ai = context.getPackageManager()
                .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        Bundle bundle = ai.metaData;
        String meta = bundle.getString(string);

        return meta;
    }
}
