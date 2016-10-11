package mavonie.subterminal.models;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import mavonie.subterminal.MainActivity;

/**
 * Created by mavon on 11/10/16.
 */

public class User {

    private String firstName;
    private String surname;
    private String email;
    private Profile facebookProfile;
    private AccessToken facebookToken;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setFacebookData() {
        GraphRequest request = GraphRequest.newMeRequest(
                MainActivity.getUser().getFacebookToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {

                            User user = MainActivity.getUser();
                            user.setEmail(response.getJSONObject().getString("email"));
                            user.setFirstName(response.getJSONObject().getString("first_name"));
                            user.setSurname(response.getJSONObject().getString("last_name"));
                            user.setFacebookProfile(Profile.getCurrentProfile());

                            MainActivity.getActivity().updateDrawerProfile();

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

    public String getFullName() {
        return this.getFirstName() + ' ' + this.getSurname();
    }

    public void init() {
        this.setFacebookData();
    }

    //TODO implement user save locally
    public void save() {

    }
}
