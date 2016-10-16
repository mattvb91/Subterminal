package mavonie.subterminal.Forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


import java.util.ArrayList;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;
import mavonie.subterminal.models.Exit;
import mavonie.subterminal.models.Jump;

/**
 * Created by mavon on 15/10/16.
 */

public class JumpForm extends BaseForm {

    private AutoCompleteTextView actv;

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
