package mavonie.subterminal.Utils;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.card.payment.CardIOActivity;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (Uri.parse(url).toString().contains("?payment=true")) {

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