package mavonie.subterminal.ViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;

import java.util.List;

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
        public mavonie.subterminal.Models.Gear mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            listContainerManu = (TextView) view.findViewById(R.id.gear_container_manufacturer);
            listContainerType = (TextView) view.findViewById(R.id.gear_container_type);
            listCanopyType = (TextView) view.findViewById(R.id.gear_canopy_type);
            listCanopyManufacturer = (TextView) view.findViewById(R.id.gear_canopy_manufacturer);
        }
    }
}
