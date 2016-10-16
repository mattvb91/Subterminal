package mavonie.subterminal.ViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mavonie.subterminal.Jump;
import mavonie.subterminal.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link mavonie.subterminal.models.Jump} and makes a call to the
 * specified {@link mavonie.subterminal.Jump.OnJumpListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class JumpRecycler extends RecyclerView.Adapter<JumpRecycler.ViewHolder> {

    private final List<mavonie.subterminal.models.Jump> mValues;
    private final Jump.OnJumpListFragmentInteractionListener mListener;

    public JumpRecycler(List<mavonie.subterminal.models.Jump> items, Jump.OnJumpListFragmentInteractionListener listener) {
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
        holder.jumpId.setText(Integer.toString(mValues.get(position).getId()));
        holder.exitName.setText(mValues.get(position).getExit().getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onJumpListFragmentInteraction(holder.mItem);
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
        public final TextView jumpId;
        public final TextView exitName;
        public mavonie.subterminal.models.Jump mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            jumpId = (TextView) view.findViewById(R.id.jump_list_id);
            exitName = (TextView) view.findViewById(R.id.just_list_exit_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + exitName.getText() + "'";
        }
    }
}
