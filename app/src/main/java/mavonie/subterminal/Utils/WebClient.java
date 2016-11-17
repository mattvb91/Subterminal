package mavonie.subterminal.Utils;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.card.payment.CardIOActivity;
import mavonie.subterminal.MainActivity;

public class WebClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (Uri.parse(url).toString().contains("?payment=true")) {

            if (!Subterminal.getUser().isLoggedIn()) {
                UIHelper.toast("Please sign in before proceeding");
                return false;
            }

            Intent scanIntent = new Intent(MainActivity.getActivity(), CardIOActivity.class);

            // customize these values to suit your needs.
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
            scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true); // default: false

            // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
            MainActivity.getActivity().startActivityForResult(scanIntent, MainActivity.MY_SCAN_REQUEST_CODE);
        }
        return true;
    }
}