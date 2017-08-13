package mavonie.subterminal.Skydive.ViewAdapters;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import butterknife.BindView;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.BaseRecycler;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Date.TimeAgo;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Skydive recycler
 */
public class SkydiveRecycler extends BaseRecycler<SkydiveRecycler.ViewHolder> {

    public SkydiveRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {
        super(items, listener);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_skydive;
    }

    @Override
    protected ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void bindCustomViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mItem = (Skydive) mValues.get(position);

        if (viewHolder.mItem.getDropzone() != null) {
            viewHolder.listDropzone.setText(viewHolder.mItem.getDropzone().getName());
        }

        if (viewHolder.mItem.getDelay() != null) {
            viewHolder.listDelay.setText("Delay: " + Integer.toString(viewHolder.mItem.getDelay()) + "s");
        } else {
            viewHolder.listDelay.setVisibility(View.GONE);
        }

        viewHolder.listAgo.setText(TimeAgo.sinceToday(viewHolder.mItem.getDate()));

        Image thumb = Image.loadThumbForEntity(viewHolder.mItem);

        if (thumb != null) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(thumb.getUri()).setResizeOptions(new ResizeOptions(50, 50)).build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder().setOldController(viewHolder.mThumb.getController()).setImageRequest(request).build();
            viewHolder.mThumb.setController(controller);
            viewHolder.itemView.findViewById(R.id.skydive_list_thumb_layout).setVisibility(View.VISIBLE);
        } else {
            viewHolder.itemView.findViewById(R.id.skydive_list_thumb_layout).setVisibility(View.GONE);
        }

        if (viewHolder.mItem.getAircraft() != null) {
            viewHolder.listAircraft.setText("Aircraft: " + viewHolder.mItem.getAircraft().getName());
        }

        if ((Subterminal.getUser().isPremium() && viewHolder.mItem.isSynced())) {
            int color = Color.parseColor(MainActivity.getActivity().getString(R.string.Synchronized));
            viewHolder.mListSynchronized.setColorFilter(color);
        }

        viewHolder.row_id.setText("#" + viewHolder.mItem.getRowId());
    }

    public class ViewHolder extends BaseRecycler.ViewHolder {
        @BindView(R.id.skydive_list_dropzone) TextView listDropzone;
        @BindView(R.id.skydive_list_aircraft) TextView listAircraft;
        @BindView(R.id.skydive_list_delay) TextView listDelay;
        @BindView(R.id.skydive_list_ago) TextView listAgo;
        @BindView(R.id.skydive_list_row_id) TextView row_id;
        @BindView(R.id.skydive_list_thumb) SimpleDraweeView mThumb;
        @BindView(R.id.skydive_list_synchronized) ImageView mListSynchronized;
        public Skydive mItem;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
