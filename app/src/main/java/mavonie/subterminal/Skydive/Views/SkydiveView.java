package mavonie.subterminal.Skydive.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import developer.shivam.library.CrescentoContainer;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.R;
import mavonie.subterminal.SignatureActivity;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;
import mavonie.subterminal.Utils.UnitConverter;

/**
 * Skydive View
 */
public class SkydiveView extends BaseFragment {

    @BindView(R.id.skydive_view_description) TextView description;
    @BindView(R.id.skydive_view_altitude) TextView altitude;
    @BindView(R.id.skydive_view_deplpoy_altitude) TextView deployAlitude;
    @BindView(R.id.skydive_view_delay) TextView delay;
    @BindView(R.id.skydive_view_rig) TextView rig;
    @BindView(R.id.skydive_view_aircraft) TextView aircraft;
    @BindView(R.id.skydive_view_dropzone_name) TextView dropzone;
    @BindView(R.id.skydive_view_jump_type) TextView jumpType;
    @BindView(R.id.skydive_view_jump_date) TextView jumpDate;
    @BindView(R.id.crescentoContainer) CrescentoContainer crescento;
    @BindView(R.id.kenburnsView) KenBurnsView top;

    @OnClick(R.id.skydive_view_dropzone_name) void openDropzone() {
        if(getItem().getDropzone() == null)
            return;

        DropzoneView dropzone = new DropzoneView();
        Bundle args = new Bundle();
        args.putSerializable("item", getItem().getDropzone());
        dropzone.setArguments(args);

        UIHelper.replaceFragment(dropzone);
    }

    @OnClick(R.id.skydive_view_picture_button) void pickImage() {
        MainActivity.getActivity().onPickImage(null);
    }

    @OnClick(R.id.jump_signature_button) void startSignature() {
        Intent intent = new Intent(MainActivity.getActivity(), SignatureActivity.class);
        startActivity(intent);
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
        View view = inflater.inflate(R.layout.fragment_skydive_view, container, false);
        ButterKnife.bind(this, view);

        description.setText(getItem().getDescription());
        jumpDate.setText(getItem().getDate());

        if (getItem().getExitAltitude() != null) {
            altitude.setText(UnitConverter.getFormattedDistance(getItem().getExitAltitude(), getItem().getHeightUnit()));
        }

        if (getItem().getDeployAltitude() != null) {
            deployAlitude.setText(UnitConverter.getFormattedDistance(getItem().getDeployAltitude(), getItem().getHeightUnit()));
        }

        if (getItem().getDelay() != null) {
            delay.setText(Integer.toString(getItem().getDelay()) + "s");
        }

        if (getItem().getRig() != null) {
            rig.setText(getItem().getRig().getDisplayName());
        }

        if (getItem().getAircraftId() != null) {
            aircraft.setText(getItem().getAircraft().getName());
        }

        if (getItem().getDropzone() != null) {
            dropzone.setText(getItem().getDropzone().getName());
        }

        if (getItem().getJumpType() != null) {
            jumpType.setText(Skydive.getJumpTypes().get(getItem().getJumpType()));
        }

        imageLayout = (LinearLayout) view.findViewById(R.id.image_thumbs);

        loadImages();
        UIHelper.loadKenBurnsHeader(crescento, top, getItem());
        loadSignatures(getItem().getSignatures(), view);
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
