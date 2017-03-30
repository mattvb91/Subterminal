package mavonie.subterminal.Skydive.ViewAdapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Models.Skydive.Tunnel;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Tunnel recycler
 */
public class TunnelRecycler extends RecyclerView.Adapter<TunnelRecycler.ViewHolder> {

    static final int ITEM_TYPE_MODEL = 1;
    static final int ITEM_TYPE_AD = 2;

    static final int ITEMS_PER_AD = 20;

    private final List<Object> mValues;
    private final BaseFragment.OnFragmentInteractionListener mListener;

    public TunnelRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {

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
                    .inflate(R.layout.fragment_tunnel, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ad_item, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_MODEL) {
            holder.mItem = (Tunnel) mValues.get(position);
            holder.listName.setText(holder.mItem.getName());
            holder.listCountry.setText(holder.mItem.getCountry());
            CardView card = (CardView) holder.mView.findViewById(R.id.card_view);

            if (holder.mItem.getFeatured() == Dropzone.FEATURED_TRUE) {
                card.setCardBackgroundColor(Color.parseColor("#DBF1FF"));
            }else {
                card.setCardBackgroundColor(Color.WHITE);
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
         return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView listName;
        public final TextView listCountry;

        public Tunnel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            listName = (TextView) view.findViewById(R.id.tunnel_list_name);
            listCountry = (TextView) view.findViewById(R.id.tunnel_list_country);
        }
    }
}