package mavonie.subterminal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.FilterableFragment;
import mavonie.subterminal.Utils.UIHelper;
import mavonie.subterminal.ViewAdapters.GridViewAdapter;

/**
 * Gallery view
 */
public class Gallery extends FilterableFragment {

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    /**
     * Filter
     */
    Spinner imageTypeSpinner;
    LinkedHashMap<Integer, String> imageTypes;
    LinkedHashMapAdapter<Integer, String> imageTypeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        gridView = (GridView) inflater.inflate(R.layout.grid_view, container, false);
        gridAdapter = new GridViewAdapter(MainActivity.getActivity(), R.layout.grid_item_layout, new Image().getItems(buildFilterParams()));
        gridView.setAdapter(gridAdapter);

        return gridView;
    }

    @Override
    protected String getItemClass() {
        return Image.class.getCanonicalName();
    }

    @Override
    protected int getPopupLayout() {
        return R.layout.fragment_gallery_filter;
    }

    @Override
    public void populateFilter() {
        PopupWindow popupWindow = getFilterPopup(this);

        imageTypes = new LinkedHashMap<>();
        imageTypes.put(null, " - ");
        imageTypes.put(Image.ENTITY_TYPE_SKYDIVE, "Skydive");
        imageTypes.put(Image.ENTITY_TYPE_JUMP, "B.A.S.E.");
        imageTypes.put(Image.ENTITY_TYPE_EXIT, "Exit");

        imageTypeSpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.gallery_filter_image_type);
        imageTypeAdapter = new LinkedHashMapAdapter<>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, imageTypes);
        imageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageTypeSpinner.setAdapter(imageTypeAdapter);

        //Prefil with previous selection
        if (this.getArguments() != null) {
            Object imageType = this.getArguments().get("imageType");
            if (imageType != null)
                imageTypeSpinner.setSelection(imageTypeAdapter.findPositionFromKey((Integer) imageType));
        }
    }

    @Override
    protected HashMap<String, Object> buildFilterParams() {
        Query query = new Query(Image.COLUMN_NAME_ENTITY_TYPE, Image.ENTITY_TYPE_SIGNATURE, Model.OPERATOR_NOT_EQUALS);

        if (this.getArguments() != null) {
            Object imageType = this.getArguments().get("imageType");
            if (imageType != null)
                query.addWhere(Image.COLUMN_NAME_ENTITY_TYPE, imageType.toString());
        }

        return query.getParams();
    }

    @Override
    protected void filterButtonPressed() {
        popupWindow.dismiss();

        Fragment gallery = new Gallery();
        Bundle filters = new Bundle();

        Map.Entry entry = imageTypeAdapter.getItem(imageTypeSpinner.getSelectedItemPosition());

        if (entry.getKey() != null) {
            filters.putInt("imageType", Integer.parseInt(entry.getKey().toString()));
        }

        gallery.setArguments(filters);
        UIHelper.replaceFragment(gallery);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        MainActivity.getActivity().setTitle(getString(R.string.gallery));
    }
}
