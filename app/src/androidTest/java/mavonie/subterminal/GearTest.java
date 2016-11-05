package mavonie.subterminal;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;


public class GearTest extends BaseTest{


    @Test
    public void addGearTest() {
        navigateToGear();

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.gear_save), withText("Save")));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.edit_container_manufacturer));
        appCompatEditText.perform(scrollTo(), replaceText(this.randomString(5)), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.edit_container_type));
        appCompatEditText2.perform(scrollTo(), replaceText(this.randomString(5)), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                withId(R.id.edit_container_serial));
        appCompatEditText3.perform(scrollTo(), replaceText(this.randomString(5)), closeSoftKeyboard());


        ViewInteraction appCompatEditText5 = onView(
                withId(R.id.edit_canopy_manufacturer));
        appCompatEditText5.perform(scrollTo(), replaceText(this.randomString(5)), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                withId(R.id.edit_canopy_type));
        appCompatEditText6.perform(scrollTo(), replaceText(this.randomString(5)), closeSoftKeyboard());

        ViewInteraction appCompatEditText7= onView(
                withId(R.id.edit_canopy_type));
        appCompatEditText7.perform(scrollTo(), replaceText(this.randomString(5)), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.gear_save), withText("Save")));
        appCompatButton2.perform(scrollTo(), click());

    }

    public void navigateToGear() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Gear"), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }

    @Test
    public void deleteGearTest() {

        clickRandomItemTest();

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_delete), withContentDescription("Delete"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"), isDisplayed()));
        appCompatButton2.perform(click());
    }

    @Test
    public void clickRandomItemTest() {

        navigateToGear();

        //Magic happening
        int x = getRandomRecyclerPosition(R.id.list);

        onView(withId(R.id.list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(x, click()));
    }
}
