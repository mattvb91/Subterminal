package mavonie.subterminal.Utils.Api;

import com.google.firebase.iid.FirebaseInstanceIdService;

import jonathanfinerty.once.Once;
import mavonie.subterminal.Utils.API;
import mavonie.subterminal.Utils.Subterminal;


public class FirebaseInstanceService extends FirebaseInstanceIdService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onTokenRefresh() {
        Once.needToDo(API.CALLS_UPDATE_NOTIFICATIONS);
        Subterminal.getApi().updateNotificationSettings();
    }
}
