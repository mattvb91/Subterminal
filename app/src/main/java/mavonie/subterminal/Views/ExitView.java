package mavonie.subterminal.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.List;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.R;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Utils.BaseFragment;

/**
 * Exit view
 */
public class ExitView extends BaseFragment implements OnMapReadyCallback {

    protected MapView mMapView;

    public ExitView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapsInitializer.initialize(getContext());
        MainActivity.getActivity().setActiveModel(this.getItem());
//        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_delete).setVisible(true);
//        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_edit).setVisible(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_exit_view, container, false);

        if (getItem().getDescription() != null) {
            TextView description = (TextView) view.findViewById(R.id.exit_view_description);
            description.setText(Html.fromHtml(getItem().getDescription()));
        }

        this.imageLayout = (LinearLayout) view.findViewById(R.id.image_thumbs);

        TextView rules = (TextView) view.findViewById(R.id.exit_view_rules);
        rules.setText(Html.fromHtml(getItem().getRules()));

        TextView rockdropDistance = (TextView) view.findViewById(R.id.exit_view_rockdrop_distance);
        rockdropDistance.setText(getItem().getFormatedRockdrop());

        TextView rockdropTime = (TextView) view.findViewById(R.id.exit_view_rockdrop_time);
        rockdropTime.setText(getItem().getFormattedRockdropTime());

        TextView altitudeToLanding = (TextView) view.findViewById(R.id.exit_view_altitude_to_landing);
        altitudeToLanding.setText(getItem().getFormatedAltitudeToLanding());

        if (getItem().getDifficulty_tracking_exit() != 0) {
            TextView difficultyTrackingExit = (TextView) view.findViewById(R.id.exit_view_difficulty_tracking_exit);
            difficultyTrackingExit.setText(getItem().getDifficultyDescriptor(getItem().getDifficulty_tracking_exit()));
            difficultyTrackingExit.setTextColor(Color.parseColor(getItem().getDifficultyColor(getItem().getDifficulty_tracking_exit())));

            TextView difficultyTrackingFreefall = (TextView) view.findViewById(R.id.exit_view_difficulty_tracking_freefall);
            difficultyTrackingFreefall.setText(getItem().getDifficultyDescriptor(getItem().getDifficulty_tracking_freefall()));
            difficultyTrackingFreefall.setTextColor(Color.parseColor(getItem().getDifficultyColor(getItem().getDifficulty_tracking_freefall())));

            TextView difficultyTrackingLanding = (TextView) view.findViewById(R.id.exit_view_difficulty_tracking_landing);
            difficultyTrackingLanding.setText(getItem().getDifficultyDescriptor(getItem().getDifficulty_tracking_landing()));
            difficultyTrackingLanding.setTextColor(Color.parseColor(getItem().getDifficultyColor(getItem().getDifficulty_tracking_landing())));

            TextView difficultyTrackingOverall = (TextView) view.findViewById(R.id.exit_view_difficulty_tracking_overall);
            difficultyTrackingOverall.setText(getItem().getDifficultyDescriptor(getItem().getDifficulty_tracking_overall()));
            difficultyTrackingOverall.setTextColor(Color.parseColor(getItem().getDifficultyColor(getItem().getDifficulty_tracking_overall())));

            TextView difficultyWingsuitExit = (TextView) view.findViewById(R.id.exit_view_difficulty_wingsuit_exit);
            difficultyWingsuitExit.setText(getItem().getDifficultyDescriptor(getItem().getDifficulty_wingsuit_exit()));
            difficultyWingsuitExit.setTextColor(Color.parseColor(getItem().getDifficultyColor(getItem().getDifficulty_wingsuit_exit())));

            TextView difficultyWingsuitFreefall = (TextView) view.findViewById(R.id.exit_view_difficulty_wingsuit_freefall);
            difficultyWingsuitFreefall.setText(getItem().getDifficultyDescriptor(getItem().getDifficulty_wingsuit_freefall()));
            difficultyWingsuitFreefall.setTextColor(Color.parseColor(getItem().getDifficultyColor(getItem().getDifficulty_wingsuit_freefall())));

            TextView difficultyWingsuitLanding = (TextView) view.findViewById(R.id.exit_view_difficulty_wingsuit_landing);
            difficultyWingsuitLanding.setText(getItem().getDifficultyDescriptor(getItem().getDifficulty_wingsuit_landing()));
            difficultyWingsuitLanding.setTextColor(Color.parseColor(getItem().getDifficultyColor(getItem().getDifficulty_wingsuit_landing())));

            TextView difficultyWingsuitOverall = (TextView) view.findViewById(R.id.exit_view_difficulty_wingsuit_overall);
            difficultyWingsuitOverall.setText(getItem().getDifficultyDescriptor(getItem().getDifficulty_wingsuit_overall()));
            difficultyWingsuitOverall.setTextColor(Color.parseColor(getItem().getDifficultyColor(getItem().getDifficulty_wingsuit_overall())));
        }

        List<Image> images = Image.loadImagesForEntity(getItem());

        if (!images.isEmpty()) {
            showImages(images);
        }

        Button pictureButton = (Button) view.findViewById(R.id.exit_picture_button);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getActivity().onPickImage(view);
            }
        });

        if (getItem().isMapActive()) {
            mMapView = (MapView) view.findViewById(R.id.exit_view_map);
            mMapView.setVisibility(View.VISIBLE);
            mMapView.getMapAsync(this);
            mMapView.onCreate(savedInstanceState);
        }

        return view;
    }

    private void showImages(List<Image> images) {
        for (Image current : images) {

            ImageView image = new ImageView(MainActivity.getActivity().getApplicationContext());

            String path = current.getFullPath();

            Bitmap bitmap = current.decodeSampledBitmapFromResource(path, 200, 200);
            image.setImageBitmap(bitmap);
            image.setPadding(2, 2, 2, 2);
            image.setMaxWidth(300);
            image.setMaxHeight(300);
            image.setAdjustViewBounds(true);
            image.setScaleType(ImageView.ScaleType.FIT_XY);

            this.imageLayout.addView(image);

            image = null;
            bitmap = null;
        }
    }

    @Override
    protected String getItemClass() {
        return Exit.class.getCanonicalName();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(this.getItem().getLatitude(), this.getItem().getLongtitude()))
                .title(this.getItem().getName())
        );

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(marker.getPosition()) // Center Set
                .zoom(13.0f)                // Zoom
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        MainActivity.getActivity().setTitle(this.getItem().getName());

        if (mMapView != null && getItem().isMapActive()) {
            mMapView.onResume();
        }

        //Check if an image has been added
        Bitmap bitMap = MainActivity.getActivity().getLastBitmap();
        if (MainActivity.getActivity().getLastBitmap() instanceof Bitmap) {

            if (Image.createFromBitmap(bitMap, MainActivity.getActivity().getActiveModel())) {
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
    }

    @Override
    public void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }

        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_edit).setVisible(false);
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_delete).setVisible(false);
        MainActivity.getActivity().getFab().show();

        int count = this.imageLayout.getChildCount();
        for(int i = 0; i < count; i++) {
            ImageView image = (ImageView) this.imageLayout.getChildAt(i);
            image.setImageDrawable(null);
        }

        this.imageLayout = null;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mMapView != null) {
            try {
                mMapView.onDestroy();
            } catch (NullPointerException e) {
                Log.e("", "Error while attempting MapView.onDestroy(), ignoring exception", e);
            }
        }

        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null) {
            mMapView.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public Exit getItem() {
        return (Exit) super.getItem();
    }
}
