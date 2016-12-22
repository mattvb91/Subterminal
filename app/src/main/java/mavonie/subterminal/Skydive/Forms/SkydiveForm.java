package mavonie.subterminal.Skydive.Forms;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import mavonie.subterminal.Forms.BaseForm;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
import mavonie.subterminal.Utils.Date.DateFormat;
import mavonie.subterminal.Utils.Subterminal;


/**
 * Skydive form
 */
public class SkydiveForm extends BaseForm implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private AutoCompleteTextView dropzone;
    private Spinner jumpType;

    private TextView delay,
            description,
            date,
            exit_altitude,
            deploy_altitude;

    private LinkedHashMap<String, String> dropzoneNames;
    LinkedHashMapAdapter<String, String> dropzonesAdapter;

    private LinkedHashMap suits = new LinkedHashMap();
    LinkedHashMapAdapter suitsAdapter = new LinkedHashMapAdapter<Integer, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, this.suits);

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

        dropzoneNames = new Dropzone().getItemsForSelect("name");
        dropzonesAdapter = new LinkedHashMapAdapter<String, String>(MainActivity.getActivity().getApplicationContext(), R.layout.item_simple, dropzoneNames, LinkedHashMapAdapter.FLAG_FILTER_ON_VALUE);

        dropzone.setAdapter(dropzonesAdapter);
        dropzone.setThreshold(2);
        dropzone.setOnItemClickListener(this);

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


        Button button = (Button) view.findViewById(R.id.skydive_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });
    }

    private void updateDate() {

        DateFormat df = new DateFormat();
        this.date.setText(df.format(calendar.getTime()));
    }

    public void save() {

        if (validateForm()) {
            if (dropzoneEntry != null) {
                getItem().setDropzoneId(dropzoneEntry.getValue());
            }

            String delayString = delay.getText().toString();
            String descriptionString = description.getText().toString();

            getItem().setDate(date.getText().toString());
            getItem().setExitAltitude(Integer.parseInt(exit_altitude.getText().toString()));
            getItem().setDeployAltitude(Integer.parseInt(deploy_altitude.getText().toString()));

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
            }

            date.setText(getItem().getDate());

            if (getItem().getDelay() != null) {
                delay.setText(Integer.toString(getItem().getDelay()));
            }

            description.setText(getItem().getDescription());

            if (getItem().getExitAltitude() != null) {
                exit_altitude.setText(Integer.toString(getItem().getExitAltitude()));
            }

            if (getItem().getExitAltitude() != null) {
                deploy_altitude.setText(Integer.toString(getItem().getDeployAltitude()));
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        dropzoneEntry = this.dropzonesAdapter.getItem(position);
        dropzone.setText(dropzoneEntry.getValue());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dropzoneEntry = this.dropzonesAdapter.getItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}