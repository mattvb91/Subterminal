package mavonie.subterminal.Forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;


import java.util.ArrayList;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;
import mavonie.subterminal.models.Exit;
import mavonie.subterminal.models.Gear;
import mavonie.subterminal.models.Jump;

/**
 * Created by mavon on 15/10/16.
 */

public class JumpForm extends BaseForm {

    private AutoCompleteTextView actv;

    //TODO move out to assignFormElements
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        MainActivity.getActivity().setTheme(android.R.style.Theme);

        View view = super.onCreateView(inflater, container, savedInstanceState);

        this.actv = (AutoCompleteTextView) view.findViewById(R.id.jump_edit_exit_name);

        ArrayList exitNames = new Exit().getItemsForSelectArray("name");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.getActivity().getApplicationContext(), R.layout.item_simple, exitNames);
        actv.setAdapter(adapter);

        actv.setAdapter(adapter);
        actv.setThreshold(2);

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

        return view;
    }

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

    }

    @Override
    protected void updateForm() {

    }

    @Override
    protected boolean validateForm() {
        return false;
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_jump_form;
    }
}
