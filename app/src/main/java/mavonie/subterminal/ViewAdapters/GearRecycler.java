package mavonie.subterminal.ViewAdapters;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Gear;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.BaseRecycler;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Gear recycler
 */
public class GearRecycler extends BaseRecycler<GearRecycler.ViewHolder> {

    public GearRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {
        super(items, listener);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_gear;
    }

    @Override
    protected ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void bindCustomViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mItem = (Gear) mValues.get(position);
        viewHolder.listContainerManu.setText(viewHolder.mItem.getContainerManufacturer());
        viewHolder.listContainerType.setText(viewHolder.mItem.getContainerType());
        viewHolder.listCanopyType.setText(viewHolder.mItem.getCanopyType());
        viewHolder.listCanopyManufacturer.setText(viewHolder.mItem.getCanopyManufacturer());
        viewHolder.listJumpCount.setText("Jumps: " + new Jump().count(new Query(Jump.COLUMN_NAME_GEAR_ID, viewHolder.mItem.getId()).getParams()));

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
        public Gear mItem;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
