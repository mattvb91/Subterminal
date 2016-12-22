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
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.Views.MapView;

/**
 * Skydive View
 */
public class SkydiveView extends BaseFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Subterminal.setActiveModel(getItem());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skydive_view, container, false);

        TextView description = (TextView) view.findViewById(R.id.skydive_view_description);
        description.setText(getItem().getDescription());

        if (getItem().getAltitude() != null) {
            TextView altitude = (TextView) view.findViewById(R.id.skydive_view_altitude);
            altitude.setText(getItem().getAltitude());
        }

        if (getItem().getDelay() != null) {
            TextView delay = (TextView) view.findViewById(R.id.skydive_view_delay);
            delay.setText(Integer.toString(getItem().getDelay()) + "s");
        }

        TextView date = (TextView) view.findViewById(R.id.skydive_view_date);
        date.setText(getItem().getDate());

        adRequest(view);

        return view;
    }

    @Override
    protected Skydive getItem() {
        return (Skydive) super.getItem();
    }

    @Override
    protected String getItemClass() {
        return Skydive.class.getCanonicalName();
    }

}
