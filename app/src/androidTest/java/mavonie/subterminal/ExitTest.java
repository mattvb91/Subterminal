package mavonie.subterminal;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class ExitTest extends BaseTest {

    @Test
    public void addExitTest() throws InterruptedException {
        navigateToExits();

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.title), withText("All"), isDisplayed()));
        appCompatTextView.perform(click());

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
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Exits"), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }

    @Test
    public void tabTest() {

        navigateToExits();

        navigateTab("All");
        navigateTab("My Exits");

        navigateTab("All");
        navigateTab("My Exits");
    }

    @Test
    public void clickRandomItemTest() {
        navigateToExits();

        navigateTab("All");

        //Magic happening
        int x = getRandomRecyclerPosition(R.id.list);

        onView(withId(R.id.list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(x, click()));
    }

    public void navigateTab(String tab) {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.title), withText(tab), isDisplayed()));
        appCompatTextView.perform(click());
    }

}
