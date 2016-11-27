package mavonie.subterminal.Forms;

import android.app.DatePickerDialog;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Gear;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Preference;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
import mavonie.subterminal.Utils.Date.DateFormat;
import mavonie.subterminal.Utils.Subterminal;


/**
 * Jump form
 */
public class JumpForm extends BaseForm implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private AutoCompleteTextView exitNameAutoComplete;
    private Spinner rig;
    private Spinner pilotChute;
    private Spinner sliderConfig;
    private Spinner jumpTypeSpinner;
    private Spinner suitSpinner;
    private TextView delay;
    private TextView description;
    private TextView date;

    private LinkedHashMap<String, String> exitNames;
    LinkedHashMapAdapter<String, String> exitsAdapter;

    private LinkedHashMap<String, String> gear;
    LinkedHashMapAdapter<String, String> gearAdapter;

    private LinkedHashMap suits;
    LinkedHashMapAdapter suitsAdapter = null;

    @Override
    protected String getItemClass() {
        return Jump.class.getCanonicalName();
    }

    @Override
    protected int getParentFragmentId() {
        return R.id.nav_jumps;
    }

    @Override
    protected void assignFormElements(View view) {

        this.exitNameAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.jump_edit_exit_name);
        this.rig = (Spinner) view.findViewById(R.id.jump_edit_gear);
        this.pilotChute = (Spinner) view.findViewById(R.id.jump_edit_pc_size);
        this.sliderConfig = (Spinner) view.findViewById(R.id.jump_edit_slider);
        this.delay = (TextView) view.findViewById(R.id.jump_edit_delay);
        this.description = (TextView) view.findViewById(R.id.jump_edit_description);

        this.exitNames = new Exit().getItemsForSelect("name");

        this.exitsAdapter = new LinkedHashMapAdapter<String, String>(MainActivity.getActivity().getApplicationContext(), R.layout.item_simple, this.exitNames, LinkedHashMapAdapter.FLAG_FILTER_ON_VALUE);
        exitNameAutoComplete.setAdapter(this.exitsAdapter);
        exitNameAutoComplete.setThreshold(2);
        exitNameAutoComplete.setOnItemClickListener(this);

        Spinner gearSpinner = (Spinner) view.findViewById(R.id.jump_edit_gear);
        this.gear = new Gear().getItemsForSelect("container_manufacturer");

        if (this.gear.size() > 0) {
            this.gearAdapter = new LinkedHashMapAdapter<String, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, this.gear);
            this.gearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gearSpinner.setAdapter(this.gearAdapter);
            gearSpinner.setOnItemSelectedListener(this);
        } else {
            view.findViewById(R.id.jump_edit_gear_text).setVisibility(View.GONE);
            gearSpinner.setVisibility(View.GONE);
        }

        //SUIT SPINNER
        this.suitSpinner = (Spinner) view.findViewById(R.id.jump_edit_suit);
        this.suits = new Suit().getItemsForSpinner(-1);

        if (this.suits.size() > 0) {
            this.suitsAdapter = new LinkedHashMapAdapter<Integer, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, this.suits);
            this.suitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.suitSpinner.setAdapter(suitsAdapter);
        } else {
            this.suitSpinner.setVisibility(View.GONE);
        }
        //END SUIT SPINNER

        Spinner pcSizeSpinner = (Spinner) view.findViewById(R.id.jump_edit_pc_size);
        ArrayAdapter<Integer> pcSizeAdapter = new ArrayAdapter<Integer>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, Jump.getPcSizeArray());
        pcSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pcSizeSpinner.setAdapter(pcSizeAdapter);
        pcSizeSpinner.setSelection(Arrays.asList(Jump.getPcSizeArray()).indexOf(Prefs.getInt(Preference.PREFS_DEFAULT_PC, 32)), false);


        Spinner sliderConfigSpinner = (Spinner) view.findViewById(R.id.jump_edit_slider);
        ArrayAdapter<String> sliderAdapter =
                new ArrayAdapter<String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item,
                        Jump.getSliderConfigArray()
                );

        sliderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sliderConfigSpinner.setAdapter(sliderAdapter);
        sliderConfigSpinner.setSelection(Prefs.getInt(Preference.PREFS_DEFAULT_SLIDER, Jump.SLIDER_DOWN), false);

        jumpTypeSpinner = (Spinner) view.findViewById(R.id.jump_edit_type);
        ArrayAdapter<String> typeAdapter =
                new ArrayAdapter<String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item,
                        Jump.getTypeArray()
                );

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jumpTypeSpinner.setAdapter(typeAdapter);
        jumpTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Integer type = convertTypes(i);

                if (type != null) {
                    suits = new Suit().getItemsForSpinner(type);

                    if (suits.size() > 0) {
                        suitSpinner.setVisibility(View.VISIBLE);

                        if (suitsAdapter == null) {
                            suitsAdapter = new LinkedHashMapAdapter<String, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, suits);
                            suitSpinner.setAdapter(suitsAdapter);
                        }
                        suitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        jumpTypeSpinner.setSelection(Prefs.getInt(Preference.PREFS_DEFAULT_JUMP_TYPE, Jump.TYPE_SLICK));

        this.date = (EditText) view.findViewById(R.id.jump_edit_date);
        DateFormat df = new DateFormat();
        this.date.setText(df.format(myCalendar.getTime()));

        final DatePickerDialog.OnDateSetListener jumpDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };

        this.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), jumpDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        Button button = (Button) view.findViewById(R.id.jump_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });
    }

    private Integer convertTypes(int i) {
        Integer type = null;
        if (i == Jump.TYPE_TRACKING) {
            type = Suit.TYPE_TRACKING;
        } else if (i == Jump.TYPE_WINGSUIT) {
            type = Suit.TYPE_WINGSUIT;
        }
        return type;
    }

    private void updateDate() {

        DateFormat df = new DateFormat();
        this.date.setText(df.format(myCalendar.getTime()));
    }

    public void save() {

        if (validateForm()) {

            int exitId;

            if (this.exitEntry == null) {
                String exitName = exitNameAutoComplete.getText().toString().trim();

                Exit exit = (Exit) new Exit().getItem(new Pair<String, String>("name", exitName));

                if (exit == null) {
                    exit = new Exit();
                    exit.setName(exitName);
                    exit.save();
                }

                exitId = exit.getId();
            } else {
                exitId = Integer.parseInt(this.exitEntry.getKey());
            }

            String pilotChuteSize = pilotChute.getSelectedItem().toString();
            long sliderConfigID = sliderConfig.getSelectedItemId();
            String delayString = delay.getText().toString();
            String descriptionString = description.getText().toString();

            getItem().setExit_id(exitId);

            if (this.gearEntry != null) {
                getItem().setGear_id(Integer.parseInt(this.gearEntry.getKey()));
            }

            getItem().setPc_size(Integer.parseInt(pilotChuteSize));
            getItem().setSlider(Integer.parseInt(Long.toString(sliderConfigID)));
            getItem().setDate(date.getText().toString());

            if (delayString.length() > 0) {
                getItem().setDelay(Integer.parseInt(delayString));
            }

            if (suitSpinner.getVisibility() == View.VISIBLE) {
                getItem().setSuit_id(Integer.parseInt(this.suitEntry.getKey()));
            } else {
                getItem().setSuit_id(null);
            }

            getItem().setDescription(descriptionString);
            getItem().setType(Integer.parseInt(Long.toString(jumpTypeSpinner.getSelectedItemId())));

            super.save();
        }
    }

    @Override
    protected Jump getItem() {
        return (Jump) super.getItem();
    }

    @Override
    protected void updateForm() {
        if (getItem().exists()) {
            Subterminal.setActiveModel(getItem());

            if (getItem().getExit() != null) {
                this.exitNameAutoComplete.setText(getItem().getExit().getName());
            }

            this.date.setText(getItem().getDate());
            this.pilotChute.setSelection(Arrays.asList(Jump.getPcSizeArray()).indexOf(getItem().getPc_size()));
            this.jumpTypeSpinner.setSelection(getItem().getType(), false);

            this.suits = new Suit().getItemsForSpinner(convertTypes(getItem().getType()));
            this.suitsAdapter = new LinkedHashMapAdapter<String, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, suits);
            this.suitsAdapter.notifyDataSetChanged();
            if (getItem().getSuit_id() != null) {
                suitSpinner.setAdapter(this.suitsAdapter);
                suitSpinner.setSelection(this.suitsAdapter.findPositionFromKey(getItem().getSuit_id()), false);
            }

            this.delay.setText(Integer.toString(getItem().getDelay()));
            this.description.setText(getItem().getDescription());

            if (getItem().getGear_id() != null) {
                Integer position = this.gearAdapter.findPositionFromKey(getItem().getGear_id());
                if (position != null) {
                    this.rig.setSelection(position);
                }
            }

            this.sliderConfig.setSelection(getItem().getSlider());
        }
    }

    @Override
    protected boolean validateForm() {
        boolean valid = true;

        String exitName = exitNameAutoComplete.getText().toString();
        exitName = exitName.trim();

        if (exitName.length() == 0) {
            this.exitNameAutoComplete.setError("Exit required");
            valid = false;
        }

        return valid;
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_jump_form;
    }

    private Map.Entry<String, String> exitEntry;
    private Map.Entry<String, String> gearEntry;
    private Map.Entry<String, String> suitEntry;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        exitEntry = this.exitsAdapter.getItem(position);
        exitNameAutoComplete.setText(exitEntry.getValue());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        gearEntry = this.gearAdapter.getItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
