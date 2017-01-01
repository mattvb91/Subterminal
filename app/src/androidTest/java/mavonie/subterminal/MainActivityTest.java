package mavonie.subterminal;

import android.support.test.espresso.ViewInteraction;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.pixplicity.easyprefs.library.Prefs;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import mavonie.subterminal.Utils.Subterminal;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class MainActivityTest extends BaseTest {

    @Test
    public void testModeChange() {
        testBaseMode();
        testSkydiveMode();
    }

    public static void testSkydiveMode() {
        openNavigationDrawer();

        ViewInteraction modeMenu2 = onView(
                allOf(withId(R.id.design_menu_item_text), withText(Prefs.getString(Preference.PREFS_MODE, Subterminal.MODE_SKYDIVING)), isDisplayed()));
        modeMenu2.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.item_dialog_list_item), withText(Subterminal.MODE_SKYDIVING),
                        childAtPosition(
                                withId(R.id.dialog_list_custom_list),
                                1),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        closeNavigationDrawer();
    }

    public static void testBaseMode() {
        openNavigationDrawer();

        ViewInteraction modeMenu = onView(
                allOf(withId(R.id.design_menu_item_text), withText(Prefs.getString(Preference.PREFS_MODE, Subterminal.MODE_SKYDIVING)), isDisplayed()));
        modeMenu.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.item_dialog_list_item), withText(Subterminal.MODE_BASE),
                        childAtPosition(
                                withId(R.id.dialog_list_custom_list),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        closeNavigationDrawer();
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
