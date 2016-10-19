package mavonie.subterminal.Forms;

import android.app.DatePickerDialog;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Date.DateFormat;
import mavonie.subterminal.models.Exit;
import mavonie.subterminal.models.Gear;
import mavonie.subterminal.models.Jump;

/**
 * Created by mavon on 15/10/16.
 */

public class JumpForm extends BaseForm {

    private AutoCompleteTextView exitNameAutoComplete;
    private Spinner rig;
    private Spinner pilotChute;
    private Spinner sliderConfig;
    private TextView delay;
    private TextView description;
    private TextView date;

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

        ArrayList exitNames = new Exit().getItemsForSelectArray("name");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.getActivity().getApplicationContext(), R.layout.item_simple, exitNames);
        exitNameAutoComplete.setAdapter(adapter);
        exitNameAutoComplete.setAdapter(adapter);
        exitNameAutoComplete.setThreshold(2);

        Spinner gearSpinner = (Spinner) view.findViewById(R.id.jump_edit_gear);
        ArrayList gearNames = new Gear().getItemsForSelectArray("container_manufacturer");
        ArrayAdapter<String> gearAdapter = new ArrayAdapter<String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, gearNames);
        gearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gearSpinner.setAdapter(gearAdapter);

        Spinner pcSizeSpinner = (Spinner) view.findViewById(R.id.jump_edit_pc_size);
        ArrayAdapter<Integer> pcSizeAdapter = new ArrayAdapter<Integer>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, new Jump().getPcSizeArray());
        pcSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pcSizeSpinner.setAdapter(pcSizeAdapter);

        Spinner sliderConfigSpinner = (Spinner) view.findViewById(R.id.jump_edit_slider);
        ArrayAdapter<String> sliderAdapter =
                new ArrayAdapter<String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item,
                        new Jump().getSliderConfigArray()
                );

        sliderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sliderConfigSpinner.setAdapter(sliderAdapter);

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

    private void updateDate() {

        DateFormat df = new DateFormat();
        this.date.setText(df.format(myCalendar.getTime()));
    }

    public void save() {

        if (validateForm()) {
            String exitName = exitNameAutoComplete.getText().toString();

            Exit exit = (Exit) new Exit().getItem(new Pair<String, String>("name", exitName));

            if (exit == null) {
                exit = new Exit();
                exit.setName(exitName);
                exit.save();
            }

            long rigId = rig.getSelectedItemId();
            long pilotChuteId = pilotChute.getSelectedItemId();
            long sliderConfigID = sliderConfig.getSelectedItemId();
            String delayString = delay.getText().toString();
            String descriptionString = description.getText().toString();

            getItem().setExit_id(exit.getId());

            if (rigId > 0) {
                getItem().setGear_id(Integer.parseInt(Long.toString(rigId)));
            }

            getItem().setPc_size(Integer.parseInt(Long.toString(pilotChuteId)));
            getItem().setSlider(Integer.parseInt(Long.toString(sliderConfigID)));
            getItem().setDate(date.getText().toString());

            if (delayString.length() > 0) {
                getItem().setDelay(Integer.parseInt(delayString));
            }

            getItem().setDescription(descriptionString);

            super.save();
        }
    }

    @Override
    protected Jump getItem() {
        return (Jump) super.getItem();
    }

    @Override
    protected void updateForm() {

    }

    @Override
    protected boolean validateForm() {
        boolean valid = true;

        if (exitNameAutoComplete.getText().length() == 0) {
            this.exitNameAutoComplete.setError("Exit required");
            valid = false;
        }

        return valid;
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_jump_form;
    }
}
