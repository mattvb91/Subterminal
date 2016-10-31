package mavonie.subterminal.Utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import mavonie.subterminal.Exit;
import mavonie.subterminal.Forms.ExitForm;
import mavonie.subterminal.Forms.GearForm;
import mavonie.subterminal.Forms.JumpForm;
import mavonie.subterminal.Gear;
import mavonie.subterminal.Jump;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.R;
import mavonie.subterminal.Views.ExitView;
import mavonie.subterminal.Views.JumpView;

/**
 * Class to deal with UI/Fragment navigation components
 */
public class UIHelper {

    //private static final int FRAGMENT_HOME = R.id.nav_home;
    private static final int FRAGMENT_JUMPS = R.id.nav_jumps;
    private static final int FRAGMENT_GEAR = R.id.nav_gear;
    private static final int FRAGMENT_EXIT = R.id.nav_exits;

    private static FloatingActionButton addButton;

    /**
     * @return FloatingActionButton
     */
    public static FloatingActionButton getAddButton() {
        return addButton;
    }

    /**
     * Check which entity we have and open the relevant fragment
     *
     * @param entity
     */
    public static void openFragmentForEntity(Model entity) {

        getAddButton().hide();

        Bundle args = new Bundle();
        args.putSerializable("item", entity);

        BaseFragment fragment = null;

        if (entity instanceof mavonie.subterminal.Models.Exit) {
            fragment = new ExitView();
        } else if (entity instanceof mavonie.subterminal.Models.Gear) {
            fragment = new GearForm();
        } else if (entity instanceof mavonie.subterminal.Models.Jump) {
            fragment = new JumpView();
        }

        fragment.setArguments(args);

        replaceFragment(fragment);
    }

    public static void editEntity() {

        Model activeModel = Subterminal.getActiveModel();

        Bundle args = new Bundle();
        args.putSerializable("item", activeModel);

        Fragment fragment = null;

        if (activeModel.canEdit()) {
            if (activeModel instanceof mavonie.subterminal.Models.Exit) {
                fragment = new ExitForm();
            } else if (activeModel instanceof mavonie.subterminal.Models.Jump) {
                fragment = new JumpForm();
            }
            fragment.setArguments(args);

            replaceFragment(fragment);
        }
    }

    public static void replaceFragment(Fragment fragment) {
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.flContent, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(null).commit();
    }

    /**
     * @param id
     */
    public static void goToFragment(int id) {
        BaseFragment fragmentClass = null;

        switch (id) {
//            case R.id.nav_home:
//                fragmentClass = Home.class;
//                fab.hide();
//                break;
            case R.id.nav_jumps:
                fragmentClass = new Jump();
                getAddButton().show();
                break;
            case R.id.nav_gear:
                fragmentClass = new Gear();
                getAddButton().show();
                break;
            case R.id.nav_login:
                return;
            case R.id.nav_exits:
                fragmentClass = new Exit();
                getAddButton().show();
                break;
        }

        Subterminal.setActiveFragment(id);
        Subterminal.setActiveModel(null);

        replaceFragment(fragmentClass);

        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_delete).setVisible(false);
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_edit).setVisible(false);
    }

    public static void deleteDialog() {
        new AlertDialog.Builder(MainActivity.getActivity())
                .setTitle("Confirm delete")
                .setMessage("Delete this item?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Subterminal.getActiveModel().delete();
                        UIHelper.goToFragment(R.id.nav_jumps); //TODO proper navigation
                        Toast.makeText(MainActivity.getActivity(), "Item has been deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    /**
     * Init the addButton and set the onclick listener available on
     * multiple fragments
     */
    public static void initAddButton() {

        Subterminal.setActiveFragment(R.id.nav_jumps);

        addButton = (FloatingActionButton) MainActivity.getActivity().findViewById(R.id.fab);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Subterminal.getActiveFragment()) {
                    case FRAGMENT_GEAR:
                        UIHelper.replaceFragment(new GearForm());
                        addButton.hide();
                        break;
                    case FRAGMENT_EXIT:
                        UIHelper.replaceFragment(new ExitForm());
                        addButton.hide();
                        break;
                    case FRAGMENT_JUMPS:
                        UIHelper.replaceFragment(new JumpForm());
                        addButton.hide();
                        break;
                }
            }
        });
    }
}
