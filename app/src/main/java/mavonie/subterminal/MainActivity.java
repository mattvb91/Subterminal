package mavonie.subterminal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.orangegangsters.lollipin.lib.PinCompatActivity;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import mavonie.subterminal.Forms.GearForm;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Payment;
import mavonie.subterminal.Skydive.Dropzone;
import mavonie.subterminal.Utils.FilterableFragment;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;
import mavonie.subterminal.Views.Premium.PremiumPay;

import static mavonie.subterminal.Utils.UIHelper.openFragmentForEntity;

public class MainActivity extends PinCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GearForm.OnFragmentInteractionListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    private ImagePicker imagePicker;

    public Toolbar getToolbar() {
        return toolbar;
    }

    Toolbar toolbar;
    private RefWatcher refWatcher;

    public Menu getOptionsMenu() {
        return optionsMenu;
    }

    Menu optionsMenu;

    public NavigationView getNavigationView() {
        return navigationView;
    }

    private NavigationView navigationView;

    protected static MainActivity activity;

    public static MainActivity getActivity() {
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (BuildConfig.DEBUG) {
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectLeakedSqlLiteObjects()
//                    .detectLeakedClosableObjects()
//                    .penaltyLog()
//                    .penaltyDeath()
//                    .build());
//        }

        super.onCreate(savedInstanceState);

        activity = this;

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        this.refWatcher = LeakCanary.install(this.getApplication());

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Subterminal.init(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
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

        MenuItem item = menu.findItem(R.id.action_filter);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FilterableFragment fragment = null;

                switch (Subterminal.getActiveFragment()) {
                    case R.id.skydiving_nav_dropzones:
                        fragment = (Dropzone) MainActivity.getActivity().getSupportFragmentManager().findFragmentByTag(Dropzone.class.getCanonicalName());
                        break;
                    case R.id.skydiving_nav_jumps:
                        fragment = (mavonie.subterminal.Skydive.Skydive) MainActivity.getActivity().getSupportFragmentManager().findFragmentByTag(mavonie.subterminal.Skydive.Skydive.class.getCanonicalName());
                        break;
                    case R.id.nav_jumps:
                        fragment = (mavonie.subterminal.Jump) MainActivity.getActivity().getSupportFragmentManager().findFragmentByTag(mavonie.subterminal.Jump.class.getCanonicalName());
                        break;
                    case R.id.nav_gallery:
                        fragment = (mavonie.subterminal.Gallery) MainActivity.getActivity().getSupportFragmentManager().findFragmentByTag(mavonie.subterminal.Gallery.class.getCanonicalName());
                        break;
                    case R.id.skydiving_nav_tunnels:
                        fragment = (mavonie.subterminal.Skydive.Tunnel) MainActivity.getActivity().getSupportFragmentManager().findFragmentByTag(mavonie.subterminal.Skydive.Tunnel.class.getCanonicalName());
                        break;
                }

                if (fragment != null) {
                    fragment.populateFilter();
                }

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Checks for items where we dont want the drawer to close
        if (item.getItemId() == R.id.nav_login) {
            UIHelper.replaceFragment(new Login());
            drawer.closeDrawer(Gravity.LEFT);
            return true;
        } else if (item.getItemId() == R.id.nav_mode) {
            UIHelper.switchModeDialog();
            return true;
        }

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

        actionBarDrawerToggle.syncState();
    }

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

    public void onPickImage(View view) {
        if (imagePicker == null) {
            imagePicker = new ImagePicker(this);
        }
        imagePicker.setImagePickerCallback(
                new ImagePickerCallback() {
                    @Override
                    public void onImagesChosen(List<ChosenImage> images) {
                        // Display images
                        for (ChosenImage image : images) {
                            Image.createFromPath(image.getOriginalPath(), Subterminal.getActiveModel());
                        }

                        openFragmentForEntity(Subterminal.getActiveModel());
                    }

                    @Override
                    public void onError(String message) {
                        // Do error handling
                    }
                }
        );

        imagePicker.pickImage();
    }

    public static final int MY_SCAN_REQUEST_CODE = 2345;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                imagePicker.submit(data);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
                Subterminal.getmCallbackManager().onActivityResult(requestCode, resultCode, data);
            }
        }

        if (requestCode == MY_SCAN_REQUEST_CODE) {

            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                Payment payment = new Payment((CreditCard) data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT));

                Subterminal.setActiveModel(payment);
                UIHelper.replaceFragment(new PremiumPay());

            } else {
                UIHelper.toast("Scan was canceled");
            }
        }
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
