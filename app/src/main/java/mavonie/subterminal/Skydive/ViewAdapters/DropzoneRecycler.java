package mavonie.subterminal.Skydive.ViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Models.Skydive.DropzoneAircraft;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;

/**
 * Suit recycler
 */
public class DropzoneRecycler extends RecyclerView.Adapter<DropzoneRecycler.ViewHolder> {

    private final List<Dropzone> mValues;
    private final BaseFragment.OnFragmentInteractionListener mListener;

    public DropzoneRecycler(List<Dropzone> items, BaseFragment.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dropzone, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.listName.setText(holder.mItem.getName());
        holder.listCountry.setText(holder.mItem.getCountry());
        holder.listAircraft.setText(new DropzoneAircraft().count(holder.mItem.aircraftParemeters()) + " Aircraft");

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
        public final TextView listName;
        public final TextView listCountry;
        public final TextView listAircraft;

        public mavonie.subterminal.Models.Skydive.Dropzone mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            listName = (TextView) view.findViewById(R.id.dropzone_list_name);
            listCountry = (TextView) view.findViewById(R.id.dropzone_list_country);
            listAircraft = (TextView) view.findViewById(R.id.dropzone_list_aircraft);
        }
    }
}
