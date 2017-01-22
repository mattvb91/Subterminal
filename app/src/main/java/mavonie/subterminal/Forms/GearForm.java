package mavonie.subterminal.Forms;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import mavonie.subterminal.Models.Gear;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Date.DateFormat;
import mavonie.subterminal.Utils.Subterminal;

public class GearForm extends BaseForm {

    private EditText containerManufacturer,
            containerType,
            containerSerial,
            containerDateInUse,
            canopyManufacturer,
            canopyType,
            canopySerial,
            canopyDateInUse;

    @Override
    protected Gear getItem() {
        return (Gear) super.getItem();
    }


    protected void updateForm() {
        if (getItem().exists()) {
            Subterminal.setActiveModel(getItem());
            this.containerManufacturer.setText(getItem().getContainerManufacturer());
            this.containerType.setText(getItem().getContainerType());
            this.containerSerial.setText(getItem().getContainerSerial());
            this.containerDateInUse.setText(getItem().getContainerDateInUse());
            this.canopyManufacturer.setText(getItem().getCanopyManufacturer());
            this.canopyType.setText(getItem().getCanopyType());
            this.canopySerial.setText(getItem().getCanopySerial());
            this.canopyDateInUse.setText(getItem().getCanopyDateInUse());
        }
    }

    protected void assignFormElements(View view) {
        this.containerManufacturer = (EditText) view.findViewById(R.id.edit_container_manufacturer);
        this.containerType = (EditText) view.findViewById(R.id.edit_container_type);
        this.containerSerial = (EditText) view.findViewById(R.id.edit_container_serial);

        this.containerDateInUse = (EditText) view.findViewById(R.id.edit_container_dateInUse);

        final DatePickerDialog.OnDateSetListener containerDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateCanopyDate();
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

        this.canopyManufacturer = (EditText) view.findViewById(R.id.edit_canopy_manufacturer);
        this.canopyType = (EditText) view.findViewById(R.id.edit_canopy_type);
        this.canopySerial = (EditText) view.findViewById(R.id.edit_canopy_serial);

        this.canopyDateInUse = (EditText) view.findViewById(R.id.edit_canopy_dateInUse);

        this.canopyDateInUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), canopyDate, calendar
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

    private void updateCanopyDate() {

        DateFormat df = new DateFormat();
        this.canopyDateInUse.setText(df.format(calendar.getTime()));
    }


    @Override
    protected String getItemClass() {
        return Gear.class.getCanonicalName();
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
        //Required fields
        String containerManufacturer = this.containerManufacturer.getText().toString();
        String containerType = this.containerType.getText().toString();
        String canopyManufacturer = this.canopyManufacturer.getText().toString();

        if (validateForm()) {
            getItem().setContainerManufacturer(containerManufacturer);
            getItem().setContainerType(containerType);
            getItem().setContainerSerial(this.containerSerial.getText().toString());
            getItem().setContainerDateInUse(this.containerDateInUse.getText().toString());

            getItem().setCanopyManufacturer(canopyManufacturer);
            getItem().setCanopyType(this.canopyType.getText().toString());
            getItem().setCanopySerial(this.canopySerial.getText().toString());
            getItem().setCanopyDateInUse(this.canopyDateInUse.getText().toString());

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

        if (containerType.getText().length() == 0) {
            this.containerType.setError("Type required");
            valid = false;
        }

        if (canopyManufacturer.getText().length() == 0) {
            this.canopyManufacturer.setError("Manufacturer required");
            valid = false;
        }

        return valid;
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_gear_form;
    }
}
