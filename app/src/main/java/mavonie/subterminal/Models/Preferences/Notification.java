package mavonie.subterminal.Models.Preferences;


import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Class to handle notification settings
 */
public class Notification {

    private String device_token;

    public Notification() {
        this.device_token = getDevice_token();
    }

    public String getDevice_token() {
        return FirebaseInstanceId.getInstance().getToken();
    }
}
