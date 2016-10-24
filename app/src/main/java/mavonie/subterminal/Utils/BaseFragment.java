package mavonie.subterminal.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.leakcanary.RefWatcher;

import java.lang.reflect.Constructor;
import java.util.List;

import mavonie.subterminal.Forms.BaseForm;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.R;

/**
 * Created by mavon on 19/10/16.
 */

public abstract class BaseFragment extends Fragment {

    private Model _item;

    protected abstract String getItemClass();

    protected OnFragmentInteractionListener _mListener;

    protected LinearLayout imageLayout;

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

    @Override
    public void onPause() {

        if (this.imageLayout != null &! this.isVisible()) {
            int count = this.imageLayout.getChildCount();
            for (int i = 0; i < count; i++) {
                ImageView image = (ImageView) this.imageLayout.getChildAt(i);
                image.setImageDrawable(null);
            }

            this.imageLayout = null;
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MainActivity.getActivity().getRefWatcher().watch(this);
    }

    @Override
    public void onResume() {

        //Check if an image has been added
        Bitmap bitMap = MainActivity.getActivity().getLastBitmap();
        if (bitMap instanceof Bitmap) {

            if (Image.createFromBitmap(bitMap, Subterminal.getActiveModel())) {
                ImageView image = new ImageView(MainActivity.getActivity().getApplicationContext());
                image.setImageBitmap(bitMap);
                image.setPadding(2, 2, 2, 2);
                image.setMaxWidth(300);
                image.setMaxHeight(300);
                image.setAdjustViewBounds(true);
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                this.imageLayout.addView(image);
            }

            MainActivity.getActivity().lastBitmap = null;
        }

        super.onResume();
    }

    protected void showImages(List<Image> images) {
        for (Image current : images) {

            ImageView image = new ImageView(MainActivity.getActivity().getApplicationContext());

            String path = current.getFullPath();

            Bitmap bitmap = current.decodeSampledBitmapFromResource(path, 200, 200);
            image.setImageBitmap(bitmap);
            image.setPadding(2, 2, 2, 2);
            image.setMaxWidth(300);
            image.setMaxHeight(300);
            image.setAdjustViewBounds(true);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);

            this.imageLayout.addView(image);

            image = null;
            bitmap = null;
        }
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
