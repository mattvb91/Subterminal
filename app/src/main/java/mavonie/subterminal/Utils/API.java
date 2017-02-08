package mavonie.subterminal.Utils;

import android.content.Context;
import android.os.AsyncTask;
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

import az.openweatherapi.OWService;
import jonathanfinerty.once.Once;
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
    private OkHttpClient okHttpClient;
    private Gson gson;
    private Context context;
    private OWService openWeather;

    public API(Context context) {
        this.context = context;
    }

    public static final String CALLS_LIST_PUBLIC_EXITS = "LIST_PUBLIC_EXITS";
    public static final String CALLS_LIST_DROPZONES = "LIST_DROPZONES";
    public static final String CALLS_UPDATE_NOTIFICATIONS = "UPDATE_NOTIFICATIONS";
    public static final String CALLS_UPDATE_USER = "UPDATE_USER";
    public static final String CALLS_LIST_AIRCRAFT = "LIST_AIRCRAFT";

    /**
     * Get our retrofit client.
     * We need to load in our own ssl cert to be able to communicate with our API.
     *
     * @return
     */
    private retrofit2.Retrofit getClient() {

        if (this.retrofit == null) {
            this.okHttpClient = null;
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

                this.okHttpClient = new OkHttpClient.Builder().addInterceptor(new Intercepter(this.context))
                        .sslSocketFactory(sslContext.getSocketFactory())
                        .build();

                apiUrl = Subterminal.getMetaData(this.context, "mavonie.subterminal.API_URL");

            } catch (Exception e) {
                e.printStackTrace();
            }

            // creating a RestAdapter using the custom client
            this.retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(apiUrl).addConverterFactory(GsonConverterFactory.create(this.getGson()))
                    .client(this.okHttpClient)
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
     * Get an instance of the open weather service
     *
     * @return
     */
    public OWService getOpenWeatherClient() {
        if (this.openWeather == null) {
            this.openWeather = new OWService(Subterminal.getMetaData(this.context, "mavonie.subterminal.OPENWEATHER_API_KEY"));
        }

        return openWeather;
    }

    /**
     * Calls for startup
     */
    public void init() {
        if (!Once.beenDone(TimeUnit.HOURS, 1, CALLS_LIST_PUBLIC_EXITS)) {
            updatePublicExits();
        }

        if (!Once.beenDone(TimeUnit.HOURS, 1, CALLS_LIST_AIRCRAFT)) {
            updateAircraft();
        }

        if (!Once.beenDone(TimeUnit.HOURS, 1, CALLS_LIST_DROPZONES)) {
            updateDropzones();
        }

        if (!Once.beenDone(TimeUnit.DAYS, 1, CALLS_UPDATE_NOTIFICATIONS)) {
            updateNotificationSettings();
        }

        if (Subterminal.getUser().isLoggedIn()) {
            if (!Once.beenDone(TimeUnit.DAYS, 1, CALLS_UPDATE_USER)) {
                updateLocalUser();
            }

            if (Subterminal.getUser().isPremium()) {
                downloadModel(new Exit());
                downloadModel(new Gear());
                downloadModel(new Suit());
                downloadModel(new Jump());
                downloadModel(new Skydive());
                downloadModel(new Rig());
                downloadModel(new Signature());

                Synchronizable.syncEntities();
                downloadImages();
            }
        }
    }

    private void updateAircraft() {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        Call aircraft = this.getEndpoints().getAircraft();
        aircraft.enqueue(new Callback<List<Aircraft>>() {
            @Override
            public void onResponse(Call call, final Response response) {
                if (response.isSuccessful()) {
                    List<Aircraft> aircrafts = (List<Aircraft>) response.body();

                    for (Aircraft aircraft : aircrafts) {
                        Aircraft.createOrUpdate(aircraft);
                    }

                    Once.markDone(CALLS_LIST_AIRCRAFT);
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
     * Update the dropzones list on background runnable
     */
    private void updateDropzones() {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        Call dropzones = this.getEndpoints().getDropzones(Synchronized.getLastSyncPref(Synchronized.PREF_LAST_SYNC_DROPZONES));
        dropzones.enqueue(new Callback<List<Dropzone>>() {
            @Override
            public void onResponse(Call call, final Response response) {
                if (response.isSuccessful()) {

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            List<Dropzone> dropzones = (List<Dropzone>) response.body();

                            for (Dropzone dropzone : dropzones) {
                                Dropzone.createOrUpdate(dropzone);
                            }

                            Synchronized.setLastSyncPref(Synchronized.PREF_LAST_SYNC_DROPZONES, response.headers().get("server_time"));
                            Once.markDone(CALLS_LIST_DROPZONES);
                        }
                    });

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
                        Exit.createOrUpdatePublicExit(exit);
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

                    //Init again as we are now logged in
                    Subterminal.getApi().init();
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

    private void downloadImages() {
        UIHelper.setProgressBarVisibility(View.VISIBLE);
        Call myImages = this.getEndpoints().downloadImages(Synchronized.getLastSyncPref(Synchronized.PREF_LAST_SYNC_IMAGE));

        myImages.enqueue(new Callback<List<Rig>>() {
            @Override
            public void onResponse(Call call, Response response) {
                UIHelper.setProgressBarVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<Image> images = (List) response.body();
                    Image.downloadImages(images, response.headers().get("server_time"));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    public void deleteImage(Image image) {
    }

    public OkHttpClient getOkHttpClient() {
        return this.okHttpClient;
    }

    public void deleteModel(final Synchronizable model) {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        model.getDeleteEndpoint().enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful() || response.code() == 403) {
                    model.delete();
                }
                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    public void syncModel(final Synchronizable model) {
        UIHelper.setProgressBarVisibility(View.VISIBLE);

        model.getSyncEndpoint().enqueue(new Callback<Synchronizable>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    model.markSynced();

                    Synchronized.setLastSyncPref(model.getSyncIdentifier(), response.headers().get("server_time"));
                }

                UIHelper.setProgressBarVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }

    /**
     * Download remote instances of passed in model.
     *
     * @param model
     */
    private void downloadModel(final Synchronizable model) {
        UIHelper.setProgressBarVisibility(View.VISIBLE);
        model.getDownloadEndpoint().enqueue(new Callback<List<Synchronizable>>() {
            @Override
            public void onResponse(Call call, Response response) {
                UIHelper.setProgressBarVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<Synchronizable> models = (List) response.body();
                    for (Synchronizable model : models) {
                        model.markSynced();
                    }

                    Synchronized.setLastSyncPref(model.getSyncIdentifier(), response.headers().get("server_time"));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                UIHelper.setProgressBarVisibility(View.GONE);
            }
        });
    }
}
