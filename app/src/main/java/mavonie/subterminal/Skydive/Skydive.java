package mavonie.subterminal.Skydive;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.R;
import mavonie.subterminal.Skydive.ViewAdapters.SkydiveRecycler;
import mavonie.subterminal.Utils.BaseFragment;

/**
 * Skydive listings
 */
public class Skydive extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skydive_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            HashMap<String, Object> params = new HashMap<>();

            params.put(Model.FILTER_ORDER_DIR, Model.FILTER_ORDER_DIR_DESC);
            params.put(Model.FILTER_ORDER_FIELD, mavonie.subterminal.Models.Jump.COLUMN_NAME_DATE);

            HashMap<String, Object> whereNotDeleted = new HashMap<>();
            whereNotDeleted.put(Model.FILTER_WHERE_FIELD, Synchronizable.COLUMN_DELETED);
            whereNotDeleted.put(Model.FILTER_WHERE_VALUE, Synchronizable.DELETED_FALSE.toString());

            HashMap<Integer, HashMap> wheres = new HashMap<>();
            wheres.put(wheres.size(), whereNotDeleted);

            params.put(Model.FILTER_WHERE, wheres);

            recyclerView.setAdapter(new SkydiveRecycler(new mavonie.subterminal.Models.Skydive.Skydive().getItems(params), this.getmListener()));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        HashMap<String, Object> whereNotDeleted = new HashMap<>();
        whereNotDeleted.put(Model.FILTER_WHERE_FIELD, Synchronizable.COLUMN_DELETED);
        whereNotDeleted.put(Model.FILTER_WHERE_VALUE, Synchronizable.DELETED_FALSE.toString());

        String title = getString(R.string.title_skydives) + " (" + new mavonie.subterminal.Models.Skydive.Skydive().count(whereNotDeleted) + ")";
        MainActivity.getActivity().setTitle(title);
    }

    @Override
    protected String getItemClass() {
        return mavonie.subterminal.Models.Skydive.Skydive.class.getCanonicalName();
    }

}
