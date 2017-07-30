package mavonie.subterminal;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;

import com.pixplicity.easyprefs.library.Prefs;

import org.junit.Before;
import org.junit.Test;

import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.Subterminal;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


public class GearTest extends BaseTest {

    @Before
    public void setMode() {
        if (!Prefs.getString(Preference.PREFS_MODE, null).equals(Subterminal.MODE_BASE)) {
            MainActivityTest.testBaseMode();
        }
    }

    @Test
    public void addRigTest() {
        navigateToGear();

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.arcMenu), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.gear_menu_rig), isDisplayed()));
        floatingActionButton2.perform(click());

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

        ViewInteraction appCompatEditText7 = onView(
                withId(R.id.edit_canopy_type));
        appCompatEditText7.perform(scrollTo(), replaceText(this.randomString(5)), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.gear_save), withText("Save")));
        appCompatButton2.perform(scrollTo(), click());

    }

    public void navigateToGear() {
        openNavigationDrawer();

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Gear"), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }

    @Test
    public void deleteGearTest() {

        addRigTest();
        addRigTest();
        navigateToGear();
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

    @Test
    public void addSuitTest() {
        navigateToGear();

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.title), withText("Suits (" + new Suit().count(Synchronizable.getActiveParams()) + ")"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withClassName(is("android.support.design.widget.FloatingActionButton")),
                        withParent(withId(R.id.arcMenu)),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.gear_menu_suit), isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatSpinner = onView(
                withId(R.id.suit_form_type));
        appCompatSpinner.perform(scrollTo(), click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("Tracking"), isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.suit_form_manufacturer));
        appCompatEditText.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.suit_form_manufacturer));
        appCompatEditText2.perform(scrollTo(), replaceText("Manufacturer"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                withId(R.id.suit_form_model));
        appCompatEditText3.perform(scrollTo(), replaceText("model"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                withId(R.id.suit_form_serial));
        appCompatEditText4.perform(scrollTo(), replaceText("serial"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                withId(R.id.suit_form_dateInUse));
        appCompatEditText5.perform(scrollTo(), click());

        confirmCalendar();

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.suit_save), withText("Save")));
        appCompatButton3.perform(scrollTo(), click());

        navigateToGear();

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.title), withText("Suits (" + new Suit().count(Synchronizable.getActiveParams()) + ")"), isDisplayed()));
        appCompatTextView2.perform(click());

    }

    @Test
    public void deleteSuitTest() {
        navigateToGear();
        addSuitTest();

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.title), withText("Suits (" + new Suit().count(Synchronizable.getActiveParams()) + ")"), isDisplayed()));
        appCompatTextView.perform(click());

        //Magic happening
        int x = getRandomRecyclerPosition(R.id.list);

        onView(withId(R.id.list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(x, click()));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_delete), withContentDescription("Delete"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("OK"), isDisplayed()));
        appCompatButton4.perform(click());
    }
}
