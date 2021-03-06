package mavonie.subterminal.Skydive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.R;
import mavonie.subterminal.Skydive.ViewAdapters.DropzoneRecycler;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.FilterableFragment;
import mavonie.subterminal.Utils.UIHelper;

/**
 * Dropzone listings
 */
public class Dropzone extends FilterableFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dropzone_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new DropzoneRecycler(new mavonie.subterminal.Models.Skydive.Dropzone().getItems(buildFilterParams()), this.getmListener()));

        return view;
    }

    @Override
    protected int getPopupLayout() {
        return R.layout.fragment_dropzone_filter;
    }

    @Override
    protected HashMap<String, Object> buildFilterParams() {
        Query query = new Query();
        query.orderDir(mavonie.subterminal.Models.Skydive.Dropzone.COLUMN_NAME_FEATURED, Model.FILTER_ORDER_DIR_DESC + ", name ASC");

        if (this.getArguments() != null) {
            Object country = this.getArguments().get("country");
            if (country != null) {
                query.addWhere(mavonie.subterminal.Models.Skydive.Dropzone.COLUMN_NAME_ADDRESS_LINE_COUNTRY, country.toString());
            }

            Object local = this.getArguments().get("local");
            if (local != null && local != Model.EMPTY_LIST_ITEM) {
                query.addWhere(mavonie.subterminal.Models.Skydive.Dropzone.COLUMN_NAME_ADDRESS_LEVEL_1, local.toString());
            }
        }

        return query.getParams();
    }

    @Override
    protected void filterButtonPressed() {
        popupWindow.dismiss();

        Fragment dropzones = new mavonie.subterminal.Skydive.Dropzone();
        Bundle filters = new Bundle();

        if (countrySpinner.getSelectedItem() != null) {
            filters.putString("country", countrySpinner.getSelectedItem().toString());
        }
        if (localSpinner.getSelectedItem() != null) {
            filters.putString("local", localSpinner.getSelectedItem().toString());
        }

        dropzones.setArguments(filters);
        UIHelper.replaceFragment(dropzones);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        String title = getString(R.string.title_dropzones) + " (" + new mavonie.subterminal.Models.Skydive.Dropzone().count(buildFilterParams()) + ")";
        MainActivity.getActivity().setTitle(title);
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_filter).setVisible(true);
    }

    @Override
    protected String getItemClass() {
        return mavonie.subterminal.Models.Skydive.Dropzone.class.getCanonicalName();
    }

    Spinner localSpinner, countrySpinner;

    @Override
    public void populateFilter() {
        final PopupWindow popupWindow = getFilterPopup(this);

        countrySpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.filter_country);

        ArrayList<String> countries = mavonie.subterminal.Models.Skydive.Dropzone.getCountriesForSelect();
        final ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.getActivity(), android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

        localSpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.filter_local);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    popupWindow.getContentView().findViewById(R.id.filter_local_text).setVisibility(View.VISIBLE);
                    localSpinner.setVisibility(View.VISIBLE);

                    ArrayAdapter<String> local = new ArrayAdapter(MainActivity.getActivity(), android.R.layout.simple_spinner_item, mavonie.subterminal.Models.Skydive.Dropzone.getCountiesForSelect(adapter.getItem(position)));
                    local.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    localSpinner.setAdapter(local);
                    localSpinner.invalidate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_filter).setVisible(false);
    }
}
