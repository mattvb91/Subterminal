package mavonie.subterminal;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.ViewAdapters.ExitRecycler;

/**
 * Exit list fragment
 */
public class Exit extends BaseFragment {


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Exit() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exit_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            HashMap<String, Object> params = new HashMap<>();
            params.put(Model.FILTER_ORDER_DIR, Model.FILTER_ORDER_DIR_ASC);
            params.put(Model.FILTER_ORDER_FIELD, mavonie.subterminal.Models.Exit.COLUMN_NAME_NAME);

            recyclerView.setAdapter(new ExitRecycler(new mavonie.subterminal.Models.Exit().getItems(params), getmListener()));
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        String title = getString(R.string.title_exit) + " (" + new mavonie.subterminal.Models.Exit().count() + ")";
        MainActivity.getActivity().setTitle(title);
    }

    @Override
    protected String getItemClass() {
        return mavonie.subterminal.Models.Exit.class.getCanonicalName();
    }
}
