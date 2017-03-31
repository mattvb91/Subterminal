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
import mavonie.subterminal.Models.Skydive.TunnelSession;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Date.TimeAgo;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Tunnel recycler
 */
public class TunnelSessionRecycler extends RecyclerView.Adapter<TunnelSessionRecycler.ViewHolder> {

    static final int ITEM_TYPE_MODEL = 1;
    static final int ITEM_TYPE_AD = 2;

    static final int ITEMS_PER_AD = 20;

    private final List<Object> mValues;
    private final BaseFragment.OnFragmentInteractionListener mListener;

    public TunnelSessionRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {

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
                    .inflate(R.layout.fragment_tunnel_session, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ad_item, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_MODEL) {
            holder.mItem = (TunnelSession) mValues.get(position);
            holder.listRow.setText("#" + holder.mItem.getRowId());
            holder.listTunnel.setText(holder.mItem.getTunnel().getName());
            holder.listLength.setText("Length: " + holder.mItem.getLength().toString() + "s");

            holder.listAgo.setText(TimeAgo.sinceToday(holder.mItem.getDate()));

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
        public final TextView listRow;
        public final TextView listTunnel;
        public final TextView listLength;
        public final TextView listAgo;

        public TunnelSession mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            listRow = (TextView) view.findViewById(R.id.session_list_row_id);
            listTunnel = (TextView) view.findViewById(R.id.session_tunnel_name);
            listLength = (TextView) view.findViewById(R.id.session_length);
            listAgo = (TextView) view.findViewById(R.id.session_list_ago);
        }
    }
}
