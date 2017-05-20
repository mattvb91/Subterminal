package mavonie.subterminal.ViewAdapters;

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
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.BaseRecycler;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Date.TimeAgo;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Jump recycler
 */
public class JumpRecycler extends BaseRecycler<JumpRecycler.ViewHolder> {

    public JumpRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {
        super(items, listener);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_jump;
    }

    @Override
    protected ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void bindCustomViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mItem = (Jump) mValues.get(position);

        Exit exit = viewHolder.mItem.getExit();
        if (exit != null) {
            viewHolder.exitName.setText(exit.getName());
        } else {
            viewHolder.exitName.setText(MainActivity.getActivity().getString(R.string.no_exit_info));
            viewHolder.exitName.setTextColor(MainActivity.getActivity().getResources().getColor(R.color.grey));
        }

        String date = viewHolder.mItem.getDate();

        viewHolder.slider.setText("Slider: " + viewHolder.mItem.getFormattedSlider());
        viewHolder.delay.setText("Delay: " + viewHolder.mItem.getFormattedDelay());
        viewHolder.row_id.setText("#" + viewHolder.mItem.getRowId());

        if (date != null) {
            viewHolder.ago.setText(TimeAgo.sinceToday(date));
        }

        Image thumb = Image.loadThumbForEntity(viewHolder.mItem);

        if (thumb != null) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(thumb.getUri()).setResizeOptions(new ResizeOptions(50, 50)).build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder().setOldController(viewHolder.mThumb.getController()).setImageRequest(request).build();
            viewHolder.mThumb.setController(controller);
            viewHolder.mView.findViewById(R.id.jump_list_thumb_layout).setVisibility(View.VISIBLE);
        } else {
            viewHolder.mView.findViewById(R.id.jump_list_thumb_layout).setVisibility(View.GONE);
        }

        if ((Subterminal.getUser().isPremium() && viewHolder.mItem.isSynced())) {
            int color = Color.parseColor(MainActivity.getActivity().getString(R.string.Synchronized));
            viewHolder.mListSynchronized.setColorFilter(color);
        }
    }

    public class ViewHolder extends BaseRecycler.ViewHolder {
        @BindView(R.id.jump_list_exit_name) TextView exitName;
        @BindView(R.id.jump_list_ago) TextView ago;
        @BindView(R.id.jump_list_delay) TextView delay;
        @BindView(R.id.jump_list_slider) TextView slider;
        @BindView(R.id.jump_list_row_id) TextView row_id;
        @BindView(R.id.jump_list_synchronized) ImageView mListSynchronized;
        @BindView(R.id.jump_list_thumb) SimpleDraweeView mThumb;
        public Jump mItem;

        public ViewHolder(View view) {
            super(view);
        }
    }
}