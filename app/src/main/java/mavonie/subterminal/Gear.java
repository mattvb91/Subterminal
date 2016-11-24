package mavonie.subterminal;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
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
public class Gear extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Gear() {
    }

    private FragmentTabHost mTabHost;

    public static final String TAB = "TAB";

    public static final int TAB_RIGS = 0;
    public static final int TAB_SUITS = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tabs, container, false);


        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        Bundle args = new Bundle();
        args.putInt(TAB, TAB_RIGS);

        mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(TAB_RIGS)).setIndicator("Rigs"),
                GearTabs.class, args);
        mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(TAB_SUITS)).setIndicator("Suits"),
                GearTabs.class, null);

        return rootView;
    }
}
