package mavonie.subterminal.ViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mavonie.subterminal.Gear.OnListFragmentInteractionListener;
import mavonie.subterminal.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link mavonie.subterminal.Models.Gear} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GearRecycler extends RecyclerView.Adapter<GearRecycler.ViewHolder> {

    private final List<mavonie.subterminal.Models.Gear> mValues;
    private final OnListFragmentInteractionListener mListener;

    public GearRecycler(List<mavonie.subterminal.Models.Gear> items, OnListFragmentInteractionListener listener) {
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
        holder.listContainerManu.setText(mValues.get(position).getContainerManufacturer());
        holder.listContainerType.setText(mValues.get(position).getContainerType());
        holder.listCanopyType.setText(mValues.get(position).getCanopyType());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onGearListFragmentInteraction(holder.mItem);
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
        public mavonie.subterminal.Models.Gear mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            listContainerManu = (TextView) view.findViewById(R.id.gear_container_manufacturer);
            listContainerType = (TextView) view.findViewById(R.id.gear_container_type);
            listCanopyType = (TextView) view.findViewById(R.id.gear_canopy_type);
        }
    }
}
