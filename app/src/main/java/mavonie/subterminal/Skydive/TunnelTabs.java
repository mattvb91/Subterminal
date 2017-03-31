package mavonie.subterminal.Skydive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

import mavonie.subterminal.*;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Skydive.ViewAdapters.TunnelRecycler;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.FilterableFragment;
import mavonie.subterminal.Utils.UIHelper;
import mavonie.subterminal.ViewAdapters.GearRecycler;
import mavonie.subterminal.ViewAdapters.SuitRecycler;

/**
 * Tunnel listings/Session listings
 */
public class TunnelTabs extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView.Adapter adapter = null;
        RecyclerView view;

        if (getArguments() != null && getArguments().getInt(Tunnel.TAB) == Tunnel.TAB_TUNNELS) {
            view = (RecyclerView) inflater.inflate(R.layout.fragment_dropzone_list, container, false);
            adapter = new TunnelRecycler(new mavonie.subterminal.Models.Skydive.Tunnel().getItems((HashMap<String, Object>) this.getArguments().get(Tunnel.FILTER)), getmListener());
            UIHelper.getAddButton().hide();
        } else {
            view = (RecyclerView) inflater.inflate(R.layout.fragment_suit_list, container, false);
//            adapter = new SuitRecycler(new Suit().getItems(query.getParams()), getmListener());
            UIHelper.getAddButton().show();
        }

        view.setLayoutManager(new LinearLayoutManager(view.getContext()));
        view.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        String title = getString(R.string.title_tunnels);
        MainActivity.getActivity().setTitle(title);
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_filter).setVisible(true);
    }

    @Override
    protected String getItemClass() {
        return mavonie.subterminal.Models.Skydive.Dropzone.class.getCanonicalName();
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_filter).setVisible(false);
    }
}
