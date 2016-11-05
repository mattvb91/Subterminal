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

import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;

/**
 * Created by mavon on 11/10/16.
 */

public class User {

    private String email;
    private Profile facebookProfile;
    private AccessToken facebookToken;

    public User() {
        this.email = Prefs.getString("email", null);
    }

    public String getFirstName() {
        return this.facebookProfile.getFirstName();
    }

    public String getSurname() {
        return this.facebookProfile.getLastName();
    }

    public String getEmail() {
        return Prefs.getString("email", null);
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

    public void setFacebookToken(AccessToken facebookToken) {
        this.facebookToken = facebookToken;
    }

    public void requestFacebookData() {
        GraphRequest request = GraphRequest.newMeRequest(
                Subterminal.getUser().getFacebookToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Subterminal.getUser().setEmail(response.getJSONObject().getString("email"));
                            Subterminal.getUser().setFacebookProfile(Profile.getCurrentProfile());
                            Subterminal.getApi().createOrUpdateUser();

                            UIHelper.userLoggedIn();

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
            if (this.getEmail() == null) {
                this.requestFacebookData();
            } else {
                UIHelper.userLoggedIn();
            }
        }
    }

    //TODO implement user save locally
    public void save() {

    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null || accessToken.isExpired()) {
            this.logOut();
            return false;
        }

        return accessToken != null;
    }

    public void logOut() {
        LoginManager.getInstance().logOut();
        Prefs.remove("email");
        UIHelper.userLoggedOut();
    }
}
