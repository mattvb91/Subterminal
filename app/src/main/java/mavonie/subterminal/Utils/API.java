package mavonie.subterminal.Utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Api.Exits;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Api.EndpointInterface;
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

                // loading CAs from an InputStream
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                InputStream cert = MainActivity.getActivity().getApplicationContext()
                        .getResources().openRawResource(R.raw.subterminal);
                Certificate ca;
                try {
                    ca = cf.generateCertificate(cert);
                } finally {
                    cert.close();
                }

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

                okHttpClient = new OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.getSocketFactory())
                        .build();

                ApplicationInfo ai = MainActivity.getActivity().getPackageManager()
                        .getApplicationInfo(MainActivity.getActivity().getPackageName(), PackageManager.GET_META_DATA);
                Bundle bundle = ai.metaData;
                apiUrl = bundle.getString("mavonie.subterminal.API_URL");

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

    private EndpointInterface getEndpoints() {
        return this.getClient().create(EndpointInterface.class);
    }

    public void init() {
        EndpointInterface endpoints = this.getEndpoints();
        Call publicExits = endpoints.listPublicExits();

        publicExits.enqueue(this);
    }

    @Override
    public void onResponse(Call call, Response response) {

        if (response.body() instanceof Exits) {
            List<Exit> exits = ((Exits) response.body()).getExits();

            for (Exit exit : exits) {
                //TODO check if we already have one that matches,
                //If we dont, save it, if we do, check if it is different/needs to be updated
            }
        }


    }

    @Override
    public void onFailure(Call call, Throwable t) {

    }
}
