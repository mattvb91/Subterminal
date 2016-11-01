package mavonie.subterminal.Views;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        PhotoDraweeView image = (PhotoDraweeView) view.findViewById(R.id.image_view_image);
        image.setPhotoUri(Uri.parse("file://" + Uri.parse(getItem().getFullPath())));

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
