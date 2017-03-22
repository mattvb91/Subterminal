package mavonie.subterminal.ViewAdapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Gear recycler
 */
public class GearRecycler extends RecyclerView.Adapter<GearRecycler.ViewHolder> {

    private final List<mavonie.subterminal.Models.Gear> mValues;
    private final BaseFragment.OnFragmentInteractionListener mListener;

    public GearRecycler(List<mavonie.subterminal.Models.Gear> items, BaseFragment.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_gear, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.listContainerManu.setText(holder.mItem.getContainerManufacturer());
        holder.listContainerType.setText(holder.mItem.getContainerType());
        holder.listCanopyType.setText(holder.mItem.getCanopyType());
        holder.listCanopyManufacturer.setText(holder.mItem.getCanopyManufacturer());
        holder.listJumpCount.setText("Jumps: " + new Jump().count(new Query(Jump.COLUMN_NAME_GEAR_ID, holder.mItem.getId()).getParams()));

        if ((Subterminal.getUser().isPremium() && holder.mItem.isSynced())) {
            int color = Color.parseColor(MainActivity.getActivity().getString(R.string.Synchronized));
            holder.mListSynchronized.setColorFilter(color);
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView listContainerManu;
        public final TextView listContainerType;
        public final TextView listCanopyType;
        public final TextView listCanopyManufacturer;
        public final TextView listJumpCount;
        public final ImageView mListSynchronized;
        public mavonie.subterminal.Models.Gear mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            listContainerManu = (TextView) view.findViewById(R.id.gear_container_manufacturer);
            listContainerType = (TextView) view.findViewById(R.id.gear_container_type);
            listCanopyType = (TextView) view.findViewById(R.id.gear_canopy_type);
            listCanopyManufacturer = (TextView) view.findViewById(R.id.gear_canopy_manufacturer);
            listJumpCount = (TextView) view.findViewById(R.id.gear_jump_count);
            mListSynchronized = (ImageView) view.findViewById(R.id.gear_list_synchronized);
        }
    }
}
