package mavonie.subterminal.Forms;

import android.Manifest;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;
import mavonie.subterminal.models.Exit;

public class ExitForm extends BaseForm {

    LocationManager locationManager;
    LocationListener locationListener;

    View view;

    private EditText exit_edit_name;
    private EditText exit_edit_lat;
    private EditText exit_edit_long;
    private EditText exit_edit_description;


    //TODO move the gps tracking out of here and off into its own class
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        locationManager = (LocationManager) MainActivity.getActivity().getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(MainActivity.getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                TextView editLat = (TextView) view.findViewById(R.id.exit_edit_lat);
                editLat.setText(Double.toString(location.getLatitude()));

                TextView editLong = (TextView) view.findViewById(R.id.exit_edit_long);
                editLong.setText(Double.toString(location.getLongitude()));
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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

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

        Button button = (Button) view.findViewById(R.id.exit_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });
    }


    @Override
    protected void updateForm() {

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

        if (validateForm()) {
            getItem().setName(exitName);
            getItem().setLatitude(Double.parseDouble(exitLat));
            getItem().setLongtitude(Double.parseDouble(exitLong));
            getItem().setDescription(exitDescription);

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
}
