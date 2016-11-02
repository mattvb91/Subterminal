package mavonie.subterminal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Exit extends Fragment {
    private FragmentTabHost mTabHost;

    public static final String TAB = "TAB";

    public static final int TAB_MY_EXITS = 0;
    public static final int TAB_ALL_EXITS = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tabs, container, false);


        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        Bundle args = new Bundle();
        args.putInt(TAB, TAB_MY_EXITS);

        mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(TAB_MY_EXITS)).setIndicator("My Exits"),
                ExitTabs.class, args);
        mTabHost.addTab(mTabHost.newTabSpec(Integer.toString(TAB_ALL_EXITS)).setIndicator("All"),
                ExitTabs.class, null);

        return rootView;
    }
}