package mavonie.subterminal.Utils;

import android.content.Context;
import android.view.View;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Gear;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Preferences.Notification;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Models.User;
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

            if (Subterminal.getUser().isPremium()) {
                downloadExits();
                downloadGear();
                downloadSuits();
                downloadJumps();

                Synchronizable.syncEntities();
            }
        }
    }

    private void downloadSuits() {
        UIHelper.setProgressBarVisibility(View.VISIBLE);
        Call mySuits = this.getEndpoints().downloadSuits(Synchronized.getLastSyncPref(Synchronized.PREF_LAST_SYNC_SUIT));

        mySuits.enqueue(new Callback<List<Suit>>() {
            @Override
            public void onResponse(Call call, Response response) {
                UIHelper.setProgressBarVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<Suit> suits = (List) response.body();
                    for (Suit suit : suits) {
                        suit.markSynced();
                    }

                    Synchronized.setLastSyncPref(Synchronized.PREF_LAST_SYNC_SUIT);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    private void downloadJumps() {
        UIHelper.setProgressBarVisibility(View.VISIBLE);
        Call myJumps = this.getEndpoints().downloadJumps(Synchronized.getLastSyncPref(Synchronized.PREF_LAST_SYNC_JUMP));

        myJumps.enqueue(new Callback<List<Jump>>() {
            @Override
            public void onResponse(Call call, Response response) {
                UIHelper.setProgressBarVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<Jump> jumps = (List) response.body();
                    for (Jump jump : jumps) {
                        jump.markSynced();
                    }

                    Synchronized.setLastSyncPref(Synchronized.PREF_LAST_SYNC_JUMP);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    private void downloadExits() {
        UIHelper.setProgressBarVisibility(View.VISIBLE);
        Call myExits = this.getEndpoints().downloadExits(Synchronized.getLastSyncPref(Synchronized.PREF_LAST_SYNC_EXITS));

        myExits.enqueue(new Callback<List<Exit>>() {
            @Override
            public void onResponse(Call call, Response response) {
                UIHelper.setProgressBarVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<Exit> exits = (List) response.body();
                    for (Exit exit : exits) {
                        exit.markSynced();
                    }

                    Synchronized.setLastSyncPref(Synchronized.PREF_LAST_SYNC_EXITS);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    private void downloadGear() {
        UIHelper.setProgressBarVisibility(View.VISIBLE);
        Call myGear = this.getEndpoints().downloadGear(Synchronized.getLastSyncPref(Synchronized.PREF_LAST_SYNC_GEAR));

        myGear.enqueue(new Callback<List<Gear>>() {
            @Override
            public void onResponse(Call call, Response response) {
                UIHelper.setProgressBarVisibility(View.GONE);

                if (response.isSuccessful()) {
                    List<Gear> gears = (List) response.body();

                    for (Gear gear : gears) {
                        gear.markSynced();
                    }
                    Synchronized.setLastSyncPref(Synchronized.PREF_LAST_SYNC_GEAR);
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
                if (response.isSuccessful()) {
                    List<Exit> exits = (List<Exit>) response.body();

                    for (Exit exit : exits) {
                        Exit.createOrUpdate(exit);
                    }

                    Once.markDone(CALLS_LIST_PUBLIC_EXITS);
                }

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
                if (response.isSuccessful()) {
                    Once.markDone(CALLS_UPDATE_NOTIFICATIONS);
                }

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
                if (response.isSuccessful()) {
                    Subterminal.getApi().updateLocalUser();

                    //Init again as we are now logged in
                    Subterminal.getApi().init();
                }

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
                    issueContactingServer();
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
                if (response.isSuccessful()) {
                    Subterminal.getUser().update((User) response.body());
                    Once.markDone(CALLS_UPDATE_USER);
                }
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
                    .serializeNulls()
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

                    Synchronized.setLastSyncPref(Synchronized.PREF_LAST_SYNC_EXITS);
                }

                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    public void deleteExit(final Exit exit) {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        Call deleteExit = this.getEndpoints().delete(exit.getId());
        deleteExit.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful() || response.code() == 403) {
                    exit.delete();
                }
                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    public void deleteGear(final Gear gear) {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        Call deleteGear = this.getEndpoints().deleteGear(gear.getId());
        deleteGear.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful() || response.code() == 403) {
                    gear.delete();
                }
                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });

    }

    public void syncGear(final Gear gear) {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        Call syncGear = this.getEndpoints().syncGear(gear);
        syncGear.enqueue(new Callback<Gear>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    gear.markSynced();

                    Synchronized.setLastSyncPref(Synchronized.PREF_LAST_SYNC_GEAR);
                }

                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });

    }

    public void deleteJump(final Jump jump) {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        Call deleteJump = this.getEndpoints().deleteJump(jump.getId());
        deleteJump.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful() || response.code() == 403) {
                    jump.delete();
                }
                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    public void syncJump(final Jump jump) {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        Call syncJump = this.getEndpoints().syncJump(jump);
        syncJump.enqueue(new Callback<Jump>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    jump.markSynced();

                    Synchronized.setLastSyncPref(Synchronized.PREF_LAST_SYNC_JUMP);
                }

                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    public void syncSuit(final Suit suit) {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        Call syncSuit = this.getEndpoints().syncSuit(suit);
        syncSuit.enqueue(new Callback<Jump>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    suit.markSynced();

                    Synchronized.setLastSyncPref(Synchronized.PREF_LAST_SYNC_SUIT);
                }

                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    public void deleteSuit(final Suit suit) {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        Call deleteSuit = this.getEndpoints().deleteSuit(suit.getId());
        deleteSuit.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful() || response.code() == 403) {
                    suit.delete();
                }
                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }
}
