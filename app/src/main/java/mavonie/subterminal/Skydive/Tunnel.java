package mavonie.subterminal.Skydive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
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
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.FilterableFragment;
import mavonie.subterminal.Utils.UIHelper;

/**
 * Tunnel Tabs
 */
public class Tunnel extends FilterableFragment {

    private FragmentTabHost mTabHost;

    public static final String TAB = "TAB";
    public static final String FILTER = "FILTERS";

    public static final int TAB_TUNNELS = 0;
    public static final int TAB_SESSIONS = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tabs, container, false);


        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        Bundle args = new Bundle();
        args.putInt(TAB, TAB_TUNNELS);
        args.putSerializable(FILTER, buildFilterParams());

        mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(TAB_TUNNELS)).setIndicator("Tunnels (" + new mavonie.subterminal.Models.Skydive.Tunnel().count(buildFilterParams()) + ")"),
                TunnelTabs.class, args);
        mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(TAB_SESSIONS)).setIndicator("Sessions (" +
                        new mavonie.subterminal.Models.Skydive.TunnelSession().count(Synchronizable.getActiveParams()) + ")"),
                TunnelTabs.class, null);

        return rootView;
    }

    @Override
    protected int getPopupLayout() {
        return R.layout.fragment_dropzone_filter;
    }

    @Override
    protected HashMap<String, Object> buildFilterParams() {
        Query query = new Query();
        query.orderDir(mavonie.subterminal.Models.Skydive.Tunnel.COLUMN_NAME_FEATURED, Model.FILTER_ORDER_DIR_DESC + ", name ASC");

        if (this.getArguments() != null) {
            Object country = this.getArguments().get("country");
            if (country != null) {
                query.addWhere(mavonie.subterminal.Models.Skydive.Tunnel.COLUMN_NAME_ADDRESS_LINE_COUNTRY, country.toString());
            }

            Object local = this.getArguments().get("local");
            if (local != null && local != Model.EMPTY_LIST_ITEM) {
                query.addWhere(mavonie.subterminal.Models.Skydive.Tunnel.COLUMN_NAME_ADDRESS_LEVEL_1, local.toString());
            }
        }

        return query.getParams();
    }

    @Override
    protected void filterButtonPressed() {
        popupWindow.dismiss();

        Fragment dropzones = new Tunnel();
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

    Spinner localSpinner, countrySpinner;

    @Override
    public void populateFilter() {
        final PopupWindow popupWindow = getFilterPopup(this);

        countrySpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.filter_country);

        ArrayList<String> countries = mavonie.subterminal.Models.Skydive.Tunnel.getCountriesForSelect();
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

                    ArrayAdapter<String> local = new ArrayAdapter(MainActivity.getActivity(), android.R.layout.simple_spinner_item, mavonie.subterminal.Models.Skydive.Tunnel.getCountiesForSelect(adapter.getItem(position).toString()));
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
    protected String getItemClass() {
        return mavonie.subterminal.Models.Skydive.Tunnel.class.getCanonicalName();
    }
}
