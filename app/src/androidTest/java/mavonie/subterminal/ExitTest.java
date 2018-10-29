package mavonie.subterminal;

import androidx.test.espresso.ViewInteraction;

import com.pixplicity.easyprefs.library.Prefs;

import org.junit.Before;
import org.junit.Test;

import mavonie.subterminal.Utils.Subterminal;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class ExitTest extends BaseTest {

    @Before
    public void setMode() {
        if (!Prefs.getString(Preference.PREFS_MODE, null).equals(Subterminal.MODE_BASE)) {
            MainActivityTest.testBaseMode();
        }
    }

    @Test
    public void addExitTest() throws InterruptedException {
        navigateToExits();

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.exit_edit_name));
        appCompatEditText.perform(scrollTo(), replaceText(this.randomString(5)), closeSoftKeyboard());

        ViewInteraction appCompatEditRockdrop = onView(
                withId(R.id.exit_edit_rockdrop_distance));
        appCompatEditRockdrop.perform(scrollTo(), replaceText("100"), closeSoftKeyboard());

        ViewInteraction appCompactDistanceToLanding = onView(
                withId(R.id.exit_edit_distance_to_landing));
        appCompactDistanceToLanding.perform(scrollTo(), replaceText("120"), closeSoftKeyboard());

        ViewInteraction objectType = onView(
                withId(R.id.exit_object_type));
        objectType.perform(scrollTo(), click());

        ViewInteraction objectTypeClick = onView(
                allOf(withId(android.R.id.text1), withText("Span"), isDisplayed()));
        objectTypeClick.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.exit_edit_description));
        appCompatEditText2.perform(scrollTo(), replaceText(this.randomString(20)), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.exit_save), withText("Save")));
        appCompatButton.perform(scrollTo(), click());
    }

    public void navigateToExits() {
        openNavigationDrawer();

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Exits"), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }

    @Test
    public void clickRandomItemTest() throws InterruptedException {
        addExitTest();
        addExitTest();

        navigateToExits();

        //Magic happening
        int x = getRandomRecyclerPosition(R.id.list);

//        onView(withId(R.id.list))
//                .perform(RecyclerViewActions
//                        .actionOnItemAtPosition(x, click()));
    }
}
