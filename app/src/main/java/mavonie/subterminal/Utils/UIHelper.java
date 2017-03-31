package mavonie.subterminal.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.github.ahmadnemati.wind.WindView;
import com.github.ahmadnemati.wind.enums.TrendType;
import com.pixplicity.easyprefs.library.Prefs;
import com.sa90.materialarcmenu.ArcMenu;

import java.util.Arrays;
import java.util.List;

import az.openweatherapi.listener.OWRequestListener;
import az.openweatherapi.model.OWResponse;
import az.openweatherapi.model.gson.common.Coord;
import az.openweatherapi.model.gson.five_day.ExtendedWeather;
import az.openweatherapi.model.gson.five_day.WeatherForecastElement;
import mavonie.subterminal.Dashboard;
import mavonie.subterminal.Exit;
import mavonie.subterminal.Forms.ExitForm;
import mavonie.subterminal.Forms.GearForm;
import mavonie.subterminal.Forms.JumpForm;
import mavonie.subterminal.Forms.SuitForm;
import mavonie.subterminal.Gallery;
import mavonie.subterminal.Gear;
import mavonie.subterminal.Jump;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Skydive.Rig;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.Models.Skydive.TunnelSession;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Preference;
import mavonie.subterminal.R;
import mavonie.subterminal.Skydive.Dropzone;
import mavonie.subterminal.Skydive.Forms.SkydiveForm;
import mavonie.subterminal.Skydive.Forms.TunnelSessionForm;
import mavonie.subterminal.Skydive.Tunnel;
import mavonie.subterminal.Skydive.Views.DropzoneView;
import mavonie.subterminal.Skydive.Views.SkydiveView;
import mavonie.subterminal.Skydive.Views.TunnelView;
import mavonie.subterminal.Views.ExitView;
import mavonie.subterminal.Views.JumpView;
import mavonie.subterminal.Views.Premium.PremiumView;
import uk.me.lewisdeane.ldialogs.CustomListDialog;

import static mavonie.subterminal.Preference.PREFS_DEFAULT_HEIGHT_UNIT;

/**
 * Class to deal with UI/Fragment navigation components
 * Make sure all changes are made on the UI thread
 */
public class UIHelper {

    private static final int FRAGMENT_JUMPS = R.id.nav_jumps;
    private static final int FRAGMENT_GEAR = R.id.nav_gear;
    private static final int FRAGMENT_EXIT = R.id.nav_exits;
    private static final int FRAGMENT_SKYDIVES = R.id.skydiving_nav_jumps;
    private static final int FRAGMENT_GALLERY = R.id.nav_gallery;
    private static final int FRAGMENT_TUNNEL_SESSION = R.id.skydiving_nav_tunnels;

    private static FloatingActionButton addButton;
    private static ArcMenu arcButton;

    /**
     * @return FloatingActionButton
     */
    public static FloatingActionButton getAddButton() {
        return addButton;
    }

    public static ArcMenu getArcMenu() {
        return arcButton;
    }

    /**
     * Check which entity we have and open the relevant fragment
     *
     * @param entity
     */
    public static void openFragmentForEntity(Model entity) {

        getAddButton().hide();
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_filter).setVisible(false);

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
        } else if (entity instanceof mavonie.subterminal.Models.Skydive.Tunnel) {
            fragment = new TunnelView();
        } else if (entity instanceof TunnelSession) {
            fragment = new TunnelSessionForm();
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

        //Disable add buttons here and enable manually below
        getArcMenu().setVisibility(View.GONE);
        getAddButton().hide();
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_filter).setVisible(false);

        switch (id) {
            case R.id.nav_jumps:
                fragmentClass = new Jump();
                getAddButton().show();
                break;

            case R.id.nav_gear:
                fragmentClass = new Gear();
                break;

            case R.id.nav_exits:
                fragmentClass = new Exit();
                getAddButton().show();
                break;

            case R.id.nav_settings:
                fragmentClass = new Preference();
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
                break;

            case R.id.skydiving_nav_tunnels:
                fragmentClass = new Tunnel();
                break;

            case R.id.skydiving_nav_gear:
                fragmentClass = new mavonie.subterminal.Skydive.Gear();
                break;
            case R.id.nav_dashboard:
                fragmentClass = new Dashboard();
                break;
            case R.id.nav_gallery:
                fragmentClass = new Gallery();
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

        addButton = (FloatingActionButton) MainActivity.getActivity().findViewById(R.id.fab);
        addButton.hide();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Subterminal.getActiveFragment()) {
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
                    case FRAGMENT_TUNNEL_SESSION:
                        UIHelper.replaceFragment(new TunnelSessionForm());
                        addButton.hide();
                        break;
                }
            }
        });

        arcButton = (ArcMenu) MainActivity.getActivity().findViewById(R.id.arcMenu);
        arcButton.setVisibility(View.GONE);
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
        MenuItem item = MainActivity.getActivity().getNavigationView().getMenu().findItem(R.id.nav_login);
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
        UIHelper.switchMode(Prefs.getString(Preference.PREFS_MODE, Subterminal.MODE_SKYDIVING));

        UIHelper.initAddButton();
        UIHelper.replaceFragment(new Dashboard());

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
        MenuItem item = MainActivity.getActivity().getNavigationView().getMenu().findItem(R.id.nav_premium);
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

    public static void initWeatherView(final View view, Coord coordinate) {
        final WindView windView = (WindView) view.findViewById(R.id.windview);

        Subterminal.getApi().getOpenWeatherClient().getFiveDayForecast(coordinate, new OWRequestListener<ExtendedWeather>() {
            @Override
            public void onResponse(OWResponse<ExtendedWeather> response) {
                ExtendedWeather extendedWeather = response.body();
                WeatherForecastElement element = extendedWeather.getList().get(0);

                windView.setPressure(element.getMain().getPressure().intValue());
                windView.setPressureUnit("in hPa");

                if (Prefs.getInt(Preference.PREFS_DEFAULT_HEIGHT_UNIT, Subterminal.HEIGHT_UNIT_IMPERIAL) == Subterminal.HEIGHT_UNIT_IMPERIAL) {
                    windView.setWindSpeedUnit(" mph");
                    windView.setWindSpeed((float) new UnitConverter().lengthConvert(element.getWind().getSpeed().doubleValue(), "kilometers", "miles"));
                } else {
                    windView.setWindSpeedUnit(" km/h");
                    windView.setWindSpeed(element.getWind().getSpeed().intValue());
                }
                windView.setTrendType(TrendType.UP);
                windView.animateBaroMeter();
                windView.start();

                TableLayout forecastTable = (TableLayout) view.findViewById(R.id.forecast_table);

                TableRow titleRow = new TableRow(MainActivity.getActivity());
                titleRow.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView timeTitle = new TextView(MainActivity.getActivity());
                timeTitle.setText(" Time (Hours) ");
                timeTitle.setTextColor(Color.BLACK);
                timeTitle.setTypeface(null, Typeface.BOLD);
                timeTitle.setGravity(Gravity.CENTER);
                titleRow.addView(timeTitle);

                TextView windTitle = new TextView(MainActivity.getActivity());

                if (Prefs.getInt(Preference.PREFS_DEFAULT_HEIGHT_UNIT, Subterminal.HEIGHT_UNIT_IMPERIAL) == Subterminal.HEIGHT_UNIT_IMPERIAL) {
                    windTitle.setText(" Wind (mph) ");
                } else {
                    windTitle.setText(" Wind (km/h) ");
                }
                windTitle.setTextColor(Color.BLACK);
                windTitle.setTypeface(null, Typeface.BOLD);
                windTitle.setGravity(Gravity.CENTER);
                titleRow.addView(windTitle);

                TextView pressureTitle = new TextView(MainActivity.getActivity());
                pressureTitle.setText(" Pressure (hPa) ");
                pressureTitle.setTextColor(Color.BLACK);
                pressureTitle.setGravity(Gravity.CENTER);
                pressureTitle.setTypeface(null, Typeface.BOLD);
                titleRow.addView(pressureTitle);

                forecastTable.addView(titleRow);

                for (int i = 1; i < 5; i++) {
                    element = extendedWeather.getList().get(i);

                    TableRow weatherRow = new TableRow(MainActivity.getActivity());
                    weatherRow.setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView timeColumn = new TextView(MainActivity.getActivity());
                    timeColumn.setText("+" + (i * 3));
                    timeColumn.setTextColor(Color.BLACK);
                    timeColumn.setGravity(Gravity.CENTER);
                    weatherRow.addView(timeColumn);

                    TextView windColumn = new TextView(MainActivity.getActivity());

                    windColumn.setText(element.getWind().getSpeed().toString());
                    if (Prefs.getInt(Preference.PREFS_DEFAULT_HEIGHT_UNIT, Subterminal.HEIGHT_UNIT_IMPERIAL) == Subterminal.HEIGHT_UNIT_IMPERIAL) {
                        windColumn.setText(String.format("%.1f", new UnitConverter().lengthConvert(element.getWind().getSpeed().doubleValue(), "kilometers", "miles")));
                    } else {
                        windColumn.setText(element.getWind().getSpeed().toString());
                    }

                    windColumn.setTextColor(Color.BLACK);
                    windColumn.setGravity(Gravity.CENTER);
                    weatherRow.addView(windColumn);

                    TextView pressureColumn = new TextView(MainActivity.getActivity());
                    pressureColumn.setText(element.getMain().getPressure().toString());
                    pressureColumn.setTextColor(Color.BLACK);
                    pressureColumn.setGravity(Gravity.CENTER);
                    weatherRow.addView(pressureColumn);

                    forecastTable.addView(weatherRow);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("TAG", "Five Day Forecast request failed: " + t.getMessage());
                windView.setVisibility(View.GONE);
            }
        });
    }

    public static void prefillHeightUnit(RadioGroup heightUnit) {
        heightUnit.setOnCheckedChangeListener(null);

        if (Prefs.getInt(PREFS_DEFAULT_HEIGHT_UNIT, Subterminal.HEIGHT_UNIT_IMPERIAL) == Subterminal.HEIGHT_UNIT_IMPERIAL) {
            heightUnit.check(R.id.radio_imperial);
        } else {
            heightUnit.check(R.id.radio_metric);
        }
    }
}
