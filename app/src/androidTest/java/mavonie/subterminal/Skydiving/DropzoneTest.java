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

import mavonie.subterminal.BaseTest;
import mavonie.subterminal.MainActivityTest;
import mavonie.subterminal.Preference;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Subterminal;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class DropzoneTest extends BaseTest {

    @Before
    public void setMode() {
        if (!Prefs.getString(Preference.PREFS_MODE, null).equals(Subterminal.MODE_SKYDIVING)) {
            MainActivityTest.testSkydiveMode();
        }
    }

    @Test
    public void openDropzoneTest() {

        navigateToDropzones();

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.list),
                        withParent(withId(R.id.flContent)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction textView = onView(
                allOf(withText("Website"),
                        childAtPosition(
                                allOf(withId(R.id.contact_layout_website),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.TableRow.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Website")));

        ViewInteraction textView2 = onView(
                allOf(withText("Email"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.TableRow.class),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Email")));

        ViewInteraction textView3 = onView(
                allOf(withText("Map"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Map")));

        ViewInteraction textView4 = onView(
                allOf(withText("Map"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Map")));

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

    private void navigateToDropzones() {
        openNavigationDrawer();

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(ViewMatchers.withId(R.id.design_menu_item_text), withText("Dropzones"), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }
}
