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
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.R;
import mavonie.subterminal.Skydive.ViewAdapters.SkydiveRecycler;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
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

    Spinner jumpTypeSpinner;
    LinkedHashMapAdapter jumpTypesAdapter;

    EditText dateFrom, dateTo;
    CheckBox cutaway;

    @Override
    public void populateFilter() {
        LinkedHashMap<Integer, String> jumpTypes = new LinkedHashMap<>();
        jumpTypes.put(null, " - ");
        jumpTypes.putAll(mavonie.subterminal.Models.Skydive.Skydive.getJumpTypes());

        PopupWindow popupWindow = getFilterPopup();

        jumpTypeSpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.skydive_filter_jump_type);
        jumpTypesAdapter = new LinkedHashMapAdapter<Integer, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, jumpTypes);
        jumpTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jumpTypeSpinner.setAdapter(jumpTypesAdapter);

        cutaway = (CheckBox) popupWindow.getContentView().findViewById(R.id.skydive_filter_cutaway);
        dateFrom = (EditText) popupWindow.getContentView().findViewById(R.id.skydive_filter_date_from);
        dateTo = (EditText) popupWindow.getContentView().findViewById(R.id.skydive_filter_date_to);

        new DatePickerTextView(dateFrom);
        new DatePickerTextView(dateTo);
    }

    @Override
    protected HashMap<String, Object> buildFilterParams() {

        HashMap<String, Object> params = new HashMap<>();

        params.put(Model.FILTER_ORDER_DIR, Model.FILTER_ORDER_DIR_DESC);
        params.put(Model.FILTER_ORDER_FIELD, mavonie.subterminal.Models.Jump.COLUMN_NAME_DATE);

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), Synchronizable.getActiveParams());

        if (this.getArguments() != null) {
            Object jumpType = this.getArguments().get("jumpType");
            if (jumpType != null) {

                HashMap<String, Object> whereJumpType = new HashMap<>();
                whereJumpType.put(Model.FILTER_WHERE_FIELD, mavonie.subterminal.Models.Skydive.Skydive.COLUMN_NAME_JUMP_TYPE);
                whereJumpType.put(Model.FILTER_WHERE_VALUE, "'" + jumpType.toString() + "'");
                wheres.put(wheres.size(), whereJumpType);
            }

            Object cutaway = this.getArguments().get("cutaway");
            if (cutaway != null) {
                HashMap<String, Object> whereCutaway = new HashMap<>();
                whereCutaway.put(Model.FILTER_WHERE_FIELD, mavonie.subterminal.Models.Skydive.Skydive.COLUMN_NAME_CUTAWAY);
                whereCutaway.put(Model.FILTER_WHERE_VALUE, "'" + mavonie.subterminal.Models.Skydive.Skydive.CUTAWAY_YES + "'");
                wheres.put(wheres.size(), whereCutaway);
            }

            Object dateFrom = this.getArguments().get("dateFrom");
            if (dateFrom != null) {
                HashMap<String, Object> whereDateFrom = new HashMap<>();
                whereDateFrom.put(Model.FILTER_WHERE_FIELD, mavonie.subterminal.Models.Skydive.Skydive.COLUMN_NAME_DATE);
                whereDateFrom.put(Model.FILTER_WHERE_VALUE, "'" + dateFrom.toString() + "'");
                whereDateFrom.put(Model.FILTER_WHERE_OPERATOR, ">=");
                wheres.put(wheres.size(), whereDateFrom);
            }

            Object dateTo = this.getArguments().get("dateTo");
            if (dateTo != null) {
                HashMap<String, Object> whereDateTo = new HashMap<>();
                whereDateTo.put(Model.FILTER_WHERE_FIELD, mavonie.subterminal.Models.Skydive.Skydive.COLUMN_NAME_DATE);
                whereDateTo.put(Model.FILTER_WHERE_VALUE, "'" + dateTo.toString() + "'");
                whereDateTo.put(Model.FILTER_WHERE_OPERATOR, "<=");
                wheres.put(wheres.size(), whereDateTo);
            }
        }

        params.put(Model.FILTER_WHERE, wheres);

        return params;
    }

    @Override
    protected void filterButtonPressed() {
        popupWindow.dismiss();

        Fragment skydives = new Skydive();
        Bundle filters = new Bundle();

        Map.Entry entry = jumpTypesAdapter.getItem(jumpTypeSpinner.getSelectedItemPosition());

        if (entry.getKey() != null) {
            filters.putInt("jumpType", Integer.parseInt(entry.getKey().toString()));
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
