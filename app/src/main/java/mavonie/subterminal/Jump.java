package mavonie.subterminal;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.ViewAdapters.JumpRecycler;
import mavonie.subterminal.Models.Model;

/**
 * Jump list fragment
 */
public class Jump extends BaseFragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Jump() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jump_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            HashMap<String, String> params = new HashMap<>();
            params.put(Model.FILTER_ORDER_DIR, "DESC");
            params.put(Model.FILTER_ORDER_FIELD, mavonie.subterminal.Models.Jump.COLUMN_NAME_DATE);

            recyclerView.setAdapter(new JumpRecycler(new mavonie.subterminal.Models.Jump().getItems(params), this.getmListener()));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        String title = getString(R.string.title_jumps) + " (" + new mavonie.subterminal.Models.Jump().count() + ")";
        MainActivity.getActivity().setTitle(title);
    }

    @Override
    protected String getItemClass() {
        return mavonie.subterminal.Models.Jump.class.getCanonicalName();
    }

}
