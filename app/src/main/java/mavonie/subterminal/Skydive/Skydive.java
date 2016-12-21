package mavonie.subterminal.Skydive;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;
import mavonie.subterminal.Skydive.ViewAdapters.DropzoneRecycler;
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

            recyclerView.setAdapter(new SkydiveRecycler(new mavonie.subterminal.Models.Skydive.Skydive().getItems(null), this.getmListener()));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        String title = getString(R.string.title_skydives) + " (" + new mavonie.subterminal.Models.Skydive.Skydive().count() + ")";
        MainActivity.getActivity().setTitle(title);
    }

    @Override
    protected String getItemClass() {
        return mavonie.subterminal.Models.Skydive.Skydive.class.getCanonicalName();
    }

}
