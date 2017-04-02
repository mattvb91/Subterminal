package mavonie.subterminal.ViewAdapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UnitConverter;


/**
 * Exit Recycler
 */
public class ExitRecycler extends RecyclerView.Adapter<ExitRecycler.ViewHolder> {

    static final int ITEM_TYPE_MODEL = 1;
    static final int ITEM_TYPE_AD = 2;

    static final int ITEMS_PER_AD = 20;

    private final List<Object> mValues;
    private final BaseFragment.OnFragmentInteractionListener mListener;

    static final int THUMB_SIZE = 80;

    public ExitRecycler(List<Object> items, BaseFragment.OnFragmentInteractionListener listener) {

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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_exit, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ad_item, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_MODEL) {
            holder.mItem = (Exit) mValues.get(position);

            holder.mHeight.setText(UnitConverter.getFormattedDistance(holder.mItem.getRockdropDistance(), holder.mItem.getHeightUnit()));
            holder.mName.setText(holder.mItem.getName());
            holder.mObjectType.setText(holder.mItem.getFormattedObjectType());

            Image thumb = Image.loadThumbForEntity(holder.mItem);

            if (thumb != null) {
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(thumb.getUri()).setResizeOptions(new ResizeOptions(50, 50)).build();
                PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder().setOldController(holder.mThumb.getController()).setImageRequest(request).build();
                holder.mThumb.setController(controller);
                holder.mView.findViewById(R.id.exit_list_thumb_layout).setVisibility(View.VISIBLE);
            } else {
                holder.mView.findViewById(R.id.exit_list_thumb_layout).setVisibility(View.GONE);
            }

            if ((Subterminal.getUser().isPremium() && holder.mItem.isSynced()) || holder.mItem.isGlobal()) {
                int color = Color.parseColor(MainActivity.getActivity().getString(R.string.Synchronized));
                holder.mListSynchronized.setColorFilter(color);
            }

            int jumpCount = new Jump().count(new Query(Jump.COLUMN_NAME_EXIT_ID, holder.mItem.getId()).getParams());

            if (jumpCount > 0) {
                holder.mJumpCount.setVisibility(View.VISIBLE);
                holder.mJumpCount.setText("Jumps: " + jumpCount);
            } else {
                holder.mJumpCount.setVisibility(View.GONE);
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
        public final TextView mHeight;
        public final TextView mName;
        public final TextView mObjectType;
        public final TextView mJumpCount;
        public final SimpleDraweeView mThumb;
        public final ImageView mListSynchronized;
        public mavonie.subterminal.Models.Exit mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mHeight = (TextView) view.findViewById(R.id.exit_list_height);
            mName = (TextView) view.findViewById(R.id.exit_list_name);
            mObjectType = (TextView) view.findViewById(R.id.exit_list_object_type);
            mJumpCount = (TextView) view.findViewById(R.id.exit_list_jumps);
            mListSynchronized = (ImageView) view.findViewById(R.id.exit_list_synchronized);
            mThumb = (SimpleDraweeView) view.findViewById(R.id.exit_list_thumb);
        }
    }
}
