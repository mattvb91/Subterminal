package mavonie.subterminal.Skydive.ViewAdapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
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

    static final int ITEM_TYPE_MODEL = 1;
    static final int ITEM_TYPE_AD = 2;

    static final int ITEMS_PER_AD = 20;

    private final List<Object> mValues;
    private final BaseFragment.OnFragmentInteractionListener mListener;

    public SkydiveRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {

        if (!Subterminal.getUser().isPremium() & !Subterminal.isTesting()) {
            List<Object> updatedList = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                if (i % ITEMS_PER_AD == 4) {
                    updatedList.add(null);
                }
                updatedList.add(items.get(i));
            }
            mValues = updatedList;
        } else {
            mValues = items;
        }

        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == ITEM_TYPE_MODEL) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_skydive, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ad_item, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_MODEL) {
            holder.mItem = (Skydive) mValues.get(position);

            if (holder.mItem.getDropzone() != null) {
                holder.listDropzone.setText(holder.mItem.getDropzone().getName());
            }

            if (holder.mItem.getDelay() != null) {
                holder.listDelay.setText("Delay: " + Integer.toString(holder.mItem.getDelay()) + "s");
            } else {
                holder.listDelay.setVisibility(View.GONE);
            }

            holder.listAgo.setText(TimeAgo.sinceToday(holder.mItem.getDate()));

            Image thumb = Image.loadThumbForEntity(holder.mItem);

            if (thumb != null) {
                holder.mThumb.setImageURI(thumb.getUri().toString());
            } else {
                holder.mThumb.setVisibility(View.GONE);
                holder.mView.findViewById(R.id.skydive_list_thumb_layout).setVisibility(View.GONE);
            }

            holder.listAircraft.setText("Aircraft: " + holder.mItem.getAircraft().getName());

            if ((Subterminal.getUser().isPremium() && holder.mItem.isSynced())) {
                int color = Color.parseColor(MainActivity.getActivity().getString(R.string.Synchronized));
                holder.mListSynchronized.setColorFilter(color);
            }

            holder.row_id.setText("#" + holder.mItem.getRowId());

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
        } else {
            AdView adview = (AdView) holder.mView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adview.loadAd(adRequest);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (!Subterminal.getUser().isPremium() & !Subterminal.isTesting()) {
            if (mValues.get(position) == null)
                return ITEM_TYPE_AD;
        }

        return ITEM_TYPE_MODEL;
    }

    @Override
    public int getItemCount() {
        if (!Subterminal.getUser().isPremium() & !Subterminal.isTesting()) {
            return mValues.size() + (mValues.size() / ITEMS_PER_AD);
        }

        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView listDropzone;
        public final TextView listAircraft;
        public final TextView listDelay;
        public final TextView listAgo;
        public final TextView row_id;
        public final SimpleDraweeView mThumb;
        public final ImageView mListSynchronized;

        public Skydive mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            listDropzone = (TextView) view.findViewById(R.id.skydive_list_dropzone);
            listAircraft = (TextView) view.findViewById(R.id.skydive_list_aircraft);
            listDelay = (TextView) view.findViewById(R.id.skydive_list_delay);
            listAgo = (TextView) view.findViewById(R.id.skydive_list_ago);
            row_id = (TextView) view.findViewById(R.id.skydive_list_row_id);
            mThumb = (SimpleDraweeView) view.findViewById(R.id.skydive_list_thumb);
            mListSynchronized = (ImageView) view.findViewById(R.id.skydive_list_synchronized);
        }
    }
}
