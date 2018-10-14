package mavonie.subterminal;


import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.recyclerview.widget.RecyclerView;
import android.view.WindowManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import mavonie.subterminal.Models.Skydive.Aircraft;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Models.Skydive.Tunnel;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BaseTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private boolean unlocked = false;

    @Before
    public void setUp() {
        this.unlockScreen();
        this.okChangelog();

        if(new Aircraft().count() == 0) {
            //Some data for tests
            Aircraft aircraft = new Aircraft();
            aircraft.setName(randomString(4));
            aircraft.save();
        }

        if(new Dropzone().count() == 0) {
            Dropzone dropzone = new Dropzone();

            dropzone.setCountry("Ireland");
            dropzone.setName("Irish Parachute Club");
            dropzone.setDescription("Description");
            dropzone.setGlobalId("irish_parachute_club");
            dropzone.setEmail("info@skydive.ie");
            dropzone.setPhone("00353 938 35663");
            dropzone.setWebsite("http://skydive.ie");
            dropzone.setLatitude(53.2506);
            dropzone.setLongtitude(-7.1174);

            dropzone.save();
        }

        if(new Tunnel().count() == 0) {
            Tunnel tunnel = new Tunnel();

            tunnel.setCountry("Ireland");
            tunnel.setName("Wind Tunnel");
            tunnel.setDescription("Description");
            tunnel.setEmail("info@tunnel.com");
            tunnel.setPhone("0023466423");
            tunnel.setWebsite("http://tunnel.com");
            tunnel.setLatitude(53.2506);
            tunnel.setLongtitude(-7.1174);
            tunnel.setTunnelHeight(10.04);
            tunnel.setTunnelDiameter(4);

            tunnel.save();
        }
    }

    private void unlockScreen() {
        if(!this.unlocked) {
            final MainActivity activity = mActivityTestRule.getActivity();
            Runnable wakeUpDevice = new Runnable() {
                public void run() {
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            };
            activity.runOnUiThread(wakeUpDevice);
        }
    }

    @Test
    public void okChangelog() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"), isDisplayed()));
        try {
            appCompatButton.perform(click());
        } catch (NoMatchingViewException e) {
            //View is not in hierarchy
        }
    }

    public static void openNavigationDrawer() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());
    }

    public static void closeNavigationDrawer() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    public int getRandomRecyclerPosition(int recyclerId) {
        Random ran = new Random();
        //Get the actual drawn RecyclerView
        RecyclerView recyclerView = (RecyclerView) mActivityTestRule
                .getActivity().findViewById(recyclerId);

        //If the RecyclerView exists, get the item count from the adapter
        int n = (recyclerView == null)
                ? 1
                : recyclerView.getAdapter().getItemCount();

        //Return a random number from 0 (inclusive) to adapter.itemCount() (exclusive)
        return ran.nextInt(n);
    }

    public void confirmCalendar() {
        //Different versions of calender
        try {
            ViewInteraction appCompatButton2 = onView(
                    allOf(withId(android.R.id.button1), withText("Set"), isDisplayed()));
            appCompatButton2.perform(click());
        } catch (Exception e) {
            ViewInteraction appCompatButton2 = onView(
                    allOf(withId(android.R.id.button1), withText("Done"), isDisplayed()));
            appCompatButton2.perform(click());
        }
    }

    private Random random = new Random();

    public String randomString(int length) {
        char[] chars1 = "ABCDEF012GHIJKL345MNOPQR678STUVWXYZ9".toCharArray();
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c1 = chars1[random.nextInt(chars1.length)];
            sb1.append(c1);
        }

        return sb1.toString();
    }
}
