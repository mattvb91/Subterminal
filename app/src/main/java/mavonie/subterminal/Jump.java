package mavonie.subterminal;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import mavonie.subterminal.ViewAdapters.JumpRecycler;
import mavonie.subterminal.Models.Model;

/**
 * Jump list fragment
 */
public class Jump extends Fragment {


    private OnJumpListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Jump() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jump_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            HashMap<String, String> params = new HashMap<>();
            params.put(Model.FILTER_ORDER_DIR, "DESC");
            params.put(Model.FILTER_ORDER_FIELD, mavonie.subterminal.Models.Jump.COLUMN_NAME_DATE);

            recyclerView.setAdapter(new JumpRecycler(new mavonie.subterminal.Models.Jump().getItems(params), mListener));
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnJumpListFragmentInteractionListener) {
            mListener = (OnJumpListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnJumpListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onJumpListFragmentInteraction(mavonie.subterminal.Models.Jump item);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        String title = getString(R.string.title_jumps) + " (" + new mavonie.subterminal.Models.Jump().count() + ")";
        MainActivity.getActivity().setTitle(title);
    }
}
