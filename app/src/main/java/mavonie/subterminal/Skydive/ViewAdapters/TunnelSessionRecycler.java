package mavonie.subterminal.Skydive.ViewAdapters;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import mavonie.subterminal.Models.Skydive.TunnelSession;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.BaseRecycler;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Date.TimeAgo;

/**
 * Tunnel Sessions Recycler
 */
public class TunnelSessionRecycler extends BaseRecycler<TunnelSessionRecycler.ViewHolder> {

    public TunnelSessionRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {
        super(items, listener);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_tunnel_session;
    }

    @Override
    protected ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void bindCustomViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mItem = (TunnelSession) mValues.get(position);
        viewHolder.listRow.setText("#" + viewHolder.mItem.getRowId());

        if (viewHolder.mItem.getTunnel() != null) {
            viewHolder.listTunnel.setText(viewHolder.mItem.getTunnel().getName());
        }

        viewHolder.listLength.setText("Length: " + viewHolder.mItem.getLength().toString() + "s");
        viewHolder.listAgo.setText(TimeAgo.sinceToday(viewHolder.mItem.getDate()));
    }

    public class ViewHolder extends BaseRecycler.ViewHolder {
        @BindView(R.id.session_list_row_id) TextView listRow;
        @BindView(R.id.session_tunnel_name) TextView listTunnel;
        @BindView(R.id.session_length) TextView listLength;
        @BindView(R.id.session_list_ago) TextView listAgo;
        public TunnelSession mItem;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
