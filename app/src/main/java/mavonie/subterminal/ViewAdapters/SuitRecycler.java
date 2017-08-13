package mavonie.subterminal.ViewAdapters;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.BaseRecycler;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Suit recycler
 */
public class SuitRecycler extends BaseRecycler<SuitRecycler.ViewHolder> {

    public SuitRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {
        super(items, listener);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_suit;
    }

    @Override
    protected ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void bindCustomViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mItem = (Suit) mValues.get(position);
        viewHolder.listManufacturer.setText(viewHolder.mItem.getManufacturer());
        viewHolder.listModel.setText(viewHolder.mItem.getModel());
        viewHolder.listType.setText(viewHolder.mItem.getFormattedSuitType());
        viewHolder.listJumpCount.setText("Jumps: " + viewHolder.mItem.getJumpCount());

        if ((Subterminal.getUser().isPremium() && viewHolder.mItem.isSynced())) {
            int color = Color.parseColor(MainActivity.getActivity().getString(R.string.Synchronized));
            viewHolder.mListSynchronized.setColorFilter(color);
        }
    }

    public class ViewHolder extends BaseRecycler.ViewHolder {
        @BindView(R.id.suit_manufacturer) TextView listManufacturer;
        @BindView(R.id.suit_model) TextView listModel;
        @BindView(R.id.suit_type) TextView listType;
        @BindView(R.id.suit_jump_count) TextView listJumpCount;
        @BindView(R.id.suit_list_synchronized) ImageView mListSynchronized;
        public Suit mItem;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
