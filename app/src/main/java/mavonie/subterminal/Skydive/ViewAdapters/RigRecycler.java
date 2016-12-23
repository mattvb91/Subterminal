package mavonie.subterminal.Skydive.ViewAdapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Skydive.Rig;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Skydive Rig recycler
 */
public class RigRecycler extends RecyclerView.Adapter<RigRecycler.ViewHolder> {

    private final List<Rig> mValues;
    private final BaseFragment.OnFragmentInteractionListener mListener;

    public RigRecycler(List<Rig> items, BaseFragment.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_rig, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.listContainerManu.setText(holder.mItem.getContainerManufacturer());
        holder.listContainerType.setText(holder.mItem.getContainerModel());
        holder.listCanopyType.setText(holder.mItem.getMainModel());
        holder.listCanopyManufacturer.setText(holder.mItem.getMainManufacturer());

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
        public final ImageView mListSynchronized;
        public Rig mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            listContainerManu = (TextView) view.findViewById(R.id.gear_container_manufacturer);
            listContainerType = (TextView) view.findViewById(R.id.gear_container_type);
            listCanopyType = (TextView) view.findViewById(R.id.gear_canopy_type);
            listCanopyManufacturer = (TextView) view.findViewById(R.id.gear_canopy_manufacturer);
            mListSynchronized = (ImageView) view.findViewById(R.id.gear_list_synchronized);
        }
    }
}
