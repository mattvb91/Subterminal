package mavonie.subterminal.Skydive.ViewAdapters;

import android.graphics.Color;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Models.Skydive.Tunnel;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.BaseRecycler;
import mavonie.subterminal.Utils.BaseFragment;

/**
 * Tunnel recycler
 */
public class TunnelRecycler extends BaseRecycler<TunnelRecycler.ViewHolder> {

    public TunnelRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {
        super(items, listener);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_tunnel;
    }

    @Override
    protected ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void bindCustomViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mItem = (Tunnel) mValues.get(position);
        viewHolder.listName.setText(viewHolder.mItem.getName());
        viewHolder.listCountry.setText(viewHolder.mItem.getCountry());
        CardView card = (CardView) viewHolder.mView.findViewById(R.id.card_view);

        if (viewHolder.mItem.getFeatured() == Dropzone.FEATURED_TRUE) {
            card.setCardBackgroundColor(Color.parseColor("#DBF1FF"));
        } else {
            card.setCardBackgroundColor(Color.WHITE);
        }
    }

    public class ViewHolder extends BaseRecycler.ViewHolder {
        @BindView(R.id.tunnel_list_name) TextView listName;
        @BindView(R.id.tunnel_list_country) TextView listCountry;
        public Tunnel mItem;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
