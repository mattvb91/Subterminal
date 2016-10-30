package mavonie.subterminal.Utils.Listeners;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Utils.UIHelper;
import mavonie.subterminal.Views.ImageView;

/**
 * Image onclick listener
 */
public class ImageListener implements View.OnClickListener {

    private int image_id;

    public ImageListener(Image image) {
        this.image_id = image.getId();
    }

    @Override
    public void onClick(View v) {
        Image image = (Image) new Image().getOneById(this.image_id);

        Bundle args = new Bundle();
        args.putSerializable("item", image);

        Fragment imageFragment = new ImageView();
        imageFragment.setArguments(args);

        UIHelper.replaceFragment(imageFragment);
    }
}
