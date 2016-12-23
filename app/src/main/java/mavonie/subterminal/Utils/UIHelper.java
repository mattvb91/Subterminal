package mavonie.subterminal.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Arrays;
import java.util.List;

import mavonie.subterminal.Exit;
import mavonie.subterminal.Forms.ExitForm;
import mavonie.subterminal.Forms.GearForm;
import mavonie.subterminal.Forms.JumpForm;
import mavonie.subterminal.Forms.SuitForm;
import mavonie.subterminal.Gear;
import mavonie.subterminal.Jump;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Skydive.Rig;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Preference;
import mavonie.subterminal.R;
import mavonie.subterminal.Skydive.Dropzone;
import mavonie.subterminal.Skydive.Forms.SkydiveForm;
import mavonie.subterminal.Skydive.Views.DropzoneView;
import mavonie.subterminal.Skydive.Views.SkydiveView;
import mavonie.subterminal.Views.ExitView;
import mavonie.subterminal.Views.JumpView;
import mavonie.subterminal.Views.Premium.PremiumView;
import uk.me.lewisdeane.ldialogs.CustomListDialog;

/**
 * Class to deal with UI/Fragment navigation components
 * Make sure all changes are made on the UI thread
 */
public class UIHelper {

    private static final int FRAGMENT_JUMPS = R.id.nav_jumps;
    private static final int FRAGMENT_GEAR = R.id.nav_gear;
    private static final int FRAGMENT_EXIT = R.id.nav_exits;
    private static final int FRAGMENT_SKYDIVES = R.id.skydiving_nav_jumps;

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
        } else if (entity instanceof Suit) {
            fragment = new SuitForm();
        } else if (entity instanceof mavonie.subterminal.Models.Skydive.Dropzone) {
            fragment = new DropzoneView();
        } else if (entity instanceof Skydive) {
            fragment = new SkydiveView();
        } else if (entity instanceof Rig) {
            fragment = new mavonie.subterminal.Skydive.Forms.GearForm();
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
            } else if (activeModel instanceof Skydive) {
                fragment = new SkydiveForm();
            }

            fragment.setArguments(args);

            replaceFragment(fragment);
        }
    }

    public static void replaceFragment(Fragment fragment) {
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.flContent, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(null).commitAllowingStateLoss();
    }

    /**
     * @param id
     */
    public static void goToFragment(int id) {
        Fragment fragmentClass = null;

        switch (id) {
            case R.id.nav_jumps:
                fragmentClass = new Jump();
                getAddButton().show();
                break;

            case R.id.nav_gear:
                fragmentClass = new Gear();
                getAddButton().show();
                break;

            case R.id.nav_exits:
                fragmentClass = new Exit();
                getAddButton().show();
                break;

            case R.id.nav_settings:
                fragmentClass = new Preference();
                getAddButton().hide();
                break;

            case R.id.nav_premium:
                fragmentClass = new PremiumView();
                break;

            case R.id.skydiving_nav_jumps:
                fragmentClass = new mavonie.subterminal.Skydive.Skydive();
                getAddButton().show();
                break;

            case R.id.skydiving_nav_dropzones:
                fragmentClass = new Dropzone();
                getAddButton().hide();
                break;

            case R.id.skydiving_nav_gear:
                fragmentClass = new mavonie.subterminal.Skydive.Gear();
                getAddButton().show();
                break;
        }

        Subterminal.setActiveFragment(id);
        Subterminal.setActiveModel(null);

        replaceFragment(fragmentClass);

        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_delete).setVisible(false);
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_edit).setVisible(false);
    }

    public static void switchModeDialog() {

        // Create list dialog with required parameters - context, title, and our array of items to fill the list.
        String[] modes = {Subterminal.MODE_BASE, Subterminal.MODE_SKYDIVING};

        CustomListDialog.Builder builder = new CustomListDialog.Builder(MainActivity.getActivity(), "Mode", modes);
        CustomListDialog customListDialog = builder.build();
        customListDialog.show();

        customListDialog.setListClickListener(new CustomListDialog.ListClickListener() {
            @Override
            public void onListItemSelected(int i, String[] strings, String s) {
                switchMode(s);
            }
        });
    }

    /**
     * Switch to requested mode
     *
     * @param mode
     */
    private static void switchMode(String mode) {
        Prefs.putString(Preference.PREFS_MODE, mode);

        if (mode.equals(Subterminal.MODE_BASE)) {
            //Switch to B.A.S.E mode
            MainActivity.getActivity().getNavigationView().getMenu().setGroupVisible(R.id.menu_group_BASE, true);
            MainActivity.getActivity().getNavigationView().getMenu().setGroupVisible(R.id.menu_group_SKYDIVING, false);

        } else {
            //Switch to Skydiving mode
            MainActivity.getActivity().getNavigationView().getMenu().setGroupVisible(R.id.menu_group_SKYDIVING, true);
            MainActivity.getActivity().getNavigationView().getMenu().setGroupVisible(R.id.menu_group_BASE, false);
        }

        MainActivity.getActivity().getNavigationView().getMenu().findItem(R.id.nav_mode).setTitle(mode);
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
                        toast(MainActivity.getActivity().getString(R.string.delete_confirmation));
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
                    case FRAGMENT_SKYDIVES:
                        UIHelper.replaceFragment(new SkydiveForm());
                        addButton.hide();
                        break;
                }
            }
        });
    }

    public static void facebookDialog() {

        LoginManager.getInstance().logOut();

        List<String> permissionNeeds = Arrays.asList("email", "user_birthday", "user_friends");

        LoginManager.getInstance().logInWithReadPermissions(MainActivity.getActivity(), permissionNeeds);
        LoginManager.getInstance().registerCallback(Subterminal.getmCallbackManager(),

                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResults) {
                        Subterminal.getUser().setFacebookToken(loginResults.getAccessToken());
                        Subterminal.getUser().init();

                        userLoggedIn();
                        toast(MainActivity.getActivity().getString(R.string.login_success));
                    }

                    @Override
                    public void onCancel() {
                        Log.e("dd", "facebook login canceled");
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Log.e("dd", "facebook login failed error");
                    }
                });
    }

    /**
     * Update layout for logged in user
     */
    public static void userLoggedIn() {
        MenuItem item = (MenuItem) MainActivity.getActivity().getNavigationView().getMenu().findItem(R.id.nav_login);
        item.setVisible(false);

        NavigationView nav = MainActivity.getActivity().getNavigationView();
        View headerView = nav.getHeaderView(0);

        ImageView logo = (ImageView) headerView.findViewById(R.id.nav_header_icon);
        logo.setVisibility(View.GONE);

        ProfilePictureView profilePictureView = (ProfilePictureView) headerView.findViewById(R.id.profile_pic);
        profilePictureView.setProfileId(Subterminal.getUser().getFacebookToken().getUserId());
        profilePictureView.setVisibility(View.VISIBLE);
        profilePictureView.setPresetSize(ProfilePictureView.SMALL);
        profilePictureView.getLayoutParams().width = 100;
        profilePictureView.getLayoutParams().height = 100;

        TextView profileName = (TextView) headerView.findViewById(R.id.profile_name);
        profileName.setText(Subterminal.getUser().getEmail());
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) profileName.getLayoutParams();
        params.addRule(RelativeLayout.END_OF, R.id.profile_pic);
        profileName.setLayoutParams(params);
    }

    /**
     * Update layout for logged out user
     */
    public static void userLoggedOut() {

        Handler mainHandler = new Handler(MainActivity.getActivity().getMainLooper());

        Runnable uiRunnable = new Runnable() {
            @Override
            public void run() {
                MenuItem item = MainActivity.getActivity().getNavigationView().getMenu().findItem(R.id.nav_login);
                item.setVisible(true);

                NavigationView nav = MainActivity.getActivity().getNavigationView();
                View headerView = nav.getHeaderView(0);

                ProfilePictureView profilePictureView = (ProfilePictureView) headerView.findViewById(R.id.profile_pic);
                profilePictureView.setVisibility(View.GONE);

                TextView appDescriptor = (TextView) headerView.findViewById(R.id.profile_name);
                appDescriptor.setText(R.string.app_descriptor);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) appDescriptor.getLayoutParams();
                params.addRule(RelativeLayout.END_OF, R.id.nav_header_icon);
                appDescriptor.setLayoutParams(params);

                ImageView logo = (ImageView) headerView.findViewById(R.id.nav_header_icon);
                logo.setVisibility(View.VISIBLE);
            }
        };

        mainHandler.post(uiRunnable);
    }

    public static void init() {

        UIHelper.replaceFragment(new Jump());

        UIHelper.initAddButton();
        UIHelper.switchMode(Prefs.getString(Preference.PREFS_MODE, Subterminal.MODE_SKYDIVING));

        if (!Subterminal.getUser().isLoggedIn()) {
            userLoggedOut();
        }

        if (Subterminal.getUser().isPremium()) {
            userPremium();
        }
    }

    //Quick toast method
    public static void toast(String message) {
        Toast.makeText(MainActivity.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    static ProgressDialog progress;

    public static void loadSpinner() {
        progress = new ProgressDialog(MainActivity.getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
    }

    public static void removeLoadSpinner() {
        if (progress != null) {
            progress.hide();
        }

        progress = null;
    }

    public static void userPremium() {
        MenuItem item = (MenuItem) MainActivity.getActivity().getNavigationView().getMenu().findItem(R.id.nav_premium);
        item.setVisible(false);
    }

    /**
     * Make sure we run our UI changes on the main thread
     *
     * @param visibility
     */
    public static void setProgressBarVisibility(final int visibility) {

        Handler mainHandler = new Handler(MainActivity.getActivity().getMainLooper());

        Runnable uiRunnable = new Runnable() {
            @Override
            public void run() {
                ProgressBar progress = (ProgressBar) MainActivity.getActivity().getToolbar().findViewById(R.id.toolbar_progress_bar);
                progress.setVisibility(visibility);
            }
        };
        mainHandler.post(uiRunnable);

    }
}
