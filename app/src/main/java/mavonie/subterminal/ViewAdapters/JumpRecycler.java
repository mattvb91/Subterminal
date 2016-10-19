package mavonie.subterminal.ViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mavonie.subterminal.Jump;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Date.DateFormat;
import mavonie.subterminal.Utils.Date.TimeAgo;
import mavonie.subterminal.Models.Exit;

import java.util.List;

/**
 * Jump recycler
 */
public class JumpRecycler extends RecyclerView.Adapter<JumpRecycler.ViewHolder> {

    private final List<mavonie.subterminal.Models.Jump> mValues;
    private final BaseFragment.OnFragmentInteractionListener mListener;

    public JumpRecycler(List<mavonie.subterminal.Models.Jump> items, BaseFragment.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_jump, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Exit exit = mValues.get(position).getExit();
        if (exit != null) {
            holder.exitName.setText(mValues.get(position).getExit().getName());
        } else {
            holder.exitName.setText("No exit info");
            holder.exitName.setTextColor(MainActivity.getActivity().getResources().getColor(R.color.grey));
        }

        DateFormat df = new DateFormat();
        String date = mValues.get(position).getDate();

        if (date != null) {
            holder.ago.setText(TimeAgo.sinceToday(date));
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
        public final TextView exitName;
        public final TextView ago;
        public mavonie.subterminal.Models.Jump mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ago = (TextView) view.findViewById(R.id.jump_list_ago);
            exitName = (TextView) view.findViewById(R.id.jump_list_exit_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + exitName.getText() + "'";
        }
    }
}
