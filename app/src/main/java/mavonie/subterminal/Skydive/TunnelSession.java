package mavonie.subterminal.Skydive;

import java.util.HashMap;

import mavonie.subterminal.Utils.FilterableFragment;

/**
 * Tunnel Sessions
 */
public class TunnelSession extends FilterableFragment {

    @Override
    protected int getPopupLayout() {
        return 0;
    }

    @Override
    public void populateFilter() {

    }

    @Override
    protected HashMap<String, Object> buildFilterParams() {
        return null;
    }

    @Override
    protected void filterButtonPressed() {

    }

    @Override
    protected String getItemClass() {
        return mavonie.subterminal.Models.Skydive.TunnelSession.class.getCanonicalName();
    }
}
