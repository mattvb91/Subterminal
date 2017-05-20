package mavonie.subterminal.Skydiving;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
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

import java.util.Map;

import mavonie.subterminal.BaseTest;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.MainActivityTest;
import mavonie.subterminal.Models.Skydive.Aircraft;
import mavonie.subterminal.Preference;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
import mavonie.subterminal.Utils.Subterminal;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class SkydiveTest extends BaseTest {

    @Before
    public void setMode() {
        if (!Prefs.getString(Preference.PREFS_MODE, null).equals(Subterminal.MODE_SKYDIVING)) {
            MainActivityTest.testSkydiveMode();
        }
    }

    @Test
    public void skydiveTest() {

        navigateToSkydives();

        ViewInteraction floatingActionButton = onView(
                allOf(ViewMatchers.withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                withId(R.id.skydive_edit_dropzone));
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText("Irish Parachute Club"), closeSoftKeyboard());

//        ViewInteraction textView = onView(
//                allOf(withId(android.R.id.text1), withText("Irish Parachute Club"), isDisplayed()));
//        textView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.skydive_edit_date)));
        appCompatEditText.perform(scrollTo(), click());

        confirmCalendar();

        ViewInteraction appCompatSpinner = onView(
                withId(R.id.skydive_edit_aircraft));
        appCompatSpinner.perform(scrollTo(), click());

        LinkedHashMapAdapter aircraftAdapter = new LinkedHashMapAdapter<>(MainActivity.getActivity(), android.R.layout.simple_spinner_item, new Aircraft().getItemsForSelect("name"));
        Map.Entry AircraftEntry = aircraftAdapter.getItem(aircraftAdapter.findPositionFromKey(Prefs.getInt(Preference.PREFS_DEFAULT_AIRCRAFT, 1)));

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText(AircraftEntry.getValue().toString()), isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                withId(R.id.skydive_edit_type));
        appCompatSpinner2.perform(scrollTo(), click());

        ViewInteraction appCompatCheckedTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("Tracking"), isDisplayed()));
        appCompatCheckedTextView3.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.skydive_edit_altitude));
        appCompatEditText2.perform(scrollTo(), replaceText("12345"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                withId(R.id.skydive_edit_deploy_altitude));
        appCompatEditText3.perform(scrollTo(), replaceText("3214"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                withId(R.id.skydive_edit_delay));
        appCompatEditText4.perform(scrollTo(), replaceText("54"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                withId(R.id.skydive_edit_description));
        appCompatEditText5.perform(scrollTo(), replaceText("This is the description"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.skydive_save), withText("Save")));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.list),
                        withParent(withId(R.id.flContent)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

//        ViewInteraction textView2 = onView(
//                allOf(withId(R.id.skydive_view_dropzone_name), withText("Irish Parachute Club"),
//                        childAtPosition(
//                                childAtPosition(
//                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
//                                        0),
//                                1),
//                        isDisplayed()));
//        textView2.check(matches(withText("Irish Parachute Club")));

//        ViewInteraction textView3 = onView(
//                allOf(withId(R.id.skydive_view_dropzone_name), withText("Irish Parachute Club"),
//                        childAtPosition(
//                                childAtPosition(
//                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
//                                        0),
//                                1),
//                        isDisplayed()));
//        textView3.check(matches(withText("Irish Parachute Club")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.skydive_view_aircraft), withText(AircraftEntry.getValue().toString()),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
                                        2),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText(AircraftEntry.getValue().toString())));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.skydive_view_jump_type), withText("Tracking"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
                                        3),
                                1),
                        isDisplayed()));
        textView5.check(matches(withText("Tracking")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.skydive_view_altitude), withText("12,345ft"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
                                        6),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("12,345ft")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.skydive_view_deplpoy_altitude), withText("3,214ft"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
                                        6),
                                1),
                        isDisplayed()));
        textView7.check(matches(withText("3,214ft")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.skydive_view_delay), withText("54s"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
                                        6),
                                2),
                        isDisplayed()));
        textView8.check(matches(withText("54s")));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.skydive_view_description), withText("This is the description"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
                                        8),
                                0),
                        isDisplayed()));
        textView9.check(matches(withText("This is the description")));

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.skydive_view_description), withText("This is the description"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
                                        8),
                                0),
                        isDisplayed()));
        textView10.check(matches(withText("This is the description")));

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

    private void navigateToSkydives() {

        openNavigationDrawer();

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Skydives"), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }
}
