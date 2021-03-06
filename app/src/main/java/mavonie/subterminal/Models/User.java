package mavonie.subterminal.Models;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Settings;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;

/**
 * Created by mavon on 11/10/16.
 */

public class User {

    public static String PREFS_PREMIUM = "is_premium";

    private String email;
    private boolean is_premium;
    private Profile facebookProfile;
    private AccessToken facebookToken;
    private String apiToken;
    private Settings settings;

    public User() {
        this.email = Prefs.getString("email", null);
        this.settings = new Settings();
        this.apiToken = Prefs.getString("apiToken", null);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
        Prefs.putString("email", email);
    }

    public Profile getFacebookProfile() {
        if (facebookProfile == null) {
            this.setFacebookProfile(Profile.getCurrentProfile());
        }
        return facebookProfile;
    }

    public void setFacebookProfile(Profile facebookProfile) {
        this.facebookProfile = facebookProfile;
    }

    public AccessToken getFacebookToken() {
        if (facebookToken == null) {
            this.setFacebookToken(AccessToken.getCurrentAccessToken());
        }
        return facebookToken;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
        Prefs.putString("apiToken", apiToken);
    }

    public Settings getSettings() {
        return settings;
    }

    public void setFacebookToken(AccessToken facebookToken) {
        this.facebookToken = facebookToken;
    }

    public void requestFacebookData() {
        GraphRequest request = GraphRequest.newMeRequest(
                Subterminal.getUser().getFacebookToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = response.getJSONObject().getString("email");

                            if (email.isEmpty()) {
                                UIHelper.toast("Your email is not confirmed with facebook. Please try manual login");
                                Subterminal.getUser().logOut();
                                return;
                            }

                            Subterminal.getUser().setEmail(email);
                            Subterminal.getUser().setFacebookProfile(Profile.getCurrentProfile());
                            Subterminal.getApi().createOrUpdateRemoteUser();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void init() {
        if (this.isLoggedIn()) {
            if (this.getEmail() == null && AccessToken.getCurrentAccessToken() != null) {
                this.requestFacebookData();
            } else {
                UIHelper.userLoggedIn();
            }

            if (this.isPremium()) {
                UIHelper.userPremium();
            }
        }
    }

    //TODO implement user save locally
    public void save() {

    }

    public boolean isLoggedIn() {
        if (this.getApiToken() != null) {
            return true;
        }

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null || accessToken.isExpired()) {

            //TODO this is a bit hacky
            if (this.getEmail() != null) {
                this.logOut();
            }

            return false;
        }

        return accessToken != null;
    }

    public void logOut() {
        LoginManager.getInstance().logOut();
        this.setEmail(null);
        this.setApiToken(null);
        UIHelper.userLoggedOut();
    }

    /**
     * Check if a user has premium
     *
     * @return boolean
     */
    public boolean isPremium() {
        return Prefs.getBoolean(PREFS_PREMIUM, false);
    }

    public static void activatePremium() {
        Subterminal.getUser().setIsPremium(true);

        UIHelper.toast(MainActivity.getActivity().getString(R.string.premium_member_welcome));
        UIHelper.goToFragment(R.id.nav_jumps);

        UIHelper.userPremium();
        Subterminal.getApi().init();
    }


    //Used for the updated call
    public boolean getIsPremium() {
        return this.is_premium;
    }

    public void setIsPremium(boolean is_premium) {
        this.is_premium = is_premium;
        Prefs.putBoolean(PREFS_PREMIUM, is_premium);
    }

    /**
     * Update our User object with what we received from the API
     *
     * @param responseBody
     */
    public void update(User responseBody) {
        this.setIsPremium(responseBody.getIsPremium());
        this.setEmail(responseBody.email);
    }

    /**
     * We could have a facebook token or a JWT token. Check which one.
     *
     * @return
     */
    public String getActiveToken() {
        if (AccessToken.getCurrentAccessToken() != null) {
            return AccessToken.getCurrentAccessToken().getToken();
        }

        return getApiToken();
    }
}
