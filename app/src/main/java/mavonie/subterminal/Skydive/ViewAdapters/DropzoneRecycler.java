package mavonie.subterminal.Skydive.ViewAdapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Models.Skydive.DropzoneAircraft;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.BaseRecycler;
import mavonie.subterminal.Utils.BaseFragment;

/**
 * Dropzone recycler
 */
public class DropzoneRecycler extends BaseRecycler<DropzoneRecycler.ViewHolder> {

    public DropzoneRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {
        super(items, listener);
    }

    protected int getLayout() {
        return R.layout.fragment_dropzone;
    }

    @Override
    protected ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void bindCustomViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mItem = (Dropzone) mValues.get(position);
        viewHolder.listName.setText(viewHolder.mItem.getName());
        viewHolder.listCountry.setText(viewHolder.mItem.getCountry());
        viewHolder.listAircraft.setText(new DropzoneAircraft().count(viewHolder.mItem.aircraftParemeters()) + " Aircraft");
        CardView card = (CardView) viewHolder.mView.findViewById(R.id.card_view);

        if (viewHolder.mItem.getFeatured() == Dropzone.FEATURED_TRUE) {
            card.setCardBackgroundColor(Color.parseColor("#DBF1FF"));
        } else {
            card.setCardBackgroundColor(Color.WHITE);
        }
    }

    public class ViewHolder extends BaseRecycler.ViewHolder {
        @BindView(R.id.dropzone_list_name) TextView listName;
        @BindView(R.id.dropzone_list_country) TextView listCountry;
        @BindView(R.id.dropzone_list_aircraft) TextView listAircraft;
        public Dropzone mItem;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
