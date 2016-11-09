package mavonie.subterminal.Utils.Api;

import com.facebook.AccessToken;
import com.stripe.android.model.Token;
import com.stripe.model.Charge;

import mavonie.subterminal.Models.Api.Exits;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Preferences.Notification;
import mavonie.subterminal.Models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


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

    @GET("user")
    Call<User> getUser();

    @POST("user/settings/notification")
    Call<Notification> syncPreferenceNotification(@Body Notification notification);

    @POST("payment")
    Call<Charge> sendPaymentToken(@Body Token token);


    //Authenticated requests
    @POST("exit")
    Call<Exit> syncExit(@Body Exit exit);

    @DELETE("exit")
    Call delete(@Body Exit exit);

    @GET("user/exit")
    Call<Exits> downloadExits(
            @Query("last_sync") String lastSync);
}
