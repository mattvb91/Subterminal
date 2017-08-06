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
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Skydive.Aircraft;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
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
    public static final String PREFS_DEFAULT_AIRCRAFT = "PREFS_DEFAULT_AIRCRAFT";
    public static final String PREFS_DEFAULT_DROPZONE = "PREFS_DEFAULT_DROPZONE";
    public static final String PREFS_DEFAULT_SKYDIVE_TYPE = "PREFS_DEFAULT_SKYDIVE_TYPE";
    public static final String PREFS_DEFAULT_HEIGHT_UNIT = "PREFS_DEFAULT_HEIGHT_UNIT";
    public static final String PREFS_MODE = "PREFS_MODE";

    public static final String PREFS_SKYDIVE_START_COUNT = "PREFS_SKYDIVE_START_COUNT";

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
        startCount.setText(Integer.toString(Subterminal.getUser().getSettings().getBaseStartJumpNo()));

        startCount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Subterminal.getUser().getSettings().setBaseStartJumpNo(Integer.parseInt(String.valueOf(startCount.getText())));

                    InputMethodManager imm = (InputMethodManager) MainActivity.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        final EditText skydiveStartCount = (EditText) view.findViewById(R.id.preference_skydive_start_count);
        skydiveStartCount.setText(Integer.toString(Subterminal.getUser().getSettings().getSkydiveStartJumpNo()));

        skydiveStartCount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Subterminal.getUser().getSettings().setSkydiveStartJumpNo(Integer.parseInt(String.valueOf(skydiveStartCount.getText())));

                    InputMethodManager imm = (InputMethodManager) MainActivity.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        skydiveStartCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    Subterminal.getUser().getSettings().setSkydiveStartJumpNo(Integer.parseInt(String.valueOf(skydiveStartCount.getText())));
                }
            }
        });

        startCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    Subterminal.getUser().getSettings().setBaseStartJumpNo(Integer.parseInt(String.valueOf(startCount.getText())));
                }
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
        sliderConfigSpinner.setSelection(Subterminal.getUser().getSettings().getBaseDefaultSliderConfig(), false);
        sliderConfigSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Subterminal.getUser().getSettings().setBaseDefaultSliderConfig(sliderConfigSpinner.getSelectedItemPosition());
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
        jumpTypeSpinner.setSelection(Subterminal.getUser().getSettings().getBaseDefaultJumpType(), false);
        jumpTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Subterminal.getUser().getSettings().setBaseDefaultJumpType(jumpTypeSpinner.getSelectedItemPosition());
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
        pcSize.setSelection(Arrays.asList(Jump.getPcSizeArray()).indexOf(Subterminal.getUser().getSettings().getBaseDefaultPcSize()), false);
        pcSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Subterminal.getUser().getSettings().setBaseDefaultPcSize(Integer.parseInt(pcSize.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /**
         * End PC Size Spinner
         */

        /**
         * Aircraft Spinner
         */
        final Spinner aircraftSpinner = (Spinner) view.findViewById(R.id.preference_default_aircraft_value);

        final LinkedHashMapAdapter aircraftAdapter = new LinkedHashMapAdapter<String, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, new Aircraft().getItemsForSelect("name"));
        aircraftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aircraftSpinner.setAdapter(aircraftAdapter);
        aircraftSpinner.setSelection(aircraftAdapter.findPositionFromKey(Subterminal.getUser().getSettings().getSkydiveDefaultAircraftId()), false);
        aircraftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Subterminal.getUser().getSettings().setSkydiveDefaultAircraftId(Integer.parseInt(aircraftAdapter.getItem(i).getKey().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /**
         * End Aircraft Spinner
         */

        /**
         * Dropzone AutoCompleteTextView
         */
        final AutoCompleteTextView dropzone = (AutoCompleteTextView) view.findViewById(R.id.preference_default_dz_value);
        final LinkedHashMapAdapter dropzoneAdapter = new LinkedHashMapAdapter<String, String>(MainActivity.getActivity().getApplicationContext(), R.layout.item_simple, new Dropzone().getItemsForSelect("name"), LinkedHashMapAdapter.FLAG_FILTER_ON_VALUE);
        dropzoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropzone.setAdapter(dropzoneAdapter);
        dropzone.setThreshold(2);
        dropzone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subterminal.getUser().getSettings().setSkydiveHomeDzId(Integer.parseInt(dropzoneAdapter.getItem(position).getKey().toString()));
                dropzone.setText(dropzoneAdapter.getItem(position).getValue().toString());
            }
        });

        if (Subterminal.getUser().getSettings().getSkydiveHomeDzId() != null) {
            Dropzone dbDropzone = (Dropzone) new Dropzone().getOneById(Subterminal.getUser().getSettings().getSkydiveHomeDzId());
            dropzone.setText(dbDropzone.getName());
        }
        /**
         * End Dropzone AutoCompleteTextView
         */


        /**
         * Jump type Spinner
         */
        Spinner jumpType = (Spinner) view.findViewById(R.id.preference_default_skydive_type_value);
        final LinkedHashMapAdapter jumpTypesAdapter = new LinkedHashMapAdapter<Integer, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, Skydive.getJumpTypes());
        jumpTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jumpType.setAdapter(jumpTypesAdapter);
        jumpType.setSelection(jumpTypesAdapter.findPositionFromKey(Subterminal.getUser().getSettings().getSkydiveDefaultJumpType()), false);

        jumpType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Subterminal.getUser().getSettings().setSkydiveDefaultJumpType((Integer) jumpTypesAdapter.getItem(position).getKey());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * Jump type Spinner
         */


        RadioGroup heightUnit = (RadioGroup) view.findViewById(R.id.height_unit_radio_group);
        UIHelper.prefillHeightUnit(heightUnit);

        heightUnit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(R.id.radio_metric);
                Subterminal.getUser().getSettings().setDefaultHeightUnit(radioButton.isChecked() ? Subterminal.HEIGHT_UNIT_METRIC : Subterminal.HEIGHT_UNIT_IMPERIAL);
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
