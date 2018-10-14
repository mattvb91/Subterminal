package mavonie.subterminal.Views;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import developer.shivam.crescento.CrescentoContainer;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.R;
import mavonie.subterminal.SignatureActivity;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;

/**
 * Jump view
 */
public class JumpView extends BaseFragment {

    @BindView(R.id.jump_view_date) TextView jumpDate;
    @BindView(R.id.jump_view_exit_name) TextView jumpExit;
    @BindView(R.id.jump_view_rig) TextView jumpRig;
    @BindView(R.id.jump_view_delay) TextView delay;
    @BindView(R.id.jump_view_jump_type) TextView type;
    @BindView(R.id.jump_view_slider) TextView jumpSlider;
    @BindView(R.id.jump_view_pc) TextView jumpPC;
    @BindView(R.id.jump_view_description) TextView jumpDescription;
    @BindView(R.id.jump_view_pc_config) TextView pcConfig;
    @BindView(R.id.pc_config_row) TableRow pcConfigRow;
    @BindView(R.id.crescentoContainer) CrescentoContainer crescento;
    @BindView(R.id.kenburnsView) KenBurnsView top;

    @OnClick(R.id.jump_picture_button) void pickImage() {
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
        View view = inflater.inflate(R.layout.fragment_jump_view, container, false);
        ButterKnife.bind(this, view);

        jumpDate.setText(getItem().getDate());

        if (getItem().getExit() != null) {
            jumpExit.setText(getItem().getExit().getName());
        } else {
            jumpExit.setTextColor(ContextCompat.getColor(MainActivity.getActivity(), R.color.grey));
        }

        if (getItem().getGear() != null) {
            jumpRig.setText(getItem().getGear().getDisplayName());
        }

        delay.setText(getItem().getFormattedDelay());
        type.setText(getItem().getFormattedType());
        jumpSlider.setText(getItem().getFormattedSlider());
        jumpPC.setText(Integer.toString(getItem().getPcSize()));

        if (getItem().getDescription() != null) {
            jumpDescription.setText(getItem().getDescription().replace("\\n", "\n"));
        }

        if (getItem().getPcConfig() != null) {
            pcConfig.setText(getItem().getFormattedPcConfig());
        } else {
            pcConfigRow.setVisibility(View.GONE);
        }

        this.imageLayout = (LinearLayout) view.findViewById(R.id.image_thumbs);

        loadImages();
        UIHelper.loadKenBurnsHeader(crescento, top, getItem());
        loadSignatures(getItem().getSignatures(), view);

        adRequest(view);

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
