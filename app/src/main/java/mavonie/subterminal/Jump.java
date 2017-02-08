package mavonie.subterminal;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.FilterableFragment;
import mavonie.subterminal.Utils.Listeners.DatePickerTextView;
import mavonie.subterminal.Utils.UIHelper;
import mavonie.subterminal.ViewAdapters.JumpRecycler;

/**
 * Jump list fragment
 */
public class Jump extends FilterableFragment {

    /**
     * Filter fields
     */
    private Spinner jumpTypeSpinner;
    private EditText dateFrom, dateTo;
    private LinkedHashMapAdapter<Integer, String> typeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jump_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new JumpRecycler(new mavonie.subterminal.Models.Jump().getItems(buildFilterParams()), this.getmListener()));

        return view;
    }

    @Override
    protected int getPopupLayout() {
        return R.layout.fragment_jump_filter;
    }

    @Override
    public void populateFilter() {
        PopupWindow popupWindow = getFilterPopup();

        LinkedHashMap<Integer, String> types = new LinkedHashMap<>();
        types.put(null, " - ");
        types.putAll(mavonie.subterminal.Models.Jump.getTypes());

        typeAdapter = new LinkedHashMapAdapter<Integer, String>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, types);
        jumpTypeSpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.base_filter_jump_type);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jumpTypeSpinner.setAdapter(typeAdapter);

        dateFrom = (EditText) popupWindow.getContentView().findViewById(R.id.skydive_filter_date_from);
        dateTo = (EditText) popupWindow.getContentView().findViewById(R.id.skydive_filter_date_to);

        new DatePickerTextView(dateFrom);
        new DatePickerTextView(dateTo);
    }

    @Override
    protected HashMap<String, Object> buildFilterParams() {
        Query query = new Query();
        query.orderDir(mavonie.subterminal.Models.Jump.COLUMN_NAME_DATE, Model.FILTER_ORDER_DIR_DESC);
        query.getWheres().put(query.getWheres().size(), Synchronizable.getActiveParams());

        if (this.getArguments() != null) {
            Object jumpType = this.getArguments().get("jumpType");
            if (jumpType != null)
                query.addWhere(mavonie.subterminal.Models.Jump.COLUMN_NAME_TYPE, jumpType.toString());

            Object dateFrom = this.getArguments().get("dateFrom");
            if (dateFrom != null)
                query.addWhere(mavonie.subterminal.Models.Jump.COLUMN_NAME_DATE, dateFrom.toString(), Model.OPERATOR_GTEQ);

            Object dateTo = this.getArguments().get("dateTo");
            if (dateTo != null)
                query.addWhere(mavonie.subterminal.Models.Jump.COLUMN_NAME_DATE, dateTo.toString(), Model.OPERATOR_LTEQ);
        }

        return query.getParams();
    }

    @Override
    protected void filterButtonPressed() {
        popupWindow.dismiss();

        Fragment jump = new Jump();
        Bundle filters = new Bundle();

        Map.Entry type = typeAdapter.getItem(jumpTypeSpinner.getSelectedItemPosition());
        if (type.getKey() != null) {
            filters.putInt("jumpType", Integer.parseInt(type.getKey().toString()));
        }

        if (dateFrom.getText().length() > 0) {
            filters.putString("dateFrom", dateFrom.getText().toString());
        }

        if (dateTo.getText().length() > 0) {
            filters.putString("dateTo", dateTo.getText().toString());
        }

        jump.setArguments(filters);
        UIHelper.replaceFragment(jump);
    }

    @Override
    public void onResume() {
        super.onResume();

        String title = getString(R.string.title_jumps) + " (" + new mavonie.subterminal.Models.Jump().count(buildFilterParams()) + ")";
        MainActivity.getActivity().setTitle(title);
    }

    @Override
    protected String getItemClass() {
        return mavonie.subterminal.Models.Jump.class.getCanonicalName();
    }
}
