package mavonie.subterminal.Views;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import mavonie.subterminal.Models.Image;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.UIHelper;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Image view fragment
 */
public class ImageView extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_view, container, false);

        final PhotoDraweeView image = (PhotoDraweeView) view.findViewById(R.id.image_view_image);
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setImageRequest(ImageRequestBuilder.newBuilderWithSource(getItem().getUri()).setResizeOptions(new ResizeOptions(300, 300)).build());
        controller.setOldController(image.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || image == null) {
                    return;
                }
                image.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        image.setController(controller.build());

        UIHelper.getAddButton().hide();

        return view;
    }

    @Override
    protected String getItemClass() {
        return Image.class.getCanonicalName();
    }

    @Override
    protected Image getItem() {
        return (Image) super.getItem();
    }
}
