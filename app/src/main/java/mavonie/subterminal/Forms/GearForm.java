package mavonie.subterminal.Forms;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;
import mavonie.subterminal.models.Gear;

public class GearForm extends BaseForm {

    private EditText containerManufacturer;
    private EditText containerType;
    private EditText containerSerial;
    private EditText containerDateInUse;
    private EditText canopyManufacturer;
    private EditText canopyType;
    private EditText canopySerial;
    private EditText canopyDateInUse;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GearForm.
     */
    // TODO: Rename and change types and number of parameters
    public static GearForm newInstance() {
        GearForm fragment = new GearForm();
        return fragment;
    }

    @Override
    protected Gear getItem() {
        return (Gear) super.getItem();
    }


    protected void updateForm() {
        if (getItem().exists()) {
            MainActivity.getActivity().setActiveModel(getItem());
            this.containerManufacturer.setText(getItem().getContainerManufacturer());
            this.containerType.setText(getItem().getContainerType());
            this.containerSerial.setText(getItem().getContainerSerial());
            //this.containerDateInUse.setText(getItem().getContainerDateInUse().toString());
            this.canopyManufacturer.setText(getItem().getCanopyManufacturer());
            this.canopyType.setText(getItem().getCanopyType());
            this.canopySerial.setText(getItem().getCanopySerial());
            //this.canopyDateInUse.setText(getItem().getCanopyDateInUse().toString());
        }
    }

    protected void assignFormElements(View view) {
        this.containerManufacturer = (EditText) view.findViewById(R.id.edit_container_manufacturer);
        this.containerType = (EditText) view.findViewById(R.id.edit_container_type);
        this.containerSerial = (EditText) view.findViewById(R.id.edit_container_serial);
        this.containerDateInUse = (EditText) view.findViewById(R.id.edit_container_dateInUse);
        this.canopyManufacturer = (EditText) view.findViewById(R.id.edit_canopy_manufacturer);
        this.canopyType = (EditText) view.findViewById(R.id.edit_canopy_type);
        this.canopySerial = (EditText) view.findViewById(R.id.edit_canopy_serial);
        this.canopyDateInUse = (EditText) view.findViewById(R.id.edit_canopy_dateInUse);

        Button button = (Button) view.findViewById(R.id.gear_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });
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
            //getItem().setContainerDateInUse(new Date(this.containerDateInUse.getText().toString()));
            getItem().setCanopyManufacturer(canopyManufacturer);
            getItem().setCanopyType(this.canopyType.getText().toString());
            getItem().setCanopySerial(this.canopySerial.getText().toString());
            //getItem().setCanopyDateInUse(new Date(this.canopyDateInUse.getText().toString()));

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
