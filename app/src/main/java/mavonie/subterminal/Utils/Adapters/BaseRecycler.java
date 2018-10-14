package mavonie.subterminal.Utils.Adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;

/**
 * BaseRecycler class
 * Abstracts all the common functionality into here. Mainly just checking & handling wether
 * the current slot is an AdView or its normal view.
 */
public abstract class BaseRecycler<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected static final int ITEM_TYPE_MODEL = 1;
    protected static final int ITEM_TYPE_AD = 2;

    static final int ITEMS_PER_AD = 20;

    protected final List<Object> mValues;
    protected final BaseFragment.OnFragmentInteractionListener mListener;

    public BaseRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {
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
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == ITEM_TYPE_MODEL) {
            view = LayoutInflater.from(parent.getContext()).inflate(this.getLayout(), parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ad_item, parent, false);
        }

        return getViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {

        if (getItemViewType(position) == ITEM_TYPE_MODEL) {
            bindCustomViewHolder(holder, position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onFragmentInteraction((Model) mValues.get(position));
                    }
                }
            });

        } else {
            AdView adview = (AdView) holder.itemView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adview.loadAd(adRequest);
        }
    }

    protected abstract int getLayout();

    protected abstract VH getViewHolder(View view);

    protected abstract void bindCustomViewHolder(VH viewHolder, int position);

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

        public ViewHolder(View itemView) {
            super(itemView);

            //If its an Ad view dont bind through butterknife
            if (this.itemView.getId() != R.id.ad_card_view)
                ButterKnife.bind(this, itemView);

            mView = itemView;
        }
    }
}
