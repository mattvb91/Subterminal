package mavonie.subterminal.ViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import mavonie.subterminal.Exit;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Views.SquareImageView;


import java.util.List;


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

        holder.mHeight.setText(mValues.get(position).getFormatedRockdrop());
        holder.mName.setText(mValues.get(position).getName());

        Image thumb = Image.loadThumbForEntity(mValues.get(position));

        if (thumb != null) {
            holder.mThumb.setImageBitmap(thumb.decodeSampledBitmapFromResource(THUMB_SIZE, THUMB_SIZE));
        }else{
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
        public final TextView mHeight;
        public final TextView mName;
        public final SquareImageView mThumb;
        public mavonie.subterminal.Models.Exit mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mHeight = (TextView) view.findViewById(R.id.exit_list_height);
            mName = (TextView) view.findViewById(R.id.exit_list_name);

            mThumb = (SquareImageView) view.findViewById(R.id.exit_list_thumb);
            mThumb.getLayoutParams().width = THUMB_SIZE;
            mThumb.setAdjustViewBounds(true);
            mThumb.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }
}
