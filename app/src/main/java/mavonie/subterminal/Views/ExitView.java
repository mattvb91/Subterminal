package mavonie.subterminal.Views;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import az.openweatherapi.model.gson.common.Coord;
import butterknife.BindView;
import butterknife.ButterKnife;
import developer.shivam.library.CrescentoContainer;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;
import mavonie.subterminal.Utils.UnitConverter;
import mavonie.subterminal.Utils.Views.MapView;

/**
 * Exit view
 */
public class ExitView extends BaseFragment implements OnMapReadyCallback {

    @BindView(R.id.kenburnsView) KenBurnsView top;
    @BindView(R.id.exit_view_rockdrop_time) TextView rockdropTime;
    @BindView(R.id.exit_view_rockdrop_distance) TextView rockdropDistance;
    @BindView(R.id.exit_view_altitude_to_landing) TextView altitudeToLanding;
    @BindView(R.id.exit_view_description) TextView description;

    @BindView(R.id.crescentoContainer) CrescentoContainer crescento;
    @BindView(R.id.exit_view_map_card) RelativeLayout mapLayout;

    protected MapView mMapView;

    public ExitView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapsInitializer.initialize(getContext());
        Subterminal.setActiveModel(this.getItem());

        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_delete).setVisible(true);
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_edit).setVisible(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_exit_view, container, false);
        ButterKnife.bind(this, view);

        if (getItem().getDescription() != null) {
            description.setText(Html.fromHtml(getItem().getDescription()));
        }

        this.imageLayout = (LinearLayout) view.findViewById(R.id.image_thumbs);

        UIHelper.loadKenBurnsHeader(crescento, top, getItem());

        rockdropDistance.setText(UnitConverter.getFormattedDistance(getItem().getRockdropDistance(), getItem().getHeightUnit()));
        rockdropTime.setText(getItem().getFormattedRockdropTime());
        altitudeToLanding.setText(UnitConverter.getFormattedDistance(getItem().getAltitudeToLanding(), getItem().getHeightUnit()));

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mapLayout.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, R.id.exit_view_images_card);
        mapLayout.setLayoutParams(params);

        loadImages();

        Button pictureButton = (Button) view.findViewById(R.id.exit_picture_button);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getActivity().onPickImage(view);
            }
        });

        if (getItem().isMapActive()) {
            Coord coordinate = new Coord();
            coordinate.setLat(getItem().getLatitude());
            coordinate.setLon(getItem().getLongtitude());
            UIHelper.initWeatherView(view, coordinate);

            mMapView = (MapView) view.findViewById(R.id.exit_view_map);
            mMapView.setVisibility(View.VISIBLE);
            mMapView.getMapAsync(this);
            mMapView.onCreate(savedInstanceState);
        } else {
            view.findViewById(R.id.exit_view_map_card).setVisibility(View.INVISIBLE);
        }

        adRequest(view);

        return view;
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
    }

    @Override
    public void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }

        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_edit).setVisible(false);
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_delete).setVisible(false);
        UIHelper.getAddButton().show();

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
