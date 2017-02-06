package mavonie.subterminal.Utils.Api;

import com.facebook.AccessToken;
import com.stripe.android.model.Token;
import com.stripe.model.Charge;

import java.util.List;

import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Gear;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Preferences.Notification;
import mavonie.subterminal.Models.Signature;
import mavonie.subterminal.Models.Skydive.Aircraft;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Models.Skydive.Rig;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Models.User;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("dropzone")
    Call<List<Dropzone>> getDropzones(@Query("last_sync") String lastSync);

    @GET("aircraft")
    Call<List<Aircraft>> getAircraft();

    @POST("user")
    Call<AccessToken> createOrUpdateUser(@Body AccessToken token);

    @GET("user")
    Call<User> getUser();

    @POST("user/settings/notification")
    Call<Notification> syncPreferenceNotification(@Body Notification notification);

    @POST("payment")
    Call<Charge> sendPaymentToken(@Body Token token);

    @Multipart
    @POST("user/image")
    Call<Void> uploadImage(@Part MultipartBody.Part filePart, @Query("entity_type") Integer entity_type, @Query("entity_id") Integer entity_id);

    //Exit requests
    @POST("exit")
    Call<Exit> syncExit(@Body Exit exit);

    @DELETE("user/exit/{id}")
    Call<Void> deleteExit(@Path("id") Integer id);

    @GET("user/exits")
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

    @GET("user/jumps")
    Call<List<Jump>> downloadJumps(
            @Query("last_sync") String lastSync);

    //Jump requests
    @POST("suit")
    Call<Suit> syncSuit(@Body Suit suit);

    @DELETE("user/suit/{id}")
    Call<Void> deleteSuit(@Path("id") Integer id);

    @GET("user/suits")
    Call<List<Suit>> downloadSuits(
            @Query("last_sync") String lastSync);

    //Rig requests
    @POST("rig")
    Call<Rig> syncRig(@Body Rig rig);

    @DELETE("user/rig/{id}")
    Call<Void> deleteRig(@Path("id") Integer id);

    @GET("user/rigs")
    Call<List<Rig>> downloadRigs(
            @Query("last_sync") String lastSync);

    //Skydive requests
    @POST("skydive")
    Call<Skydive> syncSkydive(@Body Skydive skydive);

    @DELETE("user/skydive/{id}")
    Call<Void> deleteSkydive(@Path("id") Integer id);

    @GET("user/skydives")
    Call<List<Skydive>> downloadSkydives(
            @Query("last_sync") String lastSync);

    //Signature requests
    @POST("signature")
    Call<Signature> syncSignature(@Body Signature signature);

    @GET("user/signatures")
    Call<List<Signature>> downloadSignatures(
            @Query("last_sync") String lastSync);

    @GET("user/images")
    Call<List<Image>> downloadImages(
            @Query("last_sync") String lastSync);

    @GET("dropzone/{id}/images")
    Call<List<Image>> getDropzoneImages(@Path("id") Integer id);

    @GET("dropzone/{id}/services")
    Call<List<String>> getDropzoneServices(@Path("id") Integer id);
}
