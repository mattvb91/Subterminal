package mavonie.subterminal.Forms;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Date.DateFormat;
import mavonie.subterminal.Utils.Subterminal;

public class SuitForm extends BaseForm {

    private Spinner suitType;

    private EditText suitModel,
            suitSerial,
            suitDateInUse,
            suitManufacturer;

    @Override
    protected Suit getItem() {
        return (Suit) super.getItem();
    }


    protected void updateForm() {
        if (getItem().exists()) {
            Subterminal.setActiveModel(getItem());
            this.suitManufacturer.setText(getItem().getManufacturer());
            this.suitType.setSelection(getItem().getType());
            this.suitModel.setText(getItem().getModel());
            this.suitSerial.setText(getItem().getSerial());
            this.suitDateInUse.setText(getItem().getDateInUse());
        }
    }

    protected void assignFormElements(View view) {
        this.suitManufacturer = (EditText) view.findViewById(R.id.suit_form_manufacturer);
        this.suitType = (Spinner) view.findViewById(R.id.suit_form_type);
        this.suitType = (Spinner) view.findViewById(R.id.suit_form_type);
        ArrayAdapter<String> typeAdapter =
                new ArrayAdapter<String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item,
                        Suit.getSuitTypeArray()
                );

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        suitType.setAdapter(typeAdapter);

        this.suitModel = (EditText) view.findViewById(R.id.suit_form_model);
        this.suitDateInUse = (EditText) view.findViewById(R.id.suit_form_dateInUse);
        this.suitSerial = (EditText) view.findViewById(R.id.suit_form_serial);

        final DatePickerDialog.OnDateSetListener suitDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateInUse();
            }

        };

        this.suitDateInUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), suitDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        Button button = (Button) view.findViewById(R.id.suit_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });
    }

    private void updateDateInUse() {

        DateFormat df = new DateFormat();
        this.suitDateInUse.setText(df.format(calendar.getTime()));
    }


    @Override
    protected String getItemClass() {
        return Suit.class.getCanonicalName();
    }

    @Override
    protected int getParentFragmentId() {
        return R.id.nav_gear;
    }


    /**
     * Save the item
     * //TODO call item.save() method
     */
    public void save() {

        if (validateForm()) {

            //Required fields
            String suitManufacturer = this.suitManufacturer.getText().toString();
            String suitModel = this.suitModel.getText().toString();
            String suitDateInUse = this.suitDateInUse.getText().toString();
            String suitSerial = this.suitSerial.getText().toString();

            getItem().setManufacturer(suitManufacturer);
            getItem().setModel(suitModel);

            if (suitDateInUse.length() > 0) {
                getItem().setDateInUse(suitDateInUse);
            }

            getItem().setSerial(suitSerial);

            getItem().setType(Integer.parseInt(String.valueOf(this.suitType.getSelectedItemId())));

            super.save();
        }
    }

    /**
     * Validate our input
     *
     * @return boolean
     */
    protected boolean validateForm() {

        boolean valid = true;

        if (suitManufacturer.getText().length() == 0) {
            this.suitManufacturer.setError("Manufacturer required");
            valid = false;
        }

        if (suitModel.getText().length() == 0) {
            this.suitModel.setError("Model required");
            valid = false;
        }

        return valid;
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_suit_form;
    }
}
