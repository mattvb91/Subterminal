package mavonie.subterminal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.ViewAdapters.ExitRecycler;

/**
 * Exit list fragment
 */
public class Exit extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exit_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new ExitRecycler(new mavonie.subterminal.Models.Exit().getItems(getQuery().getParams()), getmListener()));

        return view;
    }

    @NonNull
    private Query getQuery() {
        Query query = new Query();
        query.orderDir(mavonie.subterminal.Models.Exit.COLUMN_NAME_NAME, Model.FILTER_ORDER_DIR_ASC);
        query.getParams().putAll(Synchronizable.getActiveParams());

        return query;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Set title
        String title = getString(R.string.title_exit) + " (" + new mavonie.subterminal.Models.Exit().count(getQuery().getParams()) + ")";
        MainActivity.getActivity().setTitle(title);
    }

    @Override
    protected String getItemClass() {
        return mavonie.subterminal.Models.Exit.class.getCanonicalName();
    }
}
