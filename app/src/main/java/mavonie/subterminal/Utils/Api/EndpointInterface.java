package mavonie.subterminal.Utils.Api;

import com.facebook.AccessToken;
import com.stripe.android.model.Token;
import com.stripe.model.Charge;

import mavonie.subterminal.Models.Api.Exits;
import mavonie.subterminal.Models.Preferences.Notification;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * All the endpoints available
 */
public interface EndpointInterface {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter
    @GET("exit")
    Call<Exits> listPublicExits();

    @POST("user")
    Call<AccessToken> createOrUpdateUser(@Body AccessToken token);

    @POST("user/settings/notification")
    Call<Notification> syncPreferenceNotification(@Body Notification notification);

    @POST("payment")
    Call<Charge> sendPaymentToken(@Body Token token);
}
