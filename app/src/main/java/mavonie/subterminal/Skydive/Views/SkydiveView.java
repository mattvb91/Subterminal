package mavonie.subterminal.Skydive.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.R;
import mavonie.subterminal.SignatureActivity;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;

/**
 * Skydive View
 */
public class SkydiveView extends BaseFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Subterminal.setActiveModel(getItem());
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_delete).setVisible(true);
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_edit).setVisible(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skydive_view, container, false);

        TextView description = (TextView) view.findViewById(R.id.skydive_view_description);
        description.setText(getItem().getDescription());

        if (getItem().getExitAltitude() != null) {
            TextView altitude = (TextView) view.findViewById(R.id.skydive_view_altitude);
            altitude.setText(Integer.toString(getItem().getExitAltitude()));
        }

        if (getItem().getDeployAltitude() != null) {
            TextView deployAlitude = (TextView) view.findViewById(R.id.skydive_view_deplpoy_altitude);
            deployAlitude.setText(Integer.toString(getItem().getDeployAltitude()));
        }

        if (getItem().getDelay() != null) {
            TextView delay = (TextView) view.findViewById(R.id.skydive_view_delay);
            delay.setText(Integer.toString(getItem().getDelay()) + "s");
        }

        TextView date = (TextView) view.findViewById(R.id.skydive_view_date);
        date.setText(getItem().getDate());

        if (getItem().getRig() != null) {
            TextView rig = (TextView) view.findViewById(R.id.skydive_view_rig);
            rig.setText(getItem().getRig().getDisplayName());
        }

        if (getItem().getAircraftId() != null) {
            TextView aircraft = (TextView) view.findViewById(R.id.skydive_view_aircraft);
            aircraft.setText(getItem().getAircraft().getName());
        }

        if (getItem().getDropzone() != null) {
            TextView dropzone = (TextView) view.findViewById(R.id.skydive_view_dropzone_name);
            dropzone.setText(getItem().getDropzone().getName());
        }

        if (getItem().getJumpType() != null) {
            TextView jumpType = (TextView) view.findViewById(R.id.skydive_view_jump_type);
            jumpType.setText(Skydive.getJumpTypes().get(getItem().getJumpType()));
        }

        imageLayout = (LinearLayout) view.findViewById(R.id.image_thumbs);

        loadImages();

        loadSignatures(getItem().getSignatures(), view);

        Button pictureButton = (Button) view.findViewById(R.id.skydive_view_picture_button);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getActivity().onPickImage(v);
            }
        });

        Button signatureButton = (Button) view.findViewById(R.id.jump_signature_button);
        signatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.getActivity(), SignatureActivity.class);
                startActivity(intent);
            }
        });

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

    @Override
    public void onPause() {
        super.onPause();
        _mListener = null;
        UIHelper.getAddButton().show();
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_delete).setVisible(false);
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_edit).setVisible(false);
    }

}
