package mavonie.subterminal.Skydive.ViewAdapters;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Date.TimeAgo;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Skydive recycler
 */
public class SkydiveRecycler extends RecyclerView.Adapter<SkydiveRecycler.ViewHolder> {

    private final List<Skydive> mValues;
    private final BaseFragment.OnFragmentInteractionListener mListener;

    public SkydiveRecycler(List<Skydive> items, BaseFragment.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_skydive, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        if (holder.mItem.getDropzone() != null) {
            holder.listDropzone.setText(holder.mItem.getDropzone().getName());
        }

        if (holder.mItem.getDelay() != null) {
            holder.listDelay.setText("Delay: " + Integer.toString(holder.mItem.getDelay()) + "s");
        }

        holder.listAgo.setText(TimeAgo.sinceToday(holder.mItem.getDate()));

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
        public final TextView listDropzone;
        public final TextView listDelay;
        public final TextView listAgo;
        public final SimpleDraweeView mThumb;
        public final ImageView mListSynchronized;

        public Skydive mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            listDropzone = (TextView) view.findViewById(R.id.skydive_list_dropzone);
            listDelay = (TextView) view.findViewById(R.id.skydive_list_delay);
            listAgo = (TextView) view.findViewById(R.id.skydive_list_ago);
            mThumb = (SimpleDraweeView) view.findViewById(R.id.skydive_list_thumb);
            mListSynchronized = (ImageView) view.findViewById(R.id.skydive_list_synchronized);
        }
    }
}
