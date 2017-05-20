package mavonie.subterminal.Skydive.ViewAdapters;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Skydive.Rig;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.BaseRecycler;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Skydive Rig recycler
 */
public class RigRecycler extends BaseRecycler<RigRecycler.ViewHolder> {

    public RigRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {
        super(items, listener);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_rig;
    }

    @Override
    protected ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void bindCustomViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mItem = (Rig) mValues.get(position);
        viewHolder.listContainerManu.setText(viewHolder.mItem.getContainerManufacturer());
        viewHolder.listContainerType.setText(viewHolder.mItem.getContainerModel());
        viewHolder.listCanopyType.setText(viewHolder.mItem.getMainModel());
        viewHolder.listCanopyManufacturer.setText(viewHolder.mItem.getMainManufacturer());
        viewHolder.listJumpCount.setText("Jumps: " + new Skydive().count(new Query(Skydive.COLUMN_NAME_JUMP_RIG_ID, viewHolder.mItem.getId()).getParams()));

        if ((Subterminal.getUser().isPremium() && viewHolder.mItem.isSynced())) {
            int color = Color.parseColor(MainActivity.getActivity().getString(R.string.Synchronized));
            viewHolder.mListSynchronized.setColorFilter(color);
        }
    }

    public class ViewHolder extends BaseRecycler.ViewHolder {
        @BindView(R.id.gear_container_manufacturer) TextView listContainerManu;
        @BindView(R.id.gear_container_type) TextView listContainerType;
        @BindView(R.id.gear_canopy_type) TextView listCanopyType;
        @BindView(R.id.gear_canopy_manufacturer) TextView listCanopyManufacturer;
        @BindView(R.id.gear_jump_count) TextView listJumpCount;
        @BindView(R.id.gear_list_synchronized) ImageView mListSynchronized;
        public Rig mItem;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
