package mavonie.subterminal.Skydive.Forms;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import mavonie.subterminal.Forms.BaseForm;
import mavonie.subterminal.Models.Skydive.Rig;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Date.DateFormat;
import mavonie.subterminal.Utils.Subterminal;

public class GearForm extends BaseForm {

    private EditText containerManufacturer,
            containerModel,
            containerSerial,
            containerDateInUse,
            mainManufacturer,
            mainModel,
            mainSerial,
            mainDateInUse,
            reserveManufacturer,
            reserveModel,
            reserveSerial,
            reserveDateInUse,
            aadManufacturer,
            aadModel,
            aadSerial,
            aadDateInUse;

    @Override
    protected Rig getItem() {
        return (Rig) super.getItem();
    }


    protected void updateForm() {
        if (getItem().exists()) {
            Subterminal.setActiveModel(getItem());

            this.containerManufacturer.setText(getItem().getContainerManufacturer());
            this.containerModel.setText(getItem().getContainerModel());
            this.containerSerial.setText(getItem().getContainerSerial());
            this.containerDateInUse.setText(getItem().getContainerDateInUse());

            this.mainManufacturer.setText(getItem().getMainManufacturer());
            this.mainModel.setText(getItem().getMainModel());
            this.mainSerial.setText(getItem().getMainSerial());
            this.mainDateInUse.setText(getItem().getMainDateInUse());

            this.reserveManufacturer.setText(getItem().getReserveManufacturer());
            this.reserveModel.setText(getItem().getReserveModel());
            this.reserveSerial.setText(getItem().getReserveSerial());
            this.reserveDateInUse.setText(getItem().getReserveDateInUse());

            this.aadManufacturer.setText(getItem().getAadManufacturer());
            this.aadModel.setText(getItem().getAadModel());
            this.aadSerial.setText(getItem().getAadSerial());
            this.aadDateInUse.setText(getItem().getAadDateInUse());
        }
    }

    protected void assignFormElements(View view) {
        this.containerManufacturer = (EditText) view.findViewById(R.id.edit_container_manufacturer);
        this.containerModel = (EditText) view.findViewById(R.id.edit_container_type);
        this.containerSerial = (EditText) view.findViewById(R.id.edit_container_serial);
        this.containerDateInUse = (EditText) view.findViewById(R.id.edit_container_dateInUse);

        this.mainManufacturer = (EditText) view.findViewById(R.id.edit_canopy_manufacturer);
        this.mainModel = (EditText) view.findViewById(R.id.edit_canopy_model);
        this.mainSerial = (EditText) view.findViewById(R.id.edit_canopy_serial);
        this.mainDateInUse = (EditText) view.findViewById(R.id.edit_canopy_dateInUse);

        this.reserveManufacturer = (EditText) view.findViewById(R.id.edit_reserve_manufacturer);
        this.reserveModel = (EditText) view.findViewById(R.id.edit_reserve_model);
        this.reserveSerial = (EditText) view.findViewById(R.id.edit_reserve_serial);
        this.reserveDateInUse = (EditText) view.findViewById(R.id.edit_reserve_dateInUse);

        this.aadManufacturer = (EditText) view.findViewById(R.id.edit_aad_manufacturer);
        this.aadModel = (EditText) view.findViewById(R.id.edit_aad_model);
        this.aadSerial = (EditText) view.findViewById(R.id.edit_aad_serial);
        this.aadDateInUse = (EditText) view.findViewById(R.id.edit_aad_dateInUse);

        final DatePickerDialog.OnDateSetListener containerDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateContainerDate();
            }

        };

        final DatePickerDialog.OnDateSetListener canopyDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateMainDate();
            }

        };

        final DatePickerDialog.OnDateSetListener reserveDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateReserveDate();
            }

        };

        final DatePickerDialog.OnDateSetListener aadDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateAAdDate();
            }

        };

        this.containerDateInUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), containerDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        this.mainDateInUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), canopyDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        this.reserveDateInUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), reserveDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        this.aadDateInUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), aadDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button button = (Button) view.findViewById(R.id.gear_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });
    }

    private void updateContainerDate() {

        DateFormat df = new DateFormat();
        this.containerDateInUse.setText(df.format(calendar.getTime()));
    }

    private void updateMainDate() {

        DateFormat df = new DateFormat();
        this.mainDateInUse.setText(df.format(calendar.getTime()));
    }

    private void updateReserveDate() {

        DateFormat df = new DateFormat();
        this.reserveDateInUse.setText(df.format(calendar.getTime()));
    }

    private void updateAAdDate() {

        DateFormat df = new DateFormat();
        this.aadDateInUse.setText(df.format(calendar.getTime()));
    }

    @Override
    protected String getItemClass() {
        return Rig.class.getCanonicalName();
    }

    @Override
    protected int getParentFragmentId() {
        return R.id.skydiving_nav_gear;
    }


    /**
     * Save the item
     */
    public void save() {

        if (validateForm()) {
            getItem().setContainerManufacturer(this.containerManufacturer.getText().toString());
            getItem().setContainerModel(this.containerModel.getText().toString());
            getItem().setContainerSerial(this.containerSerial.getText().toString());
            getItem().setContainerDateInUse(this.containerDateInUse.getText().toString());

            getItem().setMainManufacturer(this.mainManufacturer.getText().toString());
            getItem().setMainModel(this.mainModel.getText().toString());
            getItem().setMainSerial(this.mainSerial.getText().toString());
            getItem().setMainDateInUse(this.mainDateInUse.getText().toString());

            getItem().setReserveManufacturer(this.reserveManufacturer.getText().toString());
            getItem().setReserveModel(this.reserveModel.getText().toString());
            getItem().setReserveSerial(this.reserveSerial.getText().toString());
            getItem().setReserveDateInUse(this.reserveDateInUse.getText().toString());

            getItem().setAadManufacturer(this.aadManufacturer.getText().toString());
            getItem().setAadModel(this.aadModel.getText().toString());
            getItem().setAadSerial(this.aadSerial.getText().toString());
            getItem().setAadDateInUse(this.aadDateInUse.getText().toString());

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

        if (containerManufacturer.getText().length() == 0) {
            this.containerManufacturer.setError("Manufacturer required");
            valid = false;
        }

        if (containerModel.getText().length() == 0) {
            this.containerModel.setError("Type required");
            valid = false;
        }

        if (mainManufacturer.getText().length() == 0) {
            this.mainManufacturer.setError("Manufacturer required");
            valid = false;
        }

        if (reserveManufacturer.getText().length() == 0) {
            this.reserveManufacturer.setError("Manufacturer required");
            valid = false;
        }

        if (aadManufacturer.getText().length() == 0) {
            this.aadManufacturer.setError("Manufacturer required");
            valid = false;
        }

        return valid;
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_skydive_gear_form;
    }
}
