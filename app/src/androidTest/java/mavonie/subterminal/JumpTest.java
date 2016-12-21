package mavonie.subterminal;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;

import com.pixplicity.easyprefs.library.Prefs;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import mavonie.subterminal.Utils.Subterminal;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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

        //Different versions of calender
        try {
            ViewInteraction appCompatButton2 = onView(
                    allOf(withId(android.R.id.button1), withText("Set"), isDisplayed()));
            appCompatButton2.perform(click());
        } catch (Exception e) {
            ViewInteraction appCompatButton2 = onView(
                    allOf(withId(android.R.id.button1), withText("OK"), isDisplayed()));
            appCompatButton2.perform(click());
        }

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

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Jumps"), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }

    @Test
    public void testViewJump() {

        addJump();
        navigateToJumpsList();

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.list),
                        withParent(withId(R.id.flContent)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

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

        onView(withId(R.id.list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(x, click()));
    }

}
