package mavonie.subterminal.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pixplicity.easyprefs.library.Prefs;

import de.cketti.library.changelog.ChangeLog;
import jonathanfinerty.once.Once;
import mavonie.subterminal.CustomPinActivity;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.User;
import mavonie.subterminal.Preference;

/**
 * Hold global variables and general access functions.
 */
public class Subterminal {

    private static Model activeModel;
    private static int activeFragment;
    private static API api;
    private static FirebaseAnalytics analytics;

    private static CallbackManager mCallbackManager;

    protected static User user;

    /**
     * We want only one user instance for the main activity
     *
     * @return User
     */
    public static User getUser() {
        if (user == null) {
            user = new User();
        }

        return user;
    }

    /**
     * @return API
     */
    public static API getApi() {
        return api;
    }

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
    public static void init(MainActivity activity) {

        new Prefs.Builder()
                .setContext(activity)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(activity.getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        mCallbackManager = CallbackManager.Factory.create();

        analytics = FirebaseAnalytics.getInstance(activity);

        FacebookSdk.sdkInitialize(activity);

        Once.initialise(activity);

        Fresco.initialize(activity);

        getUser().init();

        if (Prefs.getBoolean(Preference.PIN_ENABLED, false)) {
            LockManager.getInstance().enableAppLock(
                    activity.getApplicationContext(),
                    CustomPinActivity.class
            );
            LockManager.getInstance().getAppLock().setShouldShowForgot(false);
        }

        ChangeLog cl = new ChangeLog(activity);
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
        }

        api = new API(activity);
        api.init();

        UIHelper.init();
    }

    public static FirebaseAnalytics getAnalytics() {
        return analytics;
    }

    public static CallbackManager getmCallbackManager() {
        return mCallbackManager;
    }
}