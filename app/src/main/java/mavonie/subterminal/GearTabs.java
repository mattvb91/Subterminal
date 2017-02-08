package mavonie.subterminal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mavonie.subterminal.Forms.GearForm;
import mavonie.subterminal.Forms.SuitForm;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.DB.Query;
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
        RecyclerView view;

        Query query = new Query();
        query.getParams().putAll(Synchronizable.getActiveParams());

        if (getArguments() != null && getArguments().getInt(Gear.TAB) == Gear.TAB_RIGS) {
            view = (RecyclerView) inflater.inflate(R.layout.fragment_gear_list, container, false);
            adapter = new GearRecycler(new mavonie.subterminal.Models.Gear().getItems(query.getParams()), getmListener());
        } else {
            view = (RecyclerView) inflater.inflate(R.layout.fragment_suit_list, container, false);
            adapter = new SuitRecycler(new Suit().getItems(query.getParams()), getmListener());
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

        if (UIHelper.getArcMenu().isMenuOpened()) {
            UIHelper.getArcMenu().toggleMenu();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        MainActivity.getActivity().setTitle(getString(R.string.title_gear));

        //We want to replace the add button with our arc menu
        UIHelper.getAddButton().setVisibility(View.GONE);
        UIHelper.getArcMenu().setVisibility(View.VISIBLE);

        FloatingActionButton rig = (FloatingActionButton) MainActivity.getActivity().findViewById(R.id.gear_menu_rig);
        rig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.replaceFragment(new GearForm());
                UIHelper.getArcMenu().toggleMenu();
            }
        });

        FloatingActionButton suit = (FloatingActionButton) MainActivity.getActivity().findViewById(R.id.gear_menu_suit);
        suit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.replaceFragment(new SuitForm());
                UIHelper.getArcMenu().toggleMenu();
            }
        });

    }
}
