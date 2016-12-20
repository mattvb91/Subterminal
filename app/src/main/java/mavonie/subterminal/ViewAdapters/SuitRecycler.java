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
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Suit recycler
 */
public class SuitRecycler extends RecyclerView.Adapter<SuitRecycler.ViewHolder> {

    private final List<Suit> mValues;
    private final BaseFragment.OnFragmentInteractionListener mListener;

    public SuitRecycler(List<Suit> items, BaseFragment.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_suit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.listManufacturer.setText(holder.mItem.getManufacturer());
        holder.listModel.setText(holder.mItem.getModel());
        holder.listType.setText(holder.mItem.getFormattedSuitType());

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
        public final TextView listManufacturer;
        public final TextView listModel;
        public final TextView listType;
        public final ImageView mListSynchronized;
        public Suit mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            listManufacturer = (TextView) view.findViewById(R.id.suit_manufacturer);
            listModel = (TextView) view.findViewById(R.id.suit_model);
            listType = (TextView) view.findViewById(R.id.suit_type);
            mListSynchronized = (ImageView) view.findViewById(R.id.suit_list_synchronized);
        }
    }
}
