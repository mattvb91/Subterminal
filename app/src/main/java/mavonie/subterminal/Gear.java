package mavonie.subterminal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Models.Synchronizable;


/**
 * Gear list fragment
 */
public class Gear extends Fragment {

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

        mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(TAB_RIGS)).setIndicator("Rigs (" + new mavonie.subterminal.Models.Gear().count(Synchronizable.getActiveParams()) + ")"),
                GearTabs.class, args);
        mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(TAB_SUITS)).setIndicator("Suits (" + new Suit().count(Synchronizable.getActiveParams()) + ")"),
                GearTabs.class, null);

        return rootView;
    }
}
