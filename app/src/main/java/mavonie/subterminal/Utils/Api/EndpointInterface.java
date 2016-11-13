package mavonie.subterminal.Utils.Api;

import com.facebook.AccessToken;
import com.stripe.android.model.Token;
import com.stripe.model.Charge;

import java.util.List;

import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Gear;
import mavonie.subterminal.Models.Jump;
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
    Call<List<Exit>> listPublicExits();

    @POST("user")
    Call<AccessToken> createOrUpdateUser(@Body AccessToken token);

    @GET("user")
    Call<User> getUser();

    @POST("user/settings/notification")
    Call<Notification> syncPreferenceNotification(@Body Notification notification);

    @POST("payment")
    Call<Charge> sendPaymentToken(@Body Token token);


    //Exit requests
    @POST("exit")
    Call<Exit> syncExit(@Body Exit exit);

    @DELETE("user/exit/{id}")
    Call<Void> delete(@Path("id") Integer id);

    @GET("user/exit")
    Call<List<Exit>> downloadExits(
            @Query("last_sync") String lastSync);

    //Gear requests
    @POST("gear")
    Call<Gear> syncGear(@Body Gear exit);

    @DELETE("user/gear/{id}")
    Call<Void> deleteGear(@Path("id") Integer id);

    @GET("user/gear")
    Call<List<Gear>> downloadGear(
            @Query("last_sync") String lastSync);

    //Jump requests
    @POST("jump")
    Call<Jump> syncJump(@Body Jump exit);

    @DELETE("user/jump/{id}")
    Call<Void> deleteJump(@Path("id") Integer id);

    @GET("user/jump")
    Call<List<Gear>> downloadJumps(
            @Query("last_sync") String lastSync);
}
