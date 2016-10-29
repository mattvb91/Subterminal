package mavonie.subterminal.Utils.Api;

import mavonie.subterminal.Models.Api.Exits;
import retrofit2.Call;
import retrofit2.http.GET;


/**
 * All the endpoints available
 */
public interface EndpointInterface {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter
    @GET("exit")
    Call<Exits> listPublicExits();
}
