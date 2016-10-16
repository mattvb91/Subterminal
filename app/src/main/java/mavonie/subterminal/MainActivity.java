package mavonie.subterminal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.ProfilePictureView;

import mavonie.subterminal.Forms.ExitForm;
import mavonie.subterminal.Forms.GearForm;
import mavonie.subterminal.Forms.JumpForm;
import mavonie.subterminal.Views.ExitView;
import mavonie.subterminal.models.Model;
import mavonie.subterminal.models.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Home.OnFragmentInteractionListener,
        Login.OnFragmentInteractionListener,
        Jump.OnJumpListFragmentInteractionListener,
        Gear.OnListFragmentInteractionListener,
        GearForm.OnFragmentInteractionListener,
        Exit.OnListFragmentInteractionListener,
        ExitView.OnFragmentInteractionListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    FloatingActionButton fab;

    private ProfilePictureView profilePictureView;

    public Menu getOptionsMenu() {
        return optionsMenu;
    }

    Menu optionsMenu;

    private static final int FRAGMENT_HOME = R.id.nav_home;
    private static final int FRAGMENT_JUMPS = R.id.nav_jumps;
    private static final int FRAGMENT_GEAR = R.id.nav_gear;
    private static final int FRAGMENT_EXIT = R.id.nav_exits;

    protected static int activeFragment;

    protected static void setActiveFragment(int i) {
        activeFragment = i;
    }

    protected int getActiveFragment() {
        return activeFragment;
    }

    protected static User user;

    /**
     * We want only one user instance for the main activity
     *
     * @return User
     */
    public static User getUser() {
        if (user == null) {
            user = new User();
        }

        return user;
    }

    protected static MainActivity activity;

    public static MainActivity getActivity() {
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activateFragment(Home.class);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (getActiveFragment()) {
                    case FRAGMENT_GEAR:
                        activateFragment(GearForm.class);
                        fab.hide();
                        break;
                    case FRAGMENT_EXIT:
                        activateFragment(ExitForm.class);
                        fab.hide();
                        break;
                    case FRAGMENT_JUMPS:
                        activateFragment(JumpForm.class);
                        fab.hide();
                        break;
                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void activateFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, fragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.optionsMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        goToFragment(id);
        return true;
    }

    public void goToFragment(int id) {
        Class fragmentClass = null;

        switch (id) {
            case R.id.nav_home:
                fragmentClass = Home.class;
                fab.hide();
                break;
            case R.id.nav_jumps:
                fragmentClass = Jump.class;
                fab.show();
                break;
            case R.id.nav_gear:
                fragmentClass = Gear.class;
                fab.show();
                break;
            case R.id.nav_login:
                fragmentClass = Login.class;
                break;
            case R.id.nav_exits:
                fragmentClass = Exit.class;
                fab.show();
                break;
        }

        setActiveFragment(id);
        activateFragment(fragmentClass);
        setActiveModel(null);
        getOptionsMenu().findItem(R.id.action_delete).setVisible(false);
        getOptionsMenu().findItem(R.id.action_edit).setVisible(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (this.getUser().getFacebookToken() != null && this.getUser().getFacebookToken().isExpired() == false) {
            this.getUser().init();
        }

        actionBarDrawerToggle.syncState();
    }

    public void updateDrawerProfile() {

        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        View headerView = nav.getHeaderView(0);

        profilePictureView = (ProfilePictureView) headerView.findViewById(R.id.profile_pic);
        profilePictureView.setProfileId(this.getUser().getFacebookToken().getUserId());

        TextView profileName = (TextView) headerView.findViewById(R.id.profile_name);
        TextView profileEmail = (TextView) headerView.findViewById(R.id.profile_email);

        profileName.setText(this.getUser().getFullName());
        profileEmail.setText(this.getUser().getEmail());

        Menu nav_Menu = nav.getMenu();
        nav_Menu.findItem(R.id.nav_login).setTitle("Logout");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onGearListFragmentInteraction(mavonie.subterminal.models.Gear item) {
        fab.hide();

        Bundle args = new Bundle();
        args.putSerializable("item", item);
        GearForm form = new GearForm();
        form.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, form).addToBackStack(null).commit();
    }

    @Override
    public void onExitListFragmentInteraction(mavonie.subterminal.models.Exit item) {
        fab.hide();

        Bundle args = new Bundle();
        args.putSerializable("item", item);
        ExitView view = new ExitView();
        view.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, view).addToBackStack(null).commit();
    }

    public void deleteDialog(MenuItem item) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Confirm delete")
                .setMessage("Delete this item?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        getActiveModel().delete();
                        goToFragment(FRAGMENT_HOME); //TODO proper navigation
                        Toast.makeText(MainActivity.getActivity(), "Item has been deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void editItem(MenuItem item) {
        Toast.makeText(MainActivity.getActivity(), "CLick edit", Toast.LENGTH_SHORT).show();
    }

    /**
     * Set an active model so we can access it throughout
     * popups etc..
     * TODO clean this up
     */
    public Model getActiveModel() {
        return activeModel;
    }

    public void setActiveModel(Model activeModel) {
        this.activeModel = activeModel;
    }

    private Model activeModel;

    @Override
    public void onJumpListFragmentInteraction(mavonie.subterminal.models.Jump item) {
        fab.hide();

        Bundle args = new Bundle();
        args.putSerializable("item", item);
//        JumpView view = new JumpView();
//        view.setArguments(args);

//        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, view).addToBackStack(null).commit();
    }
}
