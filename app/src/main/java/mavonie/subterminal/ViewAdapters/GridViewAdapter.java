package mavonie.subterminal.ViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Listeners.ImageListener;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Created by mavon on 16/01/17.
 */

public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;

    private List<Model> images;

    public GridViewAdapter(Context context, int layoutResourceId, List<Model> images) {
        super(context, layoutResourceId, images);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (SimpleDraweeView) row.findViewById(R.id.thumbnail);
            holder.mListSynchronized = (ImageView) row.findViewById(R.id.synchronized_indicator);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        Image item = (Image) images.get(position);

        if ((Subterminal.getUser().isPremium() && item.isSynced())) {
            int color = Color.parseColor(MainActivity.getActivity().getString(R.string.Synchronized));
            holder.mListSynchronized.setColorFilter(color);
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(item.getUri()).setResizeOptions(new ResizeOptions(100, 100)).build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder().setOldController(holder.image.getController()).setImageRequest(request).build();
        holder.image.setController(controller);
        holder.image.setOnClickListener(new ImageListener(item));

        return row;
    }

    static class ViewHolder {
        SimpleDraweeView image;
        ImageView mListSynchronized;
    }
}