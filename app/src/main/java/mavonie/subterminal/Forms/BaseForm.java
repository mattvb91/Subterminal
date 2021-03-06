package mavonie.subterminal.Forms;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.Calendar;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.UIHelper;

/**
 * Created by mavon on 14/10/16.
 */

public abstract class BaseForm extends BaseFragment {

    public static boolean arcMenu = false;

    public static Calendar calendar = Calendar.getInstance();

    protected abstract int getParentFragmentId();

    protected abstract void assignFormElements(View view);

    protected abstract void updateForm();

    protected abstract boolean validateForm();

    protected abstract int getLayoutName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(this.getLayoutName(), container, false);

        assignFormElements(view);

        if (this.getItem() != null && this.getItem().exists()) {
            updateForm();
            MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_delete).setVisible(true);
        }

        if (arcMenu) {
            UIHelper.getArcMenu().setVisibility(View.GONE);
        } else {
            UIHelper.getAddButton().hide();
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        _mListener = null;

        if (arcMenu) {
            UIHelper.getArcMenu().setVisibility(View.VISIBLE);
        } else {
            UIHelper.getAddButton().show();
        }

        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_delete).setVisible(false);
    }

    public void save() {

        String message = "";

        //Popup and redirect
        if (getItem().save()) {
            message = "Item has been saved";
        } else {
            message = "Could not save item";
        }

        UIHelper.goToFragment(getParentFragmentId());
        Snackbar.make(this.getView(), message, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        InputMethodManager inputManager = (InputMethodManager)
                MainActivity.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(MainActivity.getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
