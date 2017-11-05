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
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UnitConverter;


/**
 * Exit Recycler
 */
public class ExitRecycler extends BaseRecycler<ExitRecycler.ViewHolder> {

    public ExitRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {
        super(items, listener);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_exit;
    }

    @Override
    protected ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void bindCustomViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mItem = (Exit) mValues.get(position);

        viewHolder.mHeight.setText(UnitConverter.getFormattedDistance(viewHolder.mItem.getRockdropDistance(), viewHolder.mItem.getHeightUnit()));
        viewHolder.mName.setText(viewHolder.mItem.getName());
        viewHolder.mObjectType.setText(viewHolder.mItem.getFormattedObjectType());

        Image thumb = Image.loadThumbForEntity(viewHolder.mItem);

        if (thumb != null) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(thumb.getUri()).setResizeOptions(new ResizeOptions(50, 50)).build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder().setOldController(viewHolder.mThumb.getController()).setImageRequest(request).build();
            viewHolder.mThumb.setController(controller);
            viewHolder.mView.findViewById(R.id.exit_list_thumb_layout).setVisibility(View.VISIBLE);
        } else {
            viewHolder.mView.findViewById(R.id.exit_list_thumb_layout).setVisibility(View.GONE);
        }

        if ((Subterminal.getUser().isPremium() && viewHolder.mItem.isSynced())) {
            int color = Color.parseColor(MainActivity.getActivity().getString(R.string.Synchronized));
            viewHolder.mListSynchronized.setColorFilter(color);
        }

        int jumpCount = new Jump().count(new Query(Jump.COLUMN_NAME_EXIT_ID, viewHolder.mItem.getId()).getParams());

        if (jumpCount > 0) {
            viewHolder.mJumpCount.setVisibility(View.VISIBLE);
            viewHolder.mJumpCount.setText("Jumps: " + jumpCount);
        } else {
            viewHolder.mJumpCount.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends BaseRecycler.ViewHolder {
        @BindView(R.id.exit_list_height) TextView mHeight;
        @BindView(R.id.exit_list_name) TextView mName;
        @BindView(R.id.exit_list_object_type) TextView mObjectType;
        @BindView(R.id.exit_list_jumps) TextView mJumpCount;
        @BindView(R.id.exit_list_thumb) SimpleDraweeView mThumb;
        @BindView(R.id.exit_list_synchronized) ImageView mListSynchronized;
        public Exit mItem;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
