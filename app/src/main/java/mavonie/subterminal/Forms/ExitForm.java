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
import android.widget.TextView;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;
import mavonie.subterminal.models.Exit;

public class ExitForm extends BaseForm implements LocationListener {

    LocationManager locationManager;
    LocationListener locationListener;

    View view;

    //TODO move the gps tracking out of here and off into its own class
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        locationManager = (LocationManager) MainActivity.getActivity().getSystemService(Context.LOCATION_SERVICE);
        view = inflater.inflate(this.getLayoutName(), container, false);


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

    }

    @Override
    protected void updateForm() {

    }

    @Override
    protected boolean validateForm() {
        return false;
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_exit_form;
    }
}
