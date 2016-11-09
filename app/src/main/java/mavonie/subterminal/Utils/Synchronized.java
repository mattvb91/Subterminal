package mavonie.subterminal.Utils;

import com.pixplicity.easyprefs.library.Prefs;

public abstract class Synchronized {

    public static String PREF_LAST_SYNC_EXITS = "LAST_SYNC_EXITS";

    public static String getLastSyncExits() {
        return Prefs.getString(PREF_LAST_SYNC_EXITS, "2000-01-01 00:00:00");
    }
}
