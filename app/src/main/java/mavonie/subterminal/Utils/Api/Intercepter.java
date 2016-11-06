package mavonie.subterminal.Utils.Api;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.IOException;

import mavonie.subterminal.Utils.Subterminal;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor for adding our custom apikey header
 */
public class Intercepter implements Interceptor {

    private String appApiKey;

    public Intercepter(Context context) throws PackageManager.NameNotFoundException {
        this.appApiKey = Subterminal.getMetaData(context, "mavonie.subterminal.API_APP_KEY");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder();

        builder.header("apiappkey", this.appApiKey);

        if (Subterminal.getUser().isLoggedIn()) {
            builder.header("sessiontoken", Subterminal.getUser().getFacebookToken().getToken());
        }

        builder.method(original.method(), original.body());
        Request request = builder.build();

        return chain.proceed(request);
    }
}
