package mavonie.subterminal.Skydiving;


import android.support.test.espresso.ViewInteraction;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.pixplicity.easyprefs.library.Prefs;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;

import mavonie.subterminal.BaseTest;
import mavonie.subterminal.MainActivityTest;
import mavonie.subterminal.Preference;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Subterminal;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class RigTest extends BaseTest {

    @Before
    public void setMode() {
        if (!Prefs.getString(Preference.PREFS_MODE, null).equals(Subterminal.MODE_SKYDIVING)) {
            MainActivityTest.testSkydiveMode();
        }
    }

    /**
     * Test adding a skydiving rig and validate inputs are correct when
     * viewing the entity.
     */
    @Test
    public void rigTest() {

        navigateToRigs();

        String containerManufacturer = this.randomString(5);
        String containerModel = this.randomString(5);
        String containerSerial = this.randomString(5);
        String containerDate = "2001-01-01";

        String mainManufacturer = this.randomString(5);
        String mainModel = this.randomString(5);
        String mainSerial = this.randomString(5);
        String mainDate = "2002-01-01";

        String reserveManufacturer = this.randomString(5);
        String reserveModel = this.randomString(5);
        String reserveSerial = this.randomString(5);
        String reserveDate = "2003-01-01";

        String aadManufacturer = this.randomString(5);
        String aadModel = this.randomString(5);
        String aadSerial = this.randomString(5);
        String aadDate = "2001-04-01";

        ViewInteraction floatingActionButton = onView(
                allOf(withClassName(is("android.support.design.widget.FloatingActionButton")),
                        withParent(withId(R.id.arcMenu)),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.gear_menu_rig), isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.edit_container_manufacturer));
        appCompatEditText.perform(scrollTo(), replaceText(containerManufacturer), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.edit_container_type));
        appCompatEditText2.perform(scrollTo(), replaceText(containerModel), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                withId(R.id.edit_container_serial));
        appCompatEditText3.perform(scrollTo(), replaceText(containerSerial), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                withId(R.id.edit_container_dateInUse));
        appCompatEditText4.perform(scrollTo(), replaceText(containerDate));

        ViewInteraction appCompatEditText5 = onView(
                withId(R.id.edit_canopy_manufacturer));
        appCompatEditText5.perform(scrollTo(), replaceText(mainManufacturer), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                withId(R.id.edit_canopy_model));
        appCompatEditText6.perform(scrollTo(), replaceText(mainModel), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                withId(R.id.edit_canopy_serial));
        appCompatEditText7.perform(scrollTo(), replaceText(mainSerial), closeSoftKeyboard());

        ViewInteraction appCompatEditText8 = onView(
                withId(R.id.edit_canopy_dateInUse));
        appCompatEditText8.perform(scrollTo(), replaceText(mainDate));

        ViewInteraction appCompatEditText9 = onView(
                withId(R.id.edit_reserve_manufacturer));
        appCompatEditText9.perform(scrollTo(), replaceText(reserveManufacturer), closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
                withId(R.id.edit_reserve_model));
        appCompatEditText10.perform(scrollTo(), replaceText(reserveModel), closeSoftKeyboard());

        ViewInteraction appCompatEditText11 = onView(
                withId(R.id.edit_reserve_serial));
        appCompatEditText11.perform(scrollTo(), replaceText(reserveSerial), closeSoftKeyboard());

        ViewInteraction appCompatEditText12 = onView(
                withId(R.id.edit_reserve_dateInUse));
        appCompatEditText12.perform(scrollTo(), replaceText(reserveDate));

        ViewInteraction appCompatEditText13 = onView(
                withId(R.id.edit_aad_manufacturer));
        appCompatEditText13.perform(scrollTo(), replaceText(aadManufacturer), closeSoftKeyboard());

        ViewInteraction appCompatEditText14 = onView(
                withId(R.id.edit_aad_model));
        appCompatEditText14.perform(scrollTo(), replaceText(aadModel), closeSoftKeyboard());

        ViewInteraction appCompatEditText15 = onView(
                withId(R.id.edit_aad_serial));
        appCompatEditText15.perform(scrollTo(), replaceText(aadSerial), closeSoftKeyboard());

        ViewInteraction appCompatEditText16 = onView(
                withId(R.id.edit_aad_dateInUse));
        appCompatEditText16.perform(scrollTo(), replaceText(aadDate));

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.gear_save), withText("Save")));
        appCompatButton6.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.list),
                        withParent(withId(R.id.realtabcontent)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        /**
         * Validate input is correct when we view it again
         */
        ViewInteraction editText = onView(
                allOf(withId(R.id.edit_container_manufacturer), withText(containerManufacturer),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        editText.check(matches(withText(containerManufacturer)));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.edit_container_type), withText(containerModel),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                1),
                        isDisplayed()));
        editText2.check(matches(withText(containerModel)));

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.edit_container_serial), withText(containerSerial),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        editText3.check(matches(withText(containerSerial)));

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.edit_container_dateInUse), withText(containerDate),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                1),
                        isDisplayed()));
        editText4.check(matches(withText(containerDate)));

        ViewInteraction editTextMain = onView(
                allOf(withId(R.id.edit_canopy_manufacturer), withText(mainManufacturer),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        editTextMain.check(matches(withText(mainManufacturer)));

        ViewInteraction editText2Main = onView(
                allOf(withId(R.id.edit_canopy_model), withText(mainModel),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                1),
                        isDisplayed()));
        editText2Main.check(matches(withText(mainModel)));

        ViewInteraction editText3Main = onView(
                allOf(withId(R.id.edit_canopy_serial), withText(mainSerial),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        editText3Main.check(matches(withText(mainSerial)));

        ViewInteraction editText4Main = onView(
                allOf(withId(R.id.edit_canopy_dateInUse), withText(mainDate),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                1),
                        isDisplayed()));
        editText4Main.check(matches(withText(mainDate)));

        ViewInteraction editTextReserve = onView(
                allOf(withId(R.id.edit_reserve_manufacturer), withText(reserveManufacturer),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        editTextReserve.check(matches(withText(reserveManufacturer)));

        ViewInteraction editText2Reserve = onView(
                allOf(withId(R.id.edit_reserve_model), withText(reserveModel),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                1),
                        isDisplayed()));
        editText2Reserve.check(matches(withText(reserveModel)));

        onView(withId(R.id.gear_save)).perform(scrollTo());

        ViewInteraction editText3Reserve = onView(
                allOf(withId(R.id.edit_reserve_serial), withText(reserveSerial),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        editText3Reserve.check(matches(withText(reserveSerial)));

        ViewInteraction editText4Reserve = onView(
                allOf(withId(R.id.edit_reserve_dateInUse), withText(reserveDate),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                1),
                        isDisplayed()));
        editText4Reserve.check(matches(withText(reserveDate)));

        ViewInteraction editTextAad = onView(
                allOf(withId(R.id.edit_aad_manufacturer), withText(aadManufacturer),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        editTextAad.check(matches(withText(aadManufacturer)));

        ViewInteraction editText2Aad = onView(
                allOf(withId(R.id.edit_aad_model), withText(aadModel),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                1),
                        isDisplayed()));
        editText2Aad.check(matches(withText(aadModel)));

        ViewInteraction editText3Aad = onView(
                allOf(withId(R.id.edit_aad_serial), withText(aadSerial),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        editText3Aad.check(matches(withText(aadSerial)));

        ViewInteraction editText4Aad = onView(
                allOf(withId(R.id.edit_aad_dateInUse), withText(aadDate),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                1),
                        isDisplayed()));
        editText4Aad.check(matches(withText(aadDate)));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_delete), withContentDescription("Delete"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("OK"), isDisplayed()));
        appCompatButton4.perform(click());
    }

    private void navigateToRigs() {

        openNavigationDrawer();

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Gear"), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
