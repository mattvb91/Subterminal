package mavonie.subterminal.Utils;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.card.payment.CardIOActivity;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Webclient
 * Override to check if payment button has been hit and user is authenticated
 */
public class WebClient extends WebViewClient {

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        final Uri uri = Uri.parse(url);
        return handleUri(uri);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        final Uri uri = request.getUrl();
        return handleUri(uri);
    }


    private boolean handleUri(final Uri uri) {
        if (uri.toString().contains("?payment=true")) {

            //Make sure user is authenticated before we allow further
            Call updateUser = Subterminal.getApi().getEndpoints().getUser();
            updateUser.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        Intent scanIntent = new Intent(MainActivity.getActivity(), CardIOActivity.class);

                        // customize these values to suit your needs.
                        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
                        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
                        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
                        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true); // default: false

                        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
                        MainActivity.getActivity().startActivityForResult(scanIntent, MainActivity.MY_SCAN_REQUEST_CODE);
                    } else {
                        UIHelper.toast("Please sign in before proceeding");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    UIHelper.toast("There was an issue contacting the server");
                }
            });
        }

        return true;
    }
}