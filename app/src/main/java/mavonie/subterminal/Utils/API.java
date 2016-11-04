package mavonie.subterminal.Utils;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import jonathanfinerty.once.Once;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Api.Exits;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Preferences.Notification;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Api.EndpointInterface;
import mavonie.subterminal.Utils.Api.Intercepter;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Handle all our API interactions
 */
public class API implements Callback {

    private retrofit2.Retrofit retrofit;
    private Context context;

    public API(Context context) {
        this.context = context;
    }

    public static final String CALLS_LIST_PUBLIC_EXITS = "LIST_PUBLIC_EXITS";
    public static final String CALLS_UPDATE_NOTIFICATIONS = "UPDATE_NOTIFICATIONS";

    /**
     * Get our retrofit client.
     * We need to load in our own ssl cert to be able to communicate with our API.
     *
     * @return
     */
    private retrofit2.Retrofit getClient() {

        if (this.retrofit == null) {
            OkHttpClient okHttpClient = null;
            String apiUrl = null;

            try {
                Certificate ca = getCertificate();

                // creating a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);

                // creating a TrustManager that trusts the CAs in our KeyStore
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                // creating an SSLSocketFactory that uses our TrustManager
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, tmf.getTrustManagers(), null);

                okHttpClient = new OkHttpClient.Builder().addInterceptor(new Intercepter(this.context))
                        .sslSocketFactory(sslContext.getSocketFactory())
                        .build();

                apiUrl = Subterminal.getMetaData(this.context, "mavonie.subterminal.API_URL");

            } catch (Exception e) {

            }

            // creating a RestAdapter using the custom client
            this.retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(apiUrl).addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return this.retrofit;
    }

    private Certificate getCertificate() throws CertificateException, IOException {

        // loading CAs from an InputStream
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream cert = this.context.getResources().openRawResource(R.raw.subterminal);
        Certificate ca;

        try {
            ca = cf.generateCertificate(cert);
        } finally {
            cert.close();
        }

        return ca;
    }

    public EndpointInterface getEndpoints() {
        return this.getClient().create(EndpointInterface.class);
    }

    public void init() {
        EndpointInterface endpoints = this.getEndpoints();

        if (!Once.beenDone(TimeUnit.HOURS, 1, CALLS_LIST_PUBLIC_EXITS)) {
            Call publicExits = endpoints.listPublicExits();
            publicExits.enqueue(this);
        }

        if (!Once.beenDone(TimeUnit.DAYS, 1, CALLS_UPDATE_NOTIFICATIONS)) {
            updateNotificationSettings();
        }
    }

    /**
     * Send notification object
     */
    public void updateNotificationSettings() {
        Call syncNotification = this.getEndpoints().syncPreferenceNotification(new Notification());
        syncNotification.enqueue(this);
    }

    /**
     * Sync the current user to the server
     */
    public void createOrUpdateUser() {
        Call userCreateUpdate = this.getEndpoints().createOrUpdateUser(Subterminal.getUser().getFacebookToken());
        userCreateUpdate.enqueue(this);
    }

    @Override
    public void onResponse(Call call, Response response) {

        if (response.isSuccessful()) {
            if (response.body() instanceof Exits) {
                List<Exit> exits = ((Exits) response.body()).getExits();

                for (Exit exit : exits) {
                    Exit.createOrUpdate(exit);
                }

                Once.markDone(CALLS_LIST_PUBLIC_EXITS);
            } else if (response.body() instanceof Notification) {
                Once.markDone(CALLS_UPDATE_NOTIFICATIONS);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Toast.makeText(MainActivity.getActivity(), "Could not update from server", Toast.LENGTH_SHORT).show();
    }
}
