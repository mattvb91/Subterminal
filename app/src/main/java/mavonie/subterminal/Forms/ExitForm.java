package mavonie.subterminal.Forms;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
import mavonie.subterminal.Utils.Subterminal;

public class ExitForm extends BaseForm implements AdapterView.OnItemSelectedListener {

    LocationManager locationManager;
    LocationListener locationListener;

    ProgressDialog progress;

    View view;

    private EditText exit_edit_name;
    private EditText exit_edit_lat;
    private EditText exit_edit_long;
    private EditText exit_edit_description;
    private EditText exit_edit_rockdrop_distance;
    private EditText exit_edit_altitude_to_landing;
    private Spinner exit_edit_object_type;

    private LinkedHashMap<String, String> object_types;
    LinkedHashMapAdapter<String, String> objectTypeAdapter;

    //TODO move the gps tracking out of here and off into its own class
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.object_types = Exit.getObject_types();
        locationManager = (LocationManager) MainActivity.getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                TextView editLat = (TextView) view.findViewById(R.id.exit_edit_lat);
                editLat.setText(Double.toString(location.getLatitude()));

                TextView editLong = (TextView) view.findViewById(R.id.exit_edit_long);
                editLong.setText(Double.toString(location.getLongitude()));

                progress.dismiss();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected String getItemClass() {
        return Exit.class.getCanonicalName();
    }

    @Override
    protected int getParentFragmentId() {
        return R.id.nav_exits;
    }

    @Override
    protected void assignFormElements(View view) {
        this.exit_edit_name = (EditText) view.findViewById(R.id.exit_edit_name);
        this.exit_edit_lat = (EditText) view.findViewById(R.id.exit_edit_lat);
        this.exit_edit_long = (EditText) view.findViewById(R.id.exit_edit_long);
        this.exit_edit_description = (EditText) view.findViewById(R.id.exit_edit_description);
        this.exit_edit_rockdrop_distance = (EditText) view.findViewById(R.id.exit_edit_rockdrop_distance);
        this.exit_edit_altitude_to_landing = (EditText) view.findViewById(R.id.exit_edit_distance_to_landing);

        exit_edit_object_type = (Spinner) view.findViewById(R.id.exit_object_type);
        this.objectTypeAdapter = new LinkedHashMapAdapter<String, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, this.object_types);
        this.objectTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exit_edit_object_type.setAdapter(this.objectTypeAdapter);
        exit_edit_object_type.setOnItemSelectedListener(this);

        Button gpsButton = (Button) view.findViewById(R.id.exit_edit_gps_button);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(MainActivity.getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                progress = new ProgressDialog(MainActivity.getActivity());
                progress.setTitle("Loading");
                progress.setMessage("Fetching GPS...");
                progress.show();
            }
        });

        Button button = (Button) view.findViewById(R.id.exit_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });
    }


    @Override
    protected void updateForm() {
        if (getItem().exists()) {
            Subterminal.setActiveModel(getItem());
            this.exit_edit_name.setText(getItem().getName());
            this.exit_edit_description.setText(getItem().getDescription());
            this.exit_edit_lat.setText(Double.toString(getItem().getLatitude()));
            this.exit_edit_long.setText(Double.toString(getItem().getLongtitude()));
            this.exit_edit_rockdrop_distance.setText(getItem().getRockdrop_distance().toString());
            this.exit_edit_altitude_to_landing.setText(getItem().getAltitude_to_landing().toString());

            Integer object_type_position = this.objectTypeAdapter.findPositionFromKey(getItem().getObject_type());
            if (object_type_position != null) {
                this.exit_edit_object_type.setSelection(object_type_position);
            }
        }
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_exit_form;
    }

    public void save() {
        //Required fields
        String exitName = this.exit_edit_name.getText().toString();
        String exitLat = this.exit_edit_lat.getText().toString();
        String exitLong = this.exit_edit_long.getText().toString();
        String exitDescription = this.exit_edit_description.getText().toString();
        String rockDropDistance = this.exit_edit_rockdrop_distance.getText().toString();
        String distanceToLanding = this.exit_edit_altitude_to_landing.getText().toString();

        if (validateForm()) {
            getItem().setName(exitName);

            if (!exitLat.isEmpty()) {
                getItem().setLatitude(Double.parseDouble(exitLat));
            }
            if (!exitLong.isEmpty()) {
                getItem().setLongtitude(Double.parseDouble(exitLong));
            }

            getItem().setDescription(exitDescription);
            getItem().setObject_type(Integer.parseInt(objectEntry.getKey()));
            getItem().setRockdrop_distance(Integer.parseInt(rockDropDistance));
            getItem().setAltitude_to_landing(Integer.parseInt(distanceToLanding));

            super.save();
        }
    }

    /**
     * Validate our input
     *
     * @return boolean
     */
    protected boolean validateForm() {

        boolean valid = true;

        if (exit_edit_name.getText().length() == 0) {
            this.exit_edit_name.setError("Exit name required");
            valid = false;
        }


        return valid;
    }


    public Exit getItem() {
        return (Exit) super.getItem();
    }

    private Map.Entry<String, String> objectEntry;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        objectEntry = this.objectTypeAdapter.getItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
