package mavonie.subterminal.ViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import mavonie.subterminal.Jump;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Date.DateFormat;
import mavonie.subterminal.Utils.Date.TimeAgo;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Utils.Views.SquareImageView;

import java.util.List;

/**
 * Jump recycler
 */
public class JumpRecycler extends RecyclerView.Adapter<JumpRecycler.ViewHolder> {

    private static final int THUMB_SIZE = 80;
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
            holder.exitName.setText(holder.mItem.getExit().getName());
        } else {
            holder.exitName.setText("No exit info");
            holder.exitName.setTextColor(MainActivity.getActivity().getResources().getColor(R.color.grey));
        }

        String date = holder.mItem.getDate();

        if (date != null) {
            holder.ago.setText(TimeAgo.sinceToday(date));
        }

        Image thumb = Image.loadThumbForEntity(holder.mItem);

        if (thumb != null) {
            holder.mThumb.setImageURI(thumb.getUri());
            holder.mThumb.setVisibility(View.VISIBLE);
        } else {
            holder.mThumb.setVisibility(View.INVISIBLE);
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
        public final SimpleDraweeView mThumb;

        public mavonie.subterminal.Models.Jump mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ago = (TextView) view.findViewById(R.id.jump_list_ago);
            exitName = (TextView) view.findViewById(R.id.jump_list_exit_name);
            mThumb = (SimpleDraweeView) view.findViewById(R.id.jump_list_thumb);
        }
    }
}
