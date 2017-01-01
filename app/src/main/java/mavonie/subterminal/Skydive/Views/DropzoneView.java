package mavonie.subterminal.Skydive.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.Views.MapView;

/**
 * Dropzone View
 */
public class DropzoneView extends BaseFragment implements OnMapReadyCallback {

    protected MapView mMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getContext());

        Subterminal.setActiveModel(getItem());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dropzone_view, container, false);

        TextView name = (TextView) view.findViewById(R.id.dropzone_view_name);
        name.setText(getItem().getName());

        TextView email = (TextView) view.findViewById(R.id.dropzone_view_email);
        email.setText(getItem().getEmail());

        TextView phone = (TextView) view.findViewById(R.id.dropzone_view_phone);
        phone.setText(getItem().getPhone());

        TextView website = (TextView) view.findViewById(R.id.dropzone_view_website);
        website.setText(getItem().getWebsite());

        TextView description = (TextView) view.findViewById(R.id.dropzone_view_description);
        description.setText(getItem().getDescription());

        TextView aircraft = (TextView) view.findViewById(R.id.dropzone_view_aircraft);
        aircraft.setText(getItem().getFormattedAircraft());

        if (getItem().isMapActive()) {
            mMapView = (MapView) view.findViewById(R.id.dropzone_view_map);
            mMapView.setVisibility(View.VISIBLE);
            mMapView.getMapAsync(this);
            mMapView.onCreate(savedInstanceState);
        } else {
            view.findViewById(R.id.dropzone_view_map_card).setVisibility(View.INVISIBLE);
        }

        adRequest(view);

        return view;
    }

    @Override
    protected Dropzone getItem() {
        return (Dropzone) super.getItem();
    }

    @Override
    protected String getItemClass() {
        return Dropzone.class.getCanonicalName();
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
}
