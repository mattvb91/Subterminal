package mavonie.subterminal.Forms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;
import mavonie.subterminal.models.Gear;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GearForm.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GearForm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GearForm extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Gear _item;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void updateForm() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gear_form, container, false);

        assignFormElements(view);

        if (this.getItem() != null) {
            updateForm();
            MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_delete).setVisible(true);
        }

        return view;
    }

    private void assignFormElements(View view) {
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * @return Gear|Null
     */
    private Gear getItem() {
        if (this._item == null) {
            if (getArguments() != null && !getArguments().isEmpty()) {
                this._item = (Gear) getArguments().getSerializable("item");
            } else {
                this._item = new Gear();
            }
        }
        return this._item;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Save the item
     * //TODO call item.save() method
     */
    public void save() {
        getItem().setContainerManufacturer(this.containerManufacturer.getText().toString());
        getItem().setContainerType(this.containerType.getText().toString());
        getItem().setContainerSerial(this.containerSerial.getText().toString());
        //getItem().setContainerDateInUse(new Date(this.containerDateInUse.getText().toString()));
        getItem().setCanopyManufacturer(this.canopyManufacturer.getText().toString());
        getItem().setCanopyType(this.canopyType.getText().toString());
        getItem().setCanopySerial(this.canopySerial.getText().toString());
        //getItem().setCanopyDateInUse(new Date(this.canopyDateInUse.getText().toString()));

        String message = "";

        //Popup and redirect
        if (getItem().save()) {
            message = "Item has been saved";
        } else {
            message = "Could not save item";
        }

        MainActivity.getActivity().goToFragment(R.id.nav_gear);
        Snackbar.make(this.getView(), message, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        InputMethodManager inputManager = (InputMethodManager)
                MainActivity.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(MainActivity.getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
