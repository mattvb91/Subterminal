package mavonie.subterminal.Skydive.Views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;

import az.openweatherapi.model.gson.common.Coord;
import co.lujun.androidtagview.TagContainerLayout;
import developer.shivam.library.CrescentoContainer;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Models.Skydive.Tunnel;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;
import mavonie.subterminal.Utils.Views.MapView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Dropzone View
 */
public class TunnelView extends BaseFragment implements OnMapReadyCallback {

    protected MapView mMapView;
    protected KenBurnsView top;

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
        final View view = inflater.inflate(R.layout.fragment_tunnel_view, container, false);

        TextView email = (TextView) view.findViewById(R.id.tunnel_view_email);
        email.setText(getItem().getEmail());

        TextView phone = (TextView) view.findViewById(R.id.tunnel_view_phone);
        phone.setText(getItem().getPhone());

        TextView website = (TextView) view.findViewById(R.id.tunnel_view_website);
        website.setText(getItem().getWebsite());

        TextView description = (TextView) view.findViewById(R.id.tunnel_view_description);
        description.setText(getItem().getDescription());

        top = (KenBurnsView) view.findViewById(R.id.kenburnsView);

        //Check for remote images
        Call images = Subterminal.getApi().getEndpoints().getTunnelImages(getItem().getId());
        images.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call call, final Response response) {
                if (response.isSuccessful()) {
                    final List<Image> images = (List<Image>) response.body();

                    if (images.size() > 0) {
                        CrescentoContainer crescento = (CrescentoContainer) view.findViewById(R.id.crescentoContainer);
                        crescento.setVisibility(View.VISIBLE);

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String url = "https://skydivelocations.com/image/" + images.get(0).getFilename() + "?full=true";
                                    final Bitmap bm = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());

                                    Handler mainHandler = new Handler(MainActivity.getActivity().getMainLooper());
                                    Runnable uiRunnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            top.setImageBitmap(bm);
                                        }
                                    };
                                    mainHandler.post(uiRunnable);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });

        if (getItem().isMapActive()) {
            Coord coordinate = new Coord();
            coordinate.setLat(getItem().getLatitude());
            coordinate.setLon(getItem().getLongtitude());

            mMapView = (MapView) view.findViewById(R.id.tunnel_view_map);
            mMapView.setVisibility(View.VISIBLE);
            mMapView.getMapAsync(this);
            mMapView.onCreate(savedInstanceState);
        } else {
            view.findViewById(R.id.tunnel_view_map_card).setVisibility(View.INVISIBLE);
        }

        adRequest(view);

        return view;
    }

    @Override
    protected Tunnel getItem() {
        return (Tunnel) super.getItem();
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
