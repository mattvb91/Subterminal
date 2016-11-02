package mavonie.subterminal.Utils.Api;

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

    @POST("user/settings/notification")
    Call<Notification> syncPreferenceNotification(@Body Notification notification);
}
