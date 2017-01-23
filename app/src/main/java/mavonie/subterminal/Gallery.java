package mavonie.subterminal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.ViewAdapters.GridViewAdapter;

/**
 * Gallery view
 */
public class Gallery extends BaseFragment {

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        gridView = (GridView) inflater.inflate(R.layout.grid_view, container, false);
        gridAdapter = new GridViewAdapter(MainActivity.getActivity(), R.layout.grid_item_layout, new Image().getActiveItems());
        gridView.setAdapter(gridAdapter);

        return gridView;
    }

    @Override
    protected String getItemClass() {
        return Image.class.getCanonicalName();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        MainActivity.getActivity().setTitle(getString(R.string.gallery));
    }
}
