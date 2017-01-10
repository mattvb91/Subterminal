package mavonie.subterminal.Utils;

import com.pixplicity.easyprefs.library.Prefs;

import mavonie.subterminal.Utils.Date.DateFormat;

/**
 * Storing synchronization times
 */
public abstract class Synchronized {

    public static final String PREF_LAST_SYNC_EXITS = "LAST_SYNC_EXITS";
    public static final String PREF_LAST_SYNC_GEAR = "LAST_SYNC_GEAR";
    public static final String PREF_LAST_SYNC_JUMP = "LAST_SYNC_JUMP";
    public static final String PREF_LAST_SYNC_SUIT = "LAST_SYNC_SUIT";
    public static final String PREF_LAST_SYNC_DROPZONES = "LAST_SYNC_DROPZONES";
    public static final String PREF_LAST_SYNC_RIG = "LAST_SYNC_RIG";
    public static final String PREF_LAST_SYNC_SKYDIVE = "LAST_SYNC_SKYDIVE";


    public static void setLastSyncPref(String pref) {
        Prefs.putString(pref, DateFormat.dateTimeNow());
    }

    public static void setLastSyncPref(String pref, String serverTime) {
        Prefs.putString(pref, serverTime);
    }

    public static String getLastSyncPref(String pref) {
        return Prefs.getString(pref, "2000-01-01 00:00:00");
    }
}
