package mavonie.subterminal.Skydive;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mavonie.subterminal.Models.Skydive.Rig;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.R;

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

        mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(TAB_RIGS)).setIndicator("Rigs (" + new Rig().count(Rig.getActiveParams()) + ")"),
                mavonie.subterminal.Skydive.GearTabs.class, args);
        mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(TAB_SUITS)).setIndicator("Suits (" + new Suit().count(Suit.getActiveParams()) + ")"),
                mavonie.subterminal.Skydive.GearTabs.class, null);

        return rootView;
    }
}
