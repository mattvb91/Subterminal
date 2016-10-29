package mavonie.subterminal;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.ViewAdapters.GearRecycler;

/**
 * Gear list fragment
 */
public class Gear extends BaseFragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Gear() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gear_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            mavonie.subterminal.Models.Gear gear = new mavonie.subterminal.Models.Gear();

            recyclerView.setAdapter(new GearRecycler(gear.getItems(null), getmListener()));
        }
        return view;
    }


    @Override
    protected String getItemClass() {
        return mavonie.subterminal.Models.Gear.class.getCanonicalName();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        String title = getString(R.string.title_gear) + " (" + new mavonie.subterminal.Models.Gear().count() + ")";
        MainActivity.getActivity().setTitle(title);
    }
}
