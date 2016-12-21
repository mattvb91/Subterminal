package mavonie.subterminal.Utils;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.lang.reflect.Constructor;
import java.util.List;

import mavonie.subterminal.Forms.BaseForm;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Listeners.ImageListener;

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
    public void onResume() {
        super.onResume();

        Bundle params = new Bundle();
        params.putString("screen_name", this.getClass().getCanonicalName());
        Subterminal.getAnalytics().logEvent("screenview", params);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _mListener = null;
    }

    @Override
    public void onPause() {

        if (this.imageLayout != null & !this.isVisible()) {
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

    protected void showImages(List<Image> images) {
        for (Image current : images) {

            SimpleDraweeView image = new SimpleDraweeView(MainActivity.getActivity().getApplicationContext());
            image.setImageURI(current.getUri().toString());
            image.setMinimumWidth(150);
            image.setMinimumHeight(150);

            image.setOnClickListener(new ImageListener(current));

            this.imageLayout.addView(image);
        }
    }

    protected void loadImages() {
        List<Image> images = Image.loadImagesForEntity(getItem());

        if (!images.isEmpty()) {
            showImages(images);
            this.imageLayout.invalidate();
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

    /**
     * Request an Ad if user is not premium
     *
     * @param view
     */
    protected void adRequest(View view) {
        AdView mAdView = (AdView) view.findViewById(R.id.adView);

        if (!Subterminal.getUser().isPremium()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }
}
