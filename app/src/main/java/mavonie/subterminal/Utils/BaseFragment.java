package mavonie.subterminal.Utils;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;

import java.lang.reflect.Constructor;

import mavonie.subterminal.Forms.BaseForm;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Model;

/**
 * Created by mavon on 19/10/16.
 */

public abstract class BaseFragment extends Fragment {

    private Model _item;

    protected abstract String getItemClass();

    protected OnFragmentInteractionListener _mListener;

    protected OnFragmentInteractionListener getmListener() {
        return _mListener;
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
                    Object object = ctor.newInstance(new Object[]{});

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
        if (_mListener != null) {
            _mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseForm.OnFragmentInteractionListener) {
            _mListener = (BaseForm.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _mListener = null;
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

        void onFragmentInteraction(Model mItem);
    }
}
