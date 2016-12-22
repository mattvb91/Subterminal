package mavonie.subterminal.Skydive.Views;

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

        if (getItem().getDelay() != null) {
            TextView delay = (TextView) view.findViewById(R.id.skydive_view_delay);
            delay.setText(Integer.toString(getItem().getDelay()) + "s");
        }

        TextView date = (TextView) view.findViewById(R.id.skydive_view_date);
        date.setText(getItem().getDate());

        imageLayout = (LinearLayout) view.findViewById(R.id.image_thumbs);

        loadImages();

        Button pictureButton = (Button) view.findViewById(R.id.skydive_view_picture_button);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getActivity().onPickImage(v);
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