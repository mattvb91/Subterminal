package mavonie.subterminal.Skydive;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Skydive.Aircraft;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.R;
import mavonie.subterminal.Skydive.ViewAdapters.SkydiveRecycler;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.FilterableFragment;
import mavonie.subterminal.Utils.Listeners.DatePickerTextView;
import mavonie.subterminal.Utils.UIHelper;

/**
 * Skydive listings
 */
public class Skydive extends FilterableFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skydive_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(new SkydiveRecycler(new mavonie.subterminal.Models.Skydive.Skydive().getItems(buildFilterParams()), this.getmListener()));
        }

        return view;
    }

    @Override
    protected int getPopupLayout() {
        return R.layout.fragment_skydive_filter;
    }

    Spinner jumpTypeSpinner,
            aircraftSpinner;

    LinkedHashMapAdapter jumpTypesAdapter,
            aircraftsAdapter;

    EditText dateFrom, dateTo;
    CheckBox cutaway;

    @Override
    public void populateFilter() {
        LinkedHashMap<Integer, String> jumpTypes = new LinkedHashMap<>();
        jumpTypes.put(null, " - ");
        jumpTypes.putAll(mavonie.subterminal.Models.Skydive.Skydive.getJumpTypes());

        PopupWindow popupWindow = getFilterPopup(this);

        jumpTypeSpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.skydive_filter_jump_type);
        jumpTypesAdapter = new LinkedHashMapAdapter<Integer, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, jumpTypes);
        jumpTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jumpTypeSpinner.setAdapter(jumpTypesAdapter);

        LinkedHashMap aircrafts = new LinkedHashMap<>();
        aircrafts.put(null, " - ");
        aircrafts.putAll(new Aircraft().getItemsForSelect("name"));
        aircraftSpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.skydive_filter_aircraft);
        aircraftsAdapter = new LinkedHashMapAdapter<String, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, aircrafts);
        aircraftsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aircraftSpinner.setAdapter(aircraftsAdapter);

        cutaway = (CheckBox) popupWindow.getContentView().findViewById(R.id.skydive_filter_cutaway);
        dateFrom = (EditText) popupWindow.getContentView().findViewById(R.id.skydive_filter_date_from);
        dateTo = (EditText) popupWindow.getContentView().findViewById(R.id.skydive_filter_date_to);

        new DatePickerTextView(dateFrom);
        new DatePickerTextView(dateTo);

        //Prefil with previous selection
        if (this.getArguments() != null) {
            Object jumpType = this.getArguments().get("jumpType");
            if (jumpType != null)
                jumpTypeSpinner.setSelection(jumpTypesAdapter.findPositionFromKey((Integer) jumpType));

            Object aircraft = this.getArguments().get("aircraft");
            if (aircraft != null)
                aircraftSpinner.setSelection(aircraftsAdapter.findPositionFromKey((Integer) aircraft));

            Object cutawayChoice = this.getArguments().get("cutaway");
            if (cutawayChoice != null)
                cutaway.setChecked(true);

            Object dateFromChoice = this.getArguments().get("dateFrom");
            if (dateFromChoice != null)
                dateFrom.setText(dateFromChoice.toString());

            Object dateToChoice = this.getArguments().get("dateTo");
            if (dateToChoice != null)
                dateTo.setText(dateToChoice.toString());
        }
    }

    @Override
    protected HashMap<String, Object> buildFilterParams() {

        Query query = new Query();
        query.orderDir(mavonie.subterminal.Models.Skydive.Skydive.COLUMN_NAME_DATE, Model.FILTER_ORDER_DIR_DESC);
        query.getWheres().put(query.getWheres().size(), Synchronizable.getActiveParams());

        if (this.getArguments() != null) {
            Object jumpType = this.getArguments().get("jumpType");
            if (jumpType != null)
                query.addWhere(mavonie.subterminal.Models.Skydive.Skydive.COLUMN_NAME_JUMP_TYPE, jumpType.toString());

            if (this.getArguments().get("cutaway") != null)
                query.addWhere(mavonie.subterminal.Models.Skydive.Skydive.COLUMN_NAME_CUTAWAY, mavonie.subterminal.Models.Skydive.Skydive.CUTAWAY_YES);

            Object aircraft = this.getArguments().get("aircraft");
            if (aircraft != null)
                query.addWhere(mavonie.subterminal.Models.Skydive.Skydive.COLUMN_NAME_AIRCRAFT_ID, aircraft.toString());

            Object dateFrom = this.getArguments().get("dateFrom");
            if (dateFrom != null)
                query.addWhere(mavonie.subterminal.Models.Skydive.Skydive.COLUMN_NAME_DATE, dateFrom.toString(), Model.OPERATOR_GTEQ);

            Object dateTo = this.getArguments().get("dateTo");
            if (dateTo != null)
                query.addWhere(mavonie.subterminal.Models.Skydive.Skydive.COLUMN_NAME_DATE, dateTo.toString(), Model.OPERATOR_LTEQ);
        }

        return query.getParams();
    }

    @Override
    protected void filterButtonPressed() {
        popupWindow.dismiss();

        Fragment skydives = new Skydive();
        Bundle filters = new Bundle();

        Map.Entry type = jumpTypesAdapter.getItem(jumpTypeSpinner.getSelectedItemPosition());
        if (type.getKey() != null) {
            filters.putInt("jumpType", Integer.parseInt(type.getKey().toString()));
        }

        Map.Entry aircraft = aircraftsAdapter.getItem(aircraftSpinner.getSelectedItemPosition());
        if (aircraft.getKey() != null) {
            filters.putInt("aircraft", Integer.parseInt(aircraft.getKey().toString()));
        }

        if (cutaway.isChecked()) {
            filters.putBoolean("cutaway", true);
        }

        if (dateFrom.getText().length() > 0) {
            filters.putString("dateFrom", dateFrom.getText().toString());
        }

        if (dateTo.getText().length() > 0) {
            filters.putString("dateTo", dateTo.getText().toString());
        }

        skydives.setArguments(filters);
        UIHelper.replaceFragment(skydives);
    }

    @Override
    public void onResume() {
        super.onResume();

        String title = getString(R.string.title_skydives) + " (" + new mavonie.subterminal.Models.Skydive.Skydive().count(buildFilterParams()) + ")";
        MainActivity.getActivity().setTitle(title);
    }

    @Override
    protected String getItemClass() {
        return mavonie.subterminal.Models.Skydive.Skydive.class.getCanonicalName();
    }
}
