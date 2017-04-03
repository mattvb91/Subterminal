package mavonie.subterminal.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;

import developer.shivam.library.CrescentoContainer;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.R;
import mavonie.subterminal.SignatureActivity;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Date.TimeAgo;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;

/**
 * Jump view
 */
public class JumpView extends BaseFragment {

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

        if (getItem().getExit() != null) {
            jumpExit.setText(getItem().getExit().getName());
        } else {
            jumpExit.setTextColor(MainActivity.getActivity().getResources().getColor(R.color.grey));
        }

        if (getItem().getGear() != null) {
            TextView jumpRig = (TextView) view.findViewById(R.id.jump_view_rig);
            jumpRig.setText(getItem().getGear().getDisplayName());
        }

        TextView delay = (TextView) view.findViewById(R.id.jump_view_delay);
        delay.setText(getItem().getFormattedDelay());

        TextView type = (TextView) view.findViewById(R.id.jump_view_jump_type);
        type.setText(getItem().getFormattedType());

        TextView jumpSlider = (TextView) view.findViewById(R.id.jump_view_slider);
        jumpSlider.setText(getItem().getFormattedSlider());

        TextView jumpPC = (TextView) view.findViewById(R.id.jump_view_pc);
        jumpPC.setText(Integer.toString(getItem().getPcSize()));

        if (getItem().getDescription() != null) {
            TextView jumpDescription = (TextView) view.findViewById(R.id.jump_view_description);
            jumpDescription.setText(getItem().getDescription().replace("\\n", "\n"));
        }

        if (getItem().getPcConfig() != null) {
            TextView pcConfig = (TextView) view.findViewById(R.id.jump_view_pc_config);
            pcConfig.setText(getItem().getFormattedPcConfig());
        } else {
            TableRow pcConfigRow = (TableRow) view.findViewById(R.id.pc_config_row);
            pcConfigRow.setVisibility(View.GONE);
        }

        this.imageLayout = (LinearLayout) view.findViewById(R.id.image_thumbs);

        loadImages();

        if (Image.loadThumbForEntity(getItem()) != null) {
            CrescentoContainer crescento = (CrescentoContainer) view.findViewById(R.id.crescentoContainer);
            crescento.setVisibility(View.VISIBLE);

            KenBurnsView top = (KenBurnsView) view.findViewById(R.id.kenburnsView);
            top.setImageURI((Image.loadThumbForEntity(getItem()).getUri()));
        }

        loadSignatures(getItem().getSignatures(), view);

        Button pictureButton = (Button) view.findViewById(R.id.jump_picture_button);
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
