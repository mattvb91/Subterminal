package mavonie.subterminal.ViewAdapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Date.TimeAgo;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Jump recycler
 */
public class JumpRecycler extends RecyclerView.Adapter<JumpRecycler.ViewHolder> {

    private static final int THUMB_SIZE = 50;
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

        Exit exit = holder.mItem.getExit();
        if (exit != null) {
            holder.exitName.setText(exit.getName());
        } else {
            holder.exitName.setText(MainActivity.getActivity().getString(R.string.no_exit_info));
            holder.exitName.setTextColor(MainActivity.getActivity().getResources().getColor(R.color.grey));
        }

        String date = holder.mItem.getDate();

        holder.slider.setText("Slider: " + holder.mItem.getFormattedSlider());
        holder.delay.setText("Delay: " + holder.mItem.getFormattedDelay());
        holder.row_id.setText("#" + holder.mItem.getRowId());

        if (date != null) {
            holder.ago.setText(TimeAgo.sinceToday(date));
        }

        Image thumb = Image.loadThumbForEntity(holder.mItem);

        if (thumb != null) {
            holder.mThumb.setImageURI(thumb.getUri().toString());
        } else {
            holder.mThumb.setHierarchy(Image.getHierarchy());
        }

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
        public final TextView exitName;
        public final TextView ago;
        public final TextView delay;
        public final TextView slider;
        public final TextView row_id;
        public final ImageView mListSynchronized;
        public final SimpleDraweeView mThumb;

        public mavonie.subterminal.Models.Jump mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ago = (TextView) view.findViewById(R.id.jump_list_ago);
            exitName = (TextView) view.findViewById(R.id.jump_list_exit_name);
            delay = (TextView) view.findViewById(R.id.jump_list_delay);
            row_id = (TextView) view.findViewById(R.id.jump_list_row_id);
            slider = (TextView) view.findViewById(R.id.jump_list_slider);
            mThumb = (SimpleDraweeView) view.findViewById(R.id.jump_list_thumb);
            mListSynchronized = (ImageView) view.findViewById(R.id.jump_list_synchronized);
        }
    }
}
