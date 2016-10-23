package mavonie.subterminal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import de.cketti.library.changelog.ChangeLog;
import mavonie.subterminal.Forms.ExitForm;
import mavonie.subterminal.Forms.GearForm;
import mavonie.subterminal.Forms.JumpForm;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.ImagePicker;
import mavonie.subterminal.Views.ExitView;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.User;
import mavonie.subterminal.Views.JumpView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Home.OnFragmentInteractionListener,
        Login.OnFragmentInteractionListener,
        GearForm.OnFragmentInteractionListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    private RefWatcher refWatcher;

    /**
     * @return FloatingActionButton
     */
    public FloatingActionButton getFab() {
        return fab;
    }

    FloatingActionButton fab;

    private ProfilePictureView profilePictureView;

    public Menu getOptionsMenu() {
        return optionsMenu;
    }

    Menu optionsMenu;

    //    private static final int FRAGMENT_HOME = R.id.nav_home;
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

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        this.refWatcher = LeakCanary.install(this.getApplication());

        activity = this;
//        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activateFragment(Jump.class);

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

        ChangeLog cl = new ChangeLog(this);
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
        }
    }

    private void activateFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, fragment, fragmentClass.getCanonicalName())
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
//            case R.id.nav_home:
//                fragmentClass = Home.class;
//                fab.hide();
//                break;
            case R.id.nav_jumps:
                fragmentClass = Jump.class;
                fab.show();
                break;
            case R.id.nav_gear:
                fragmentClass = Gear.class;
                fab.show();
                break;
            case R.id.nav_login:
                return;
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
//
//        if (this.getUser().getFacebookToken() != null && this.getUser().getFacebookToken().isExpired() == false) {
//            this.getUser().init();
//        }

        actionBarDrawerToggle.syncState();
    }

//    public void updateDrawerProfile() {
//
//        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
//        View headerView = nav.getHeaderView(0);
//
//        profilePictureView = (ProfilePictureView) headerView.findViewById(R.id.profile_pic);
//        profilePictureView.setProfileId(this.getUser().getFacebookToken().getUserId());
//
//        TextView profileName = (TextView) headerView.findViewById(R.id.profile_name);
//        TextView profileEmail = (TextView) headerView.findViewById(R.id.profile_email);
//
//        profileName.setText(this.getUser().getFullName());
//        profileEmail.setText(this.getUser().getEmail());
//
//        Menu nav_Menu = nav.getMenu();
//        nav_Menu.findItem(R.id.nav_login).setTitle("Logout");
//    }


    public void deleteDialog(MenuItem item) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Confirm delete")
                .setMessage("Delete this item?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        getActiveModel().delete();
                        goToFragment(FRAGMENT_JUMPS); //TODO proper navigation
                        Toast.makeText(MainActivity.getActivity(), "Item has been deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void editItem(MenuItem item) {

        Bundle args = new Bundle();
        args.putSerializable("item", getActiveModel());

        Fragment fragment = null;

        if (getActiveModel().canEdit()) {
            if (getActiveModel() instanceof mavonie.subterminal.Models.Exit) {
                fragment = new ExitForm();
            } else if (getActiveModel() instanceof mavonie.subterminal.Models.Jump) {
                fragment = new JumpForm();
            }
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
        }
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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Model mItem) {

        fab.hide();
        Bundle args = new Bundle();
        args.putSerializable("item", mItem);

        BaseFragment fragment = null;

        if (mItem instanceof mavonie.subterminal.Models.Exit) {
            fragment = new ExitView();
        } else if (mItem instanceof mavonie.subterminal.Models.Gear) {
            fragment = new GearForm();
        } else if (mItem instanceof mavonie.subterminal.Models.Jump) {
            fragment = new JumpView();
        }

        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
    }

    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter

    public void onPickImage(View view) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    public Bitmap getLastBitmap() {
        return lastBitmap;
    }

    public Bitmap lastBitmap = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PICK_IMAGE_ID:
                this.lastBitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }
}
