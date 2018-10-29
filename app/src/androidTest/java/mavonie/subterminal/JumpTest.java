package mavonie.subterminal;


import android.support.design.widget.NavigationView;
import androidx.test.espresso.ViewInteraction;

import com.pixplicity.easyprefs.library.Prefs;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import mavonie.subterminal.Utils.Subterminal;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class JumpTest extends BaseTest {

    @Before
    public void setMode() {
        if (!Prefs.getString(Preference.PREFS_MODE, null).equals(Subterminal.MODE_BASE)) {
            MainActivityTest.testBaseMode();
        }
    }

    @Test
    public void addJump() {

        navigateToJumpsList();

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                withId(R.id.jump_edit_exit_name));
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText(this.randomString(5)), closeSoftKeyboard());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.jump_edit_date)));
        appCompatEditText.perform(scrollTo(), click());

        confirmCalendar();

        //Jump type
        int random = new Random().nextInt(mavonie.subterminal.Models.Jump.getTypeArray().length);
        String jumpType = Arrays.asList(mavonie.subterminal.Models.Jump.getTypeArray()).get(random);

        ViewInteraction jumpTypeSpinner = onView(
                allOf(withId(R.id.jump_edit_type), isDisplayed()));
        jumpTypeSpinner.perform(click());

        ViewInteraction appCompatCheckedTextView4 = onView(
                allOf(withId(android.R.id.text1), withText(jumpType), isDisplayed()));
        appCompatCheckedTextView4.perform(click());

        ViewInteraction appCompatSpinner = onView(
                withId(R.id.jump_edit_pc_size));

        appCompatSpinner.perform(scrollTo(), click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("38"), isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                withId(R.id.jump_edit_slider));
        appCompatSpinner2.perform(scrollTo(), click());

        ViewInteraction appCompatCheckedTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("Up"), isDisplayed()));
        appCompatCheckedTextView3.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.jump_edit_delay));
        appCompatEditText2.perform(scrollTo(), replaceText("10"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                withId(R.id.jump_edit_description));
        appCompatEditText3.perform(scrollTo(), replaceText(this.randomString(30)), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.jump_save), withText("Save")));
        appCompatButton3.perform(scrollTo(), click());

    }

    public static void navigateToJumpsList() {
        openNavigationDrawer();

        onView(isAssignableFrom(NavigationView.class)).perform(swipeDown());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Jumps"), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }

    @Test
    public void testViewJump() {

        addJump();
        navigateToJumpsList();

//        ViewInteraction recyclerView = onView(
//                allOf(withId(R.id.list),
//                        withParent(withId(R.id.flContent)),
//                        isDisplayed()));
//        recyclerView.perform(actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.exit_picture_button), withText("Add Picture"), isDisplayed()));
    }

    @Test
    public void clickRandomItemTest() {

        for (int i = 0; i <= 2; i++) {
            addJump();
        }
        navigateToJumpsList();

        //Magic happening
        int x = getRandomRecyclerPosition(R.id.list);

//        onView(withId(R.id.list))
//                .perform(RecyclerViewActions
//                        .actionOnItemAtPosition(x, click()));
    }

}
