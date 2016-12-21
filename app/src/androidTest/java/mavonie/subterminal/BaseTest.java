package mavonie.subterminal;


import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BaseTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        this.okChangelog();
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

    private Random random = new Random();

    public String randomString(int length) {
        char[] chars1 = "ABCDEF012GHIJKL345MNOPQR678STUVWXYZ9".toCharArray();
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c1 = chars1[random.nextInt(chars1.length)];
            sb1.append(c1);
        }
        String random_string = sb1.toString();

        return random_string;
    }
}
