package mavonie.subterminal.Views.Premium;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import io.card.payment.CardIOActivity;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.UIHelper;


/**
 * Premium view
 */
public class PremiumView extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_premium_view, container, false);

        WebView web = (WebView) view.findViewById(R.id.premium_view_web);
        web.loadUrl("https://subterminal.eu");

        UIHelper.getAddButton().hide();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.getActivity().setTitle(R.string.get_premium);
    }

    @Override
    protected String getItemClass() {
        return null;
    }
}
