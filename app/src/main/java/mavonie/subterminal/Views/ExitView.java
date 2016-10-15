package mavonie.subterminal.Views;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import mavonie.subterminal.R;
import mavonie.subterminal.models.Exit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExitView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExitView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExitView extends Fragment implements OnMapReadyCallback {

    private Exit _item;
    protected MapView mMapView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ExitView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExitView.
     */
    // TODO: Rename and change types and number of parameters
    public static ExitView newInstance(String param1, String param2) {
        ExitView fragment = new ExitView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        MapsInitializer.initialize(getContext());
    }

    /**
     * @return Gear|Null
     */
    private Exit getItem() {
        if (this._item == null) {
            if (getArguments() != null && !getArguments().isEmpty()) {
                this._item = (Exit) getArguments().getSerializable("item");
            } else {
                this._item = new Exit();
            }
        }
        return this._item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exit_view, container, false);

        TextView description = (TextView) view.findViewById(R.id.exit_view_description);
        description.setText(Html.fromHtml(getItem().getDescription()));

        TextView rules = (TextView) view.findViewById(R.id.exit_view_rules);
        rules.setText(Html.fromHtml(getItem().getRules()));

        TextView rockdropDistance = (TextView) view.findViewById(R.id.exit_view_rockdrop_distance);
        rockdropDistance.setText(getItem().getFormatedRockdrop());

        TextView rockdropTime = (TextView) view.findViewById(R.id.exit_view_rockdrop_time);
        rockdropTime.setText(getItem().getFormattedRockdropTime());

        TextView altitudeToLanding = (TextView) view.findViewById(R.id.exit_view_altitude_to_landing);
        altitudeToLanding.setText(getItem().getFormatedAltitudeToLanding());

        if(getItem().getDifficulty_tracking_exit() != 0){
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

        mMapView = (MapView) view.findViewById(R.id.exit_view_map);
        mMapView.getMapAsync(this);

        mMapView.onCreate(savedInstanceState);

        return view;
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

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        MainActivity.getActivity().setTitle(this.getItem().getName());

        if (mMapView != null) {
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
