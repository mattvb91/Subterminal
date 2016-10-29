package mavonie.subterminal.Views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Date.TimeAgo;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;

/**
 * Jump view
 */
public class JumpView extends BaseFragment {

    public JumpView() {
        // Required empty public constructor
    }

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
        View view = inflater.inflate(R.layout.fragment_jump_view, container, false);

        TextView jumpDate = (TextView) view.findViewById(R.id.jump_view_date);
        jumpDate.setText(TimeAgo.sinceToday(getItem().getDate()));

        TextView jumpExit = (TextView) view.findViewById(R.id.jump_view_exit_name);
        jumpExit.setText(getItem().getExit().getName());

        if (getItem().getGear() != null) {
            TextView jumpRig = (TextView) view.findViewById(R.id.jump_view_rig);
            jumpRig.setText(getItem().getGear().getDisplayName());
        }

        TextView jumpSlider = (TextView) view.findViewById(R.id.jump_view_slider);
        jumpSlider.setText(getItem().getFormattedSlider());

        TextView jumpPC = (TextView) view.findViewById(R.id.jump_view_pc);
        jumpPC.setText(Integer.toString(getItem().getPc_size()));

        TextView jumpDescription = (TextView) view.findViewById(R.id.jump_view_description);
        jumpDescription.setText(getItem().getDescription());

        this.imageLayout = (LinearLayout) view.findViewById(R.id.image_thumbs);

        loadImages();

        Button pictureButton = (Button) view.findViewById(R.id.exit_picture_button);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getActivity().onPickImage(v);
            }
        });

        return view;
    }

    @Override
    protected Jump getItem() {
        return (Jump) super.getItem();
    }

    @Override
    protected String getItemClass() {
        return Exit.class.getCanonicalName();
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
