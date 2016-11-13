package mavonie.subterminal.Utils;

import com.pixplicity.easyprefs.library.Prefs;

import mavonie.subterminal.Utils.Date.DateFormat;

/**
 * Storing synchronization times
 */
public abstract class Synchronized {

    public static String PREF_LAST_SYNC_EXITS = "LAST_SYNC_EXITS";
    public static String PREF_LAST_SYNC_GEAR = "LAST_SYNC_GEAR";
    public static String PREF_LAST_SYNC_JUMP = "LAST_SYNC_JUMP";

    public static void setLastSyncExits() {
        Prefs.putString(Synchronized.PREF_LAST_SYNC_EXITS, DateFormat.dateTimeNow());
    }

    public static String getLastSyncExits() {
        return Prefs.getString(PREF_LAST_SYNC_EXITS, "2000-01-01 00:00:00");
    }

    public static void setLastSyncGear() {
        Prefs.putString(Synchronized.PREF_LAST_SYNC_GEAR, DateFormat.dateTimeNow());
    }

    public static String getLastSyncGear() {
        return Prefs.getString(PREF_LAST_SYNC_GEAR, "2000-01-01 00:00:00");
    }

    public static void setLastSyncJump() {
        Prefs.putString(Synchronized.PREF_LAST_SYNC_JUMP, DateFormat.dateTimeNow());
    }

    public static void getLastSyncJump() {
        Prefs.getString(Synchronized.PREF_LAST_SYNC_JUMP, "2000-01-01 00:00:00");
    }
}
