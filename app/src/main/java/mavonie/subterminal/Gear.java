package mavonie.subterminal;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sa90.materialarcmenu.ArcMenu;

import java.util.HashMap;

import mavonie.subterminal.Forms.GearForm;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.UIHelper;
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

            HashMap<String, Object> whereNotDeleted = new HashMap<>();
            whereNotDeleted.put(Model.FILTER_WHERE_FIELD, Synchronizable.COLUMN_DELETED);
            whereNotDeleted.put(Model.FILTER_WHERE_VALUE, Synchronizable.DELETED_FALSE.toString());

            recyclerView.setAdapter(new GearRecycler(gear.getItems(whereNotDeleted), getmListener()));
        }
        return view;
    }


    @Override
    protected String getItemClass() {
        return mavonie.subterminal.Models.Gear.class.getCanonicalName();
    }

    @Override
    public void onPause() {
        super.onPause();

        ArcMenu arc = (ArcMenu) MainActivity.getActivity().findViewById(R.id.arcMenu);
        arc.setVisibility(View.GONE);

        if (arc.isMenuOpened()) {
            arc.toggleMenu();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        String title = getString(R.string.title_gear) + " (" + new mavonie.subterminal.Models.Gear().count() + ")";
        MainActivity.getActivity().setTitle(title);

        //We want to replace the add button with our arc menu
        UIHelper.getAddButton().setVisibility(View.GONE);
        final ArcMenu arc = (ArcMenu) MainActivity.getActivity().findViewById(R.id.arcMenu);
        arc.setVisibility(View.VISIBLE);

        FloatingActionButton rig = (FloatingActionButton) MainActivity.getActivity().findViewById(R.id.gear_menu_rig);
        rig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.replaceFragment(new GearForm());
                arc.toggleMenu();
            }
        });

    }
}
