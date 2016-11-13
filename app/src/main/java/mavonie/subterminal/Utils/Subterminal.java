package mavonie.subterminal.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
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
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Gear;
import mavonie.subterminal.Models.Jump;
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

    private static JobManager jobManager;

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

        if (getUser().isPremium()) {
            syncData();
        }
    }

    public static FirebaseAnalytics getAnalytics() {
        return analytics;
    }

    public static CallbackManager getmCallbackManager() {
        return mCallbackManager;
    }

    private static void configureJobManager(Context context) {

        Configuration.Builder builder = new Configuration.Builder(context)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";

                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void v(String text, Object... args) {

                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120);//wait 2 minute
        jobManager = new JobManager(builder.build());
    }

    public static synchronized JobManager getJobManager(Context context) {
        if (jobManager == null) {
            configureJobManager(context);
        }
        return jobManager;
    }

    /**
     * Use this for situations where we dont want certain actions
     * happening during a test
     *
     * @return boolean
     */
    public static boolean isTesting() {
        try {
            Class.forName("mavonie.subterminal.unit.Base.BaseUnit");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Sync at startup. Make sure the sync order is
     * Exits -> Gear -> Jump
     */
    public static void syncData() {
        for (Exit exit : Exit.getExitsForSync()) {
            exit.addSyncJob();
        }

        for (Exit exit : Exit.getExitsForDelete()) {
            exit.addSyncJob();
        }

        for (Gear gear : Gear.getGearForSync()) {
            gear.addSyncJob();
        }

        for (Gear gear : Gear.getGearForDelete()) {
            gear.addSyncJob();
        }

        for (Jump jump : Jump.getJumpsForSync()) {
            jump.addSyncJob();
        }

        for (Jump jump : Jump.getJumpsForDelete()) {
            jump.addSyncJob();
        }
    }
}