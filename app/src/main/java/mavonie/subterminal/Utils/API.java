package mavonie.subterminal.Utils;

import android.content.Context;
import android.view.View;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;
import com.stripe.android.model.Token;
import com.stripe.model.Charge;

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
import mavonie.subterminal.Models.Api.Exits;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Preferences.Notification;
import mavonie.subterminal.Models.User;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Api.EndpointInterface;
import mavonie.subterminal.Utils.Api.Intercepter;
import mavonie.subterminal.Utils.Date.DateFormat;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Handle all our API interactions
 */
public class API {

    private retrofit2.Retrofit retrofit;
    private Gson gson;
    private Context context;

    public API(Context context) {
        this.context = context;
    }

    public static final String CALLS_LIST_PUBLIC_EXITS = "LIST_PUBLIC_EXITS";
    public static final String CALLS_UPDATE_NOTIFICATIONS = "UPDATE_NOTIFICATIONS";
    public static final String CALLS_UPDATE_USER = "UPDATE_USER";

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
                    .baseUrl(apiUrl).addConverterFactory(GsonConverterFactory.create(this.getGson()))
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

    /**
     * Calls for startup
     */
    public void init() {
        if (!Once.beenDone(TimeUnit.HOURS, 1, CALLS_LIST_PUBLIC_EXITS)) {
            updatePublicExits();
        }

        if (!Once.beenDone(TimeUnit.DAYS, 1, CALLS_UPDATE_NOTIFICATIONS)) {
            updateNotificationSettings();
        }

        if (Subterminal.getUser().isLoggedIn()) {
            if (!Once.beenDone(TimeUnit.DAYS, 1, CALLS_UPDATE_USER)) {
                updateLocalUser();
            }
        }

        downloadExits();
    }

    private void downloadExits() {
        UIHelper.setProgressBarVisibility(View.VISIBLE);
        Call myExits = this.getEndpoints().downloadExits(Synchronized.getLastSyncExits());

        myExits.enqueue(new Callback<List<Exit>>() {
            @Override
            public void onResponse(Call call, Response response) {
                UIHelper.setProgressBarVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Exits exits = (Exits) response.body();
                    for (Exit exit : exits.getExits()) {
                        exit.markSynced();
                    }

                    Prefs.putString(Synchronized.PREF_LAST_SYNC_EXITS, DateFormat.dateTimeNow());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    /**
     * Update the global exits list
     */
    private void updatePublicExits() {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        Call publicExits = this.getEndpoints().listPublicExits();
        publicExits.enqueue(new Callback<List<Exit>>() {
            @Override
            public void onResponse(Call call, Response response) {
                List<Exit> exits = ((Exits) response.body()).getExits();

                for (Exit exit : exits) {
                    Exit.createOrUpdate(exit);
                }

                Once.markDone(CALLS_LIST_PUBLIC_EXITS);
                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                API.issueContactingServer();
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    /**
     * Send notification object
     */
    public void updateNotificationSettings() {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        Call syncNotification = this.getEndpoints().syncPreferenceNotification(new Notification());
        syncNotification.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call call, Response response) {
                Once.markDone(CALLS_UPDATE_NOTIFICATIONS);
                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                API.issueContactingServer();
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    /**
     * Sync the current user to the server
     */
    public void createOrUpdateRemoteUser() {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        Call userCreateUpdate = this.getEndpoints().createOrUpdateUser(Subterminal.getUser().getFacebookToken());
        userCreateUpdate.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call call, Response response) {
                Subterminal.getApi().updateLocalUser();
                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                API.issueContactingServer();
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    /**
     * Payment confirmed, send token and await confirmation
     *
     * @param token
     */
    public void sendPaymentToken(Token token) {
        Call paymentToken = this.getEndpoints().sendPaymentToken(token);
        paymentToken.enqueue(new Callback<Charge>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {

                    //User set to premium
                    User.activatePremium();
                } else {
                    //Show message
                }

                UIHelper.removeLoadSpinner();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                API.issueContactingServer();
            }
        });
    }

    /**
     * Update the user object
     */
    public void updateLocalUser() {
        Call updateUser = this.getEndpoints().getUser();
        updateUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call call, Response response) {
                Subterminal.getUser().update((User) response.body());
                Once.markDone(CALLS_UPDATE_USER);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                API.issueContactingServer();
            }
        });
    }

    private static void issueContactingServer() {
        UIHelper.toast("There was an issue contacting the server");
        UIHelper.removeLoadSpinner();
    }

    /**
     * @return Gson
     */
    public Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setLenient()
                    .create();
        }
        return gson;
    }

    public void syncExit(final Exit exit) {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        Call syncExit = this.getEndpoints().syncExit(exit);
        syncExit.enqueue(new Callback<Exit>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    exit.markSynced();
                }
                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    public void setLastSyncExits() {
        Prefs.putString(Synchronized.PREF_LAST_SYNC_EXITS, DateFormat.dateTimeNow());
    }
}
