package mavonie.subterminal.ViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mavonie.subterminal.Exit;
import mavonie.subterminal.R;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link mavonie.subterminal.models.Exit} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ExitRecycler extends RecyclerView.Adapter<ExitRecycler.ViewHolder> {

    private final List<mavonie.subterminal.models.Exit> mValues;
    private final Exit.OnListFragmentInteractionListener mListener;

    public ExitRecycler(List<mavonie.subterminal.models.Exit> items, Exit.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_exit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mHeight.setText(mValues.get(position).getFormatedRockdrop());
        holder.mName.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onExitListFragmentInteraction(holder.mItem);
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
        public final TextView mHeight;
        public final TextView mName;
        public mavonie.subterminal.models.Exit mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mHeight = (TextView) view.findViewById(R.id.exit_list_height);
            mName = (TextView) view.findViewById(R.id.exit_list_name);
        }
    }
}
