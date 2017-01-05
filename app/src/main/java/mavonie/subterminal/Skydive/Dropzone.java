package mavonie.subterminal.Skydive;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.R;
import mavonie.subterminal.Skydive.ViewAdapters.DropzoneRecycler;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.UIHelper;

/**
 * Dropzone listings
 */
public class Dropzone extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dropzone_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            List items = null;

            HashMap<String, Object> params = new HashMap<>();

            params.put(Model.FILTER_ORDER_DIR, Model.FILTER_ORDER_DIR_ASC);
            params.put(Model.FILTER_ORDER_FIELD, mavonie.subterminal.Models.Exit.COLUMN_NAME_NAME);

            if (this.getArguments() == null) {
                items = new mavonie.subterminal.Models.Skydive.Dropzone().getItems(params);
            } else {
                Object country = this.getArguments().get("country");
                Object local = this.getArguments().get("local");

                HashMap<Integer, HashMap> wheres = new HashMap<>();

                if (country != null) {
                    HashMap<String, Object> whereCountry = new HashMap<>();
                    whereCountry.put(Model.FILTER_WHERE_FIELD, mavonie.subterminal.Models.Skydive.Dropzone.COLUMN_NAME_ADDRESS_LINE_COUNTRY);
                    whereCountry.put(Model.FILTER_WHERE_VALUE, "'" + country.toString() + "'");
                    wheres.put(wheres.size(), whereCountry);
                }

                if (local != null && local != Model.EMPTY_LIST_ITEM) {
                    HashMap<String, Object> whereLocal = new HashMap<>();
                    whereLocal.put(Model.FILTER_WHERE_FIELD, mavonie.subterminal.Models.Skydive.Dropzone.COLUMN_NAME_ADDRESS_LEVEL_1);
                    whereLocal.put(Model.FILTER_WHERE_VALUE, "'" + local.toString() + "'");
                    wheres.put(wheres.size(), whereLocal);
                }

                params.put(Model.FILTER_WHERE, wheres);
                items = new mavonie.subterminal.Models.Skydive.Dropzone().getItems(params);
            }

            recyclerView.setAdapter(new DropzoneRecycler(items, this.getmListener()));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        String title = getString(R.string.title_dropzones) + " (" + new mavonie.subterminal.Models.Skydive.Dropzone().count() + ")";
        MainActivity.getActivity().setTitle(title);
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_filter).setVisible(true);
    }

    @Override
    protected String getItemClass() {
        return mavonie.subterminal.Models.Skydive.Dropzone.class.getCanonicalName();
    }

    public static void filterPopup() {
        final View popupView = MainActivity.getActivity().getLayoutInflater().inflate(R.layout.fragment_dropzone_filter, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        final Spinner countrySpinner = (Spinner) popupView.findViewById(R.id.filter_country);

        ArrayList<String> countries = mavonie.subterminal.Models.Skydive.Dropzone.getCountriesForSelect();
        final ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.getActivity(), android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

        final Spinner localSpinner = (Spinner) popupView.findViewById(R.id.filter_local);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    popupView.findViewById(R.id.filter_local_text).setVisibility(View.VISIBLE);
                    localSpinner.setVisibility(View.VISIBLE);

                    ArrayAdapter<String> local = new ArrayAdapter(MainActivity.getActivity(), android.R.layout.simple_spinner_item, mavonie.subterminal.Models.Skydive.Dropzone.getCountiesForSelect(adapter.getItem(position).toString()));
                    local.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    localSpinner.setAdapter(local);
                    localSpinner.invalidate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        final Button filterButton = (Button) popupView.findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        _mListener = null;
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_filter).setVisible(false);
    }
}
