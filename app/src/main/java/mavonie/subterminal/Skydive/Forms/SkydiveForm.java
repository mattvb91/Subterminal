package mavonie.subterminal.Skydive.Forms;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import mavonie.subterminal.Forms.BaseForm;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Skydive.Aircraft;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Models.Skydive.Rig;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
import mavonie.subterminal.Utils.Date.DateFormat;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;


/**
 * Skydive form
 */
public class SkydiveForm extends BaseForm implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private AutoCompleteTextView dropzone;
    private Spinner rigSpinner,
            jumpType,
            aircraftSpinner,
            suitSpinner;

    private TextView delay,
            description,
            date,
            exit_altitude,
            deploy_altitude;

    private CheckBox cutaway;

    private RadioGroup heightUnit;

    private LinkedHashMap<String, String> dropzoneNames;
    LinkedHashMapAdapter<String, String> dropzonesAdapter;

    private LinkedHashMap<String, String> rigs;
    LinkedHashMapAdapter<String, String> rigAdapter;

    private LinkedHashMap suits = new LinkedHashMap();
    LinkedHashMapAdapter suitsAdapter = new LinkedHashMapAdapter<Integer, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, this.suits);

    private LinkedHashMap<String, String> aircrafts;
    LinkedHashMapAdapter<String, String> aircraftAdapter;

    LinkedHashMapAdapter<Integer, String> jumpTypesAdapter;

    @Override
    protected String getItemClass() {
        return Skydive.class.getCanonicalName();
    }

    @Override
    protected int getParentFragmentId() {
        return R.id.skydiving_nav_jumps;
    }

    @Override
    protected void assignFormElements(View view) {

        dropzone = (AutoCompleteTextView) view.findViewById(R.id.skydive_edit_dropzone);
        delay = (TextView) view.findViewById(R.id.skydive_edit_delay);
        description = (TextView) view.findViewById(R.id.skydive_edit_description);
        exit_altitude = (TextView) view.findViewById(R.id.skydive_edit_altitude);
        deploy_altitude = (TextView) view.findViewById(R.id.skydive_edit_deploy_altitude);
        date = (EditText) view.findViewById(R.id.skydive_edit_date);
        rigSpinner = (Spinner) view.findViewById(R.id.skydive_edit_rig);
        cutaway = (CheckBox) view.findViewById(R.id.skydive_edit_cutaway_checkbox);

        dropzoneNames = new Dropzone().getItemsForSelect("name");
        dropzonesAdapter = new LinkedHashMapAdapter<String, String>(MainActivity.getActivity().getApplicationContext(), R.layout.item_simple, dropzoneNames, LinkedHashMapAdapter.FLAG_FILTER_ON_VALUE);

        dropzone.setAdapter(dropzonesAdapter);
        dropzone.setThreshold(2);
        dropzone.setOnItemClickListener(this);

        if (Subterminal.getUser().getSettings().getSkydiveHomeDzId() != null) {
            Dropzone dz = (Dropzone) new Dropzone().getOneById(Subterminal.getUser().getSettings().getSkydiveHomeDzId());
            this.dropzone.setText(dz.getName());
            this.dropzoneEntry = this.dropzonesAdapter.getItem(this.dropzonesAdapter.findPositionFromKey(dz.getId()));
        }

        DateFormat df = new DateFormat();
        date.setText(df.format(calendar.getTime()));

        final DatePickerDialog.OnDateSetListener jumpDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };

        this.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), jumpDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Spinner rigSpinner = (Spinner) view.findViewById(R.id.skydive_edit_rig);
        this.rigs = new Rig().getItemsForSelect("container_manufacturer", true);

        if (this.rigs.size() > 1) {
            this.rigAdapter = new LinkedHashMapAdapter<String, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, this.rigs);
            this.rigAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rigSpinner.setAdapter(this.rigAdapter);

            rigSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (rigAdapter.getItem(i).getKey() != null) {
                        rigEntry = rigAdapter.getItem(i);
                    } else {
                        rigEntry = null;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } else {
            view.findViewById(R.id.skydive_edit_rig_text).setVisibility(View.GONE);
            rigSpinner.setVisibility(View.GONE);
        }

        aircraftSpinner = (Spinner) view.findViewById(R.id.skydive_edit_aircraft);
        aircraftAdapter = new LinkedHashMapAdapter<String, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, new Aircraft().getItemsForSelect("name"));
        aircraftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aircraftSpinner.setAdapter(aircraftAdapter);

        if (aircraftAdapter.getCount() > 0)
            aircraftSpinner.setSelection(aircraftAdapter.findPositionFromKey(Subterminal.getUser().getSettings().getSkydiveDefaultAircraftId()));

        aircraftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aircraftEntry = aircraftAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jumpType = (Spinner) view.findViewById(R.id.skydive_edit_type);
        jumpTypesAdapter = new LinkedHashMapAdapter<Integer, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, Skydive.getJumpTypes());
        jumpTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jumpType.setAdapter(jumpTypesAdapter);
        jumpType.setSelection(jumpTypesAdapter.findPositionFromKey(Subterminal.getUser().getSettings().getSkydiveDefaultJumpType()));
        jumpType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jumpTypeEntry = jumpTypesAdapter.getItem(position);

                Integer type = convertTypes(jumpTypesAdapter.getItem(position).getKey());

                if (type != null) {
                    suits.clear();
                    suits.putAll(new Suit().getItemsForSpinner(type));

                    if (suits.size() > 0) {
                        suitSpinner.setVisibility(View.VISIBLE);
                        suitsAdapter.notifyDataSetChanged();
                        suitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                suitEntry = suitsAdapter.getItem(i);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        suitSpinner.setVisibility(View.GONE);
                        suitEntry = null;
                    }
                } else {
                    suitSpinner.setVisibility(View.GONE);
                    suitEntry = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //SUIT SPINNER
        this.suitSpinner = (Spinner) view.findViewById(R.id.skydive_edit_suit);
        this.suitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.suitSpinner.setAdapter(suitsAdapter);

        if (this.suits.size() == 0) {
            this.suitSpinner.setVisibility(View.GONE);
        }
        //END SUIT SPINNER

        heightUnit = (RadioGroup) view.findViewById(R.id.height_unit_radio_group);
        UIHelper.prefillHeightUnit(heightUnit);

        Button button = (Button) view.findViewById(R.id.skydive_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });
    }

    private Integer convertTypes(int i) {
        Integer type = null;
        if (i == Skydive.SKYDIVE_TYPE_TRACKING) {
            type = Suit.TYPE_TRACKING;
        } else if (i == Skydive.SKYDIVE_TYPE_WINGSUIT) {
            type = Suit.TYPE_WINGSUIT;
        }
        return type;
    }

    private void updateDate() {

        DateFormat df = new DateFormat();
        this.date.setText(df.format(calendar.getTime()));
    }

    public void save() {

        if (validateForm()) {
            if (dropzoneEntry != null) {
                getItem().setDropzoneId(Integer.parseInt(dropzoneEntry.getKey()));
            }

            if (rigEntry != null) {
                getItem().setRigId(Integer.parseInt(rigEntry.getKey()));
            } else {
                getItem().setRigId(null);
            }

            if (aircraftEntry != null) {
                getItem().setAircraftId(Integer.parseInt(aircraftEntry.getKey()));
            }

            if (jumpTypeEntry != null) {
                getItem().setJumpType(jumpTypeEntry.getKey());
            }

            //Height unit check
            RadioButton radioButton = (RadioButton) getView().findViewById(R.id.radio_metric);
            if (radioButton.isChecked()) {
                getItem().setHeightUnit(Subterminal.HEIGHT_UNIT_METRIC);
            } else {
                getItem().setHeightUnit(Subterminal.HEIGHT_UNIT_IMPERIAL);
            }

            getItem().setCutaway(cutaway.isChecked() ? Skydive.CUTAWAY_YES : Skydive.CUTAWAY_NO);

            String delayString = delay.getText().toString();
            String descriptionString = description.getText().toString();
            String exitAltitude = exit_altitude.getText().toString();
            String deployAltitude = deploy_altitude.getText().toString();

            getItem().setDate(date.getText().toString());

            if (suitSpinner.getVisibility() == View.VISIBLE) {
                getItem().setSuitId(Integer.parseInt(this.suitEntry.getKey()));
            } else {
                getItem().setSuitId(null);
            }

            if (exitAltitude.length() > 0) {
                getItem().setExitAltitude(Integer.parseInt(exit_altitude.getText().toString()));
            }

            if (deployAltitude.length() > 0) {
                getItem().setDeployAltitude(Integer.parseInt(deploy_altitude.getText().toString()));
            }

            if (delayString.length() > 0) {
                getItem().setDelay(Integer.parseInt(delayString));
            }

            getItem().setDescription(descriptionString);

            super.save();
        }
    }

    @Override
    protected Skydive getItem() {
        return (Skydive) super.getItem();
    }

    @Override
    protected void updateForm() {
        if (getItem().exists()) {
            Subterminal.setActiveModel(getItem());

            if (getItem().getDropzoneId() != null) {
                this.dropzone.setText(getItem().getDropzone().getName());
                this.dropzoneEntry = this.dropzonesAdapter.getItem(this.dropzonesAdapter.findPositionFromKey(getItem().getDropzoneId()));
            }

            date.setText(getItem().getDate());

            if (getItem().getDelay() != null) {
                delay.setText(Integer.toString(getItem().getDelay()));
            }

            description.setText(getItem().getDescription());

            if (getItem().getHeightUnit() == Subterminal.HEIGHT_UNIT_IMPERIAL) {
                heightUnit.check(R.id.radio_imperial);
            } else {
                heightUnit.check(R.id.radio_metric);
            }

            if (getItem().getCutaway() == Skydive.CUTAWAY_YES) {
                cutaway.setChecked(true);
            }

            if (getItem().getExitAltitude() != null) {
                exit_altitude.setText(Integer.toString(getItem().getExitAltitude()));
            }

            if (getItem().getJumpType() != null) {
                jumpType.setSelection(jumpTypesAdapter.findPositionFromKey(getItem().getJumpType()));
            }

            this.suits.clear();

            if (getItem().getJumpType() != null) {
                this.suits.putAll(new Suit().getItemsForSpinner(convertTypes(getItem().getJumpType())));
                this.suitsAdapter.notifyDataSetChanged();
            }
            suitSpinner.setAdapter(this.suitsAdapter);
            if (getItem().getSuit() != null) {
                suitEntry = suitsAdapter.getItem(this.suitsAdapter.findPositionFromKey(getItem().getSuit().getId()));
                suitSpinner.setSelection(this.suitsAdapter.findPositionFromKey(getItem().getSuit().getId()), false);
            }

            if (getItem().getDeployAltitude() != null) {
                deploy_altitude.setText(Integer.toString(getItem().getDeployAltitude()));
            }

            if (getItem().getRigId() != null) {
                Integer position = this.rigAdapter.findPositionFromKey(getItem().getRigId());
                if (position != null) {
                    this.rigSpinner.setSelection(position);
                }
            }

            if (getItem().getAircraftId() != null) {
                this.aircraftSpinner.setSelection(this.aircraftAdapter.findPositionFromKey(getItem().getAircraftId()));
            }
        }
    }

    @Override
    protected boolean validateForm() {
        boolean valid = true;

        return valid;
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_skydive_form;
    }

    private Map.Entry<String, String> dropzoneEntry;
    private Map.Entry<String, String> rigEntry;
    private Map.Entry<String, String> aircraftEntry;
    private Map.Entry<Integer, String> jumpTypeEntry;
    private Map.Entry<String, String> suitEntry;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        dropzoneEntry = this.dropzonesAdapter.getItem(position);
        dropzone.setText(dropzoneEntry.getValue());

        //Check if the dropzone has associated aircrafts
        //If it has then prefil the aircraft for us
        Dropzone dropzone = (Dropzone) new Dropzone().getOneById(Integer.parseInt(dropzoneEntry.getKey()));
        if (dropzone.getAircraft().size() > 0) {
            aircraftSpinner.setSelection(aircraftAdapter.findPositionFromKey(dropzone.getAircraft().get(0).getId()));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dropzoneEntry = this.dropzonesAdapter.getItem(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
