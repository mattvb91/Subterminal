package mavonie.subterminal.Skydive;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sa90.materialarcmenu.ArcMenu;

import java.util.HashMap;

import mavonie.subterminal.Forms.SuitForm;
import mavonie.subterminal.Gear;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.UIHelper;
import mavonie.subterminal.ViewAdapters.GearRecycler;
import mavonie.subterminal.ViewAdapters.SuitRecycler;

/**
 * Gear list fragment
 */
public class GearTabs extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView.Adapter adapter;

        HashMap<String, Object> whereNotDeleted = new HashMap<>();
        whereNotDeleted.put(Model.FILTER_WHERE_FIELD, Synchronizable.COLUMN_DELETED);
        whereNotDeleted.put(Model.FILTER_WHERE_VALUE, Synchronizable.DELETED_FALSE.toString());

        RecyclerView view;

        if (getArguments() != null && getArguments().getInt(Gear.TAB) == Gear.TAB_RIGS) {
            mavonie.subterminal.Models.Gear gear = new mavonie.subterminal.Models.Gear();

            view = (RecyclerView) inflater.inflate(R.layout.fragment_gear_list, container, false);
            adapter = new GearRecycler(gear.getItems(whereNotDeleted), getmListener());
        } else {
            Suit suit = new Suit();

            view = (RecyclerView) inflater.inflate(R.layout.fragment_suit_list, container, false);
            adapter = new SuitRecycler(suit.getItems(whereNotDeleted), getmListener());
        }

        view.setLayoutManager(new LinearLayoutManager(view.getContext()));
        view.setAdapter(adapter);

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
                UIHelper.replaceFragment(new mavonie.subterminal.Skydive.Forms.GearForm());
                arc.toggleMenu();
            }
        });

        FloatingActionButton suit = (FloatingActionButton) MainActivity.getActivity().findViewById(R.id.gear_menu_suit);
        suit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.replaceFragment(new SuitForm());
                arc.toggleMenu();
            }
        });

    }
}
