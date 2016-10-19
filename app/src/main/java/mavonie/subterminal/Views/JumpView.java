package mavonie.subterminal.Views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;

/**
 * Jump view
 */
public class JumpView extends BaseFragment {

    public JumpView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jump_view, container, false);
    }

    @Override
    protected String getItemClass() {
        return Exit.class.getCanonicalName();
    }
}
