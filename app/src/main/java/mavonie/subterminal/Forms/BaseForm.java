package mavonie.subterminal.Forms;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Constructor;
import java.util.Calendar;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;
import mavonie.subterminal.models.Model;

/**
 * Created by mavon on 14/10/16.
 */

public abstract class BaseForm extends Fragment {

    private Model _item;

    private OnFragmentInteractionListener mListener;

    Calendar myCalendar = Calendar.getInstance();

    protected abstract String getItemClass();

    protected abstract int getParentFragmentId();

    protected abstract void assignFormElements(View view);

    protected abstract void updateForm();

    protected abstract boolean validateForm();

    protected abstract int getLayoutName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

        return view;
    }

    /**
     * Load the model based on its association with the form
     *
     * @return Model
     */
    protected Model getItem() {
        if (this._item == null) {
            if (getArguments() != null && !getArguments().isEmpty()) {
                this._item = (Model) getArguments().getSerializable("item");
            } else {
                try {
                    Class<?> clazz = Class.forName(this.getItemClass());
                    Constructor<?> ctor = clazz.getConstructor();
                    Object object = ctor.newInstance(new Object[] { });

                    this._item = (Model) object;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return this._item;
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

    public void save() {

        String message = "";

        //Popup and redirect
        if (getItem().save()) {
            message = "Item has been saved";
        } else {
            message = "Could not save item";
        }

        MainActivity.getActivity().goToFragment(getParentFragmentId());
        Snackbar.make(this.getView(), message, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        InputMethodManager inputManager = (InputMethodManager)
                MainActivity.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(MainActivity.getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
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
}
