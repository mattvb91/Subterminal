package mavonie.subterminal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import com.pixplicity.easyprefs.library.Prefs;

import de.cketti.library.changelog.ChangeLog;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;


public class Preference extends BaseFragment {

    Switch pinSwitch;

    public static final String PIN_ENABLED = "PIN_ENABLED";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preference, container, false);

        this.pinSwitch = (Switch) view.findViewById(R.id.settings_pin_switch);
        this.pinSwitch.setChecked(Prefs.getBoolean(PIN_ENABLED, false));
        TextView version = (TextView) view.findViewById(R.id.preference_version);
        version.setText(BuildConfig.VERSION_NAME);

        TextView account = (TextView) view.findViewById(R.id.preference_account);
        account.setText(Subterminal.getUser().getEmail());

        TextView premium = (TextView) view.findViewById(R.id.preference_account_premium_value);
        premium.setText(Boolean.toString(Subterminal.getUser().isPremium()));

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.getActivity())
                        .setTitle("Confirm log out")
                        .setMessage("Are you sure you wish to log out?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Subterminal.getUser().logOut();
                                UIHelper.goToFragment(R.id.nav_settings);
                                Toast.makeText(MainActivity.getActivity(), "You have logged out", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        //Show the changelog
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeLog cl = new ChangeLog(MainActivity.getActivity());
                cl.getFullLogDialog().show();
            }
        });

        pinSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    LockManager.getInstance().enableAppLock(
                            MainActivity.getActivity().getApplicationContext(),
                            CustomPinActivity.class);

                    LockManager.getInstance().getAppLock().setShouldShowForgot(false);

                    Intent intent = new Intent(MainActivity.getActivity().getApplicationContext(), CustomPinActivity.class);
                    intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
                    startActivity(intent);
                } else {
                    LockManager.getInstance().getAppLock().disableAndRemoveConfiguration();
                }

                Prefs.putBoolean(PIN_ENABLED, isChecked);
            }
        });

        return view;
    }


    @Override
    protected String getItemClass() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        String title = getString(R.string.title_settings);
        MainActivity.getActivity().setTitle(title);
    }
}
