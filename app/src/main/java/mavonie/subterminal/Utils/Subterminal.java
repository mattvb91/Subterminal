package mavonie.subterminal.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import com.pixplicity.easyprefs.library.Prefs;

import de.cketti.library.changelog.ChangeLog;
import mavonie.subterminal.CustomPinActivity;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Preference;

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

    /**
     * Initialize everything we need
     */
    public static void init() {

        Fresco.initialize(MainActivity.getActivity());

        new Prefs.Builder()
                .setContext(MainActivity.getActivity())
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(MainActivity.getActivity().getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        if (Prefs.getBoolean(Preference.PIN_ENABLED, false)) {
            LockManager.getInstance().enableAppLock(
                    MainActivity.getActivity().getApplicationContext(),
                    CustomPinActivity.class
            );
            LockManager.getInstance().getAppLock().setShouldShowForgot(false);
        }

        ChangeLog cl = new ChangeLog(MainActivity.getActivity());
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
        }

        API api = new API(MainActivity.getActivity());
        api.init();
    }
}
