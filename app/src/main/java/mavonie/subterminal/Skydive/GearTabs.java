package mavonie.subterminal.Skydive;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import mavonie.subterminal.Forms.SuitForm;
import mavonie.subterminal.Gear;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Skydive.Rig;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.R;
import mavonie.subterminal.Skydive.ViewAdapters.RigRecycler;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.UIHelper;
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
            Rig rig = new Rig();

            view = (RecyclerView) inflater.inflate(R.layout.fragment_gear_list, container, false);
            adapter = new RigRecycler(rig.getItems(whereNotDeleted), getmListener());
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
        UIHelper.getAddButton().hide();

        UIHelper.getArcMenu().setVisibility(View.VISIBLE);

        FloatingActionButton rig = (FloatingActionButton) MainActivity.getActivity().findViewById(R.id.gear_menu_rig);
        rig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.replaceFragment(new mavonie.subterminal.Skydive.Forms.GearForm());
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
