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
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;


/**
 * Exit Recycler
 */
public class ExitRecycler extends RecyclerView.Adapter<ExitRecycler.ViewHolder> {

    private final List<mavonie.subterminal.Models.Exit> mValues;
    private final BaseFragment.OnFragmentInteractionListener mListener;

    static final int THUMB_SIZE = 80;

    public ExitRecycler(List<mavonie.subterminal.Models.Exit> items, BaseFragment.OnFragmentInteractionListener listener) {
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

        holder.mHeight.setText(holder.mItem.getFormatedRockdrop());
        holder.mName.setText(holder.mItem.getName());
        holder.mObjectType.setText(holder.mItem.getFormattedObjectType());

        Image thumb = Image.loadThumbForEntity(holder.mItem);

        if (thumb != null) {
            holder.mThumb.setImageURI(thumb.getUri().toString());
        } else {
            holder.mThumb.setHierarchy(Image.getHierarchy());
        }

        if ((Subterminal.getUser().isPremium() && holder.mItem.isSynced()) || holder.mItem.isGlobal()) {
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

        thumb = null;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mHeight;
        public final TextView mName;
        public final TextView mObjectType;
        public final SimpleDraweeView mThumb;
        public final ImageView mListSynchronized;
        public mavonie.subterminal.Models.Exit mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mHeight = (TextView) view.findViewById(R.id.exit_list_height);
            mName = (TextView) view.findViewById(R.id.exit_list_name);
            mObjectType = (TextView) view.findViewById(R.id.exit_list_object_type);
            mListSynchronized = (ImageView) view.findViewById(R.id.exit_list_synchronized);

            mThumb = (SimpleDraweeView) view.findViewById(R.id.exit_list_thumb);
            mThumb.getLayoutParams().width = THUMB_SIZE;
            mThumb.setAdjustViewBounds(true);
            mThumb.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }
}
