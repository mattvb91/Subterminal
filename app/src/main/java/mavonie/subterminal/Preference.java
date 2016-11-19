package mavonie.subterminal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import de.cketti.library.changelog.ChangeLog;
import jonathanfinerty.once.Once;
import mavonie.subterminal.Models.*;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;


public class Preference extends BaseFragment {

    Switch pinSwitch;

    public static final String PIN_ENABLED = "PIN_ENABLED";
    public static final String PREFS_JUMP_START_COUNT = "PREFS_JUMP_START_COUNT";
    public static final String FORCE_SYNC_ENTITIES = "FORCE_SYNC_ENTITIES";
    public static final String PREFS_DEFAULT_SLIDER = "PREFS_DEFAULT_SLIDER";
    public static final String PREFS_DEFAULT_PC = "PREFS_DEFAULT_PC";
    public static final String PREFS_DEFAULT_JUMP_TYPE = "PREFS_DEFAULT_JUMP_TYPE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_preference, container, false);

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

        TextView dataSync = (TextView) view.findViewById(R.id.preference_account_sync_value);

        if (Subterminal.getUser().isPremium()) {

            dataSync.setText(R.string.force_sync);
            dataSync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Subterminal.getUser().isLoggedIn()) {
                        if (!Once.beenDone(TimeUnit.MINUTES, 1, FORCE_SYNC_ENTITIES)) {

                            UIHelper.loadSpinner();
                            Synchronizable.forceSyncAll();
                            Once.markDone(FORCE_SYNC_ENTITIES);
                            UIHelper.removeLoadSpinner();

                        }
                    } else {
                        UIHelper.toast(getString(R.string.must_be_logged_in_action));
                    }
                }
            });
        }

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

        final EditText startCount = (EditText) view.findViewById(R.id.preference_start_count);
        startCount.setText(Integer.toString(Prefs.getInt(PREFS_JUMP_START_COUNT, 0)));

        startCount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Prefs.putInt(PREFS_JUMP_START_COUNT, Integer.parseInt(String.valueOf(startCount.getText())));
                    UIHelper.toast(getString(R.string.settings_updated));

                    InputMethodManager imm = (InputMethodManager) MainActivity.getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        /**
         * Slider Spinner
         */
        final Spinner sliderConfigSpinner = (Spinner) view.findViewById(R.id.preference_default_slider_config_value);
        ArrayAdapter<String> sliderAdapter =
                new ArrayAdapter<String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item,
                        Jump.getSliderConfigArray()
                );

        sliderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sliderConfigSpinner.setAdapter(sliderAdapter);
        sliderConfigSpinner.setSelection(Prefs.getInt(PREFS_DEFAULT_SLIDER, Jump.SLIDER_DOWN), false);
        sliderConfigSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Prefs.putInt(PREFS_DEFAULT_SLIDER, sliderConfigSpinner.getSelectedItemPosition());
                UIHelper.toast(getString(R.string.settings_updated));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /**
         * End Slider Spinner
         */

        /**
         * Jump type Spinner
         */
        final Spinner jumpTypeSpinner = (Spinner) view.findViewById(R.id.preference_default_jump_type_value);
        ArrayAdapter<String> jumpTypeAdapter =
                new ArrayAdapter<String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item,
                        Jump.getTypeArray()
                );

        jumpTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jumpTypeSpinner.setAdapter(jumpTypeAdapter);
        jumpTypeSpinner.setSelection(Prefs.getInt(PREFS_DEFAULT_JUMP_TYPE, Jump.TYPE_SLICK), false);
        jumpTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Prefs.putInt(PREFS_DEFAULT_JUMP_TYPE, jumpTypeSpinner.getSelectedItemPosition());
                UIHelper.toast(getString(R.string.settings_updated));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /**
         * End Jump type Spinner
         */


        /**
         * PC Size Spinner
         */
        final Spinner pcSize = (Spinner) view.findViewById(R.id.preference_default_pc_size_value);
        ArrayAdapter<Integer> pcApter =
                new ArrayAdapter<Integer>(MainActivity.getActivity(), android.R.layout.simple_spinner_item,
                        Jump.getPcSizeArray()
                );


        pcApter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pcSize.setAdapter(pcApter);
        pcSize.setSelection(Arrays.asList(Jump.getPcSizeArray()).indexOf(Prefs.getInt(PREFS_DEFAULT_PC, 32)), false);
        pcSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Prefs.putInt(PREFS_DEFAULT_PC, Integer.parseInt(pcSize.getSelectedItem().toString()));
                UIHelper.toast(getString(R.string.settings_updated));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /**
         * End PC Size Spinner
         */

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
