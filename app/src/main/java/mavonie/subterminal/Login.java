package mavonie.subterminal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;

/**
 * Login handling
 */
public class Login extends Fragment {

    @BindView(R.id.email_edittext) EditText email;
    @BindView(R.id.password_edittext) EditText password;

    @OnClick(R.id.facebook_login_button) void facebookDialog() {
        UIHelper.facebookDialog();
    }

    @OnClick(R.id.register_button) void registerButtonPressed() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://subterminal.eu/register"));
        startActivity(browserIntent);
    }

    @OnClick(R.id.reset_password_button) void resetPasswordPressed() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://subterminal.eu/password/reset"));
        startActivity(browserIntent);
    }

    @OnClick(R.id.sign_in_button) void signInPressed() {
        //Make sure email & password have been filed.
        if (email.getText().toString().isEmpty()) {
            email.setError("Email Required");
            return;
        }

        if (password.getText().toString().isEmpty()) {
            password.setError("Password Required");
            return;
        }

        Subterminal.getApi().authenticate(email.getText().toString(), password.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(Subterminal.getUser().isLoggedIn()) {
            UIHelper.goToFragment(R.id.nav_dashboard);
        }

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        if(MainActivity.getActivity().getOptionsMenu() != null)
            MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_filter).setVisible(false);

        UIHelper.getAddButton().hide();

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.getActivity().setTitle("Sign In");
    }
}
