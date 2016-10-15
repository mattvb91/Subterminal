package mavonie.subterminal.ViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mavonie.subterminal.Gear.OnListFragmentInteractionListener;
import mavonie.subterminal.R;
import mavonie.subterminal.models.Gear;
import mavonie.subterminal.models.Model;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link mavonie.subterminal.models.Gear} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GearRecycler extends RecyclerView.Adapter<GearRecycler.ViewHolder> {

    private final List<mavonie.subterminal.models.Gear> mValues;
    private final OnListFragmentInteractionListener mListener;

    public GearRecycler(List<mavonie.subterminal.models.Gear> items, OnListFragmentInteractionListener listener) {
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
        holder.mIdView.setText(mValues.get(position).getContainerManufacturer());
        holder.mContentView.setText(mValues.get(position).getContainerType());
        holder.listContentCanopyType.setText(mValues.get(position).getCanopyType());

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
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView listContentCanopyType;
        public mavonie.subterminal.models.Gear mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.gear_container_manufacturer);
            mContentView = (TextView) view.findViewById(R.id.gear_container_type);
            listContentCanopyType = (TextView) view.findViewById(R.id.gear_canopy_type);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
