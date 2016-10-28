package mavonie.subterminal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.login.widget.ProfilePictureView;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

import de.cketti.library.changelog.ChangeLog;
import mavonie.subterminal.Forms.GearForm;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.User;
import mavonie.subterminal.Utils.API;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;

import static mavonie.subterminal.Utils.UIHelper.openFragmentForEntity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Home.OnFragmentInteractionListener,
        Login.OnFragmentInteractionListener,
        GearForm.OnFragmentInteractionListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    private RefWatcher refWatcher;

    private ProfilePictureView profilePictureView;

    public Menu getOptionsMenu() {
        return optionsMenu;
    }

    Menu optionsMenu;

    protected static User user;

    protected API api;

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

        Fresco.initialize(this);

        activity = this;
//        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UIHelper.replaceFragment(new Jump());
        UIHelper.initAddButton();

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


        this.api = new API();
        this.api.init();
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
    public boolean onNavigationItemSelected(final MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.LEFT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Handle navigation view item clicks here.
                UIHelper.goToFragment(item.getItemId());
            }
        }, 300);

        return true;
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
        UIHelper.deleteDialog();
    }

    public void editItem(MenuItem item) {
        UIHelper.editEntity();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Model mItem) {
        openFragmentForEntity(mItem);
    }

    ImagePicker imagePicker = new ImagePicker(this);

    public void onPickImage(View view) {
        imagePicker = new ImagePicker(this);
        imagePicker.setImagePickerCallback(
                new ImagePickerCallback() {
                    @Override
                    public void onImagesChosen(List<ChosenImage> images) {
                        // Display images
                        for (ChosenImage image : images) {
                            Image.createFromPath(image.getOriginalPath(), Subterminal.getActiveModel());
                            openFragmentForEntity(Subterminal.getActiveModel());
                        }
                    }

                    @Override
                    public void onError(String message) {
                        // Do error handling
                    }
                }
        );

        imagePicker.pickImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                }
                imagePicker.submit(data);
            }
        }
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }
}
