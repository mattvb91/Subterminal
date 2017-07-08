package mavonie.subterminal;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.pixplicity.easyprefs.library.Prefs;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Random;

import mavonie.subterminal.Models.Jump;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsTest extends BaseTest {

    @Test
    public void pinTest() {

        goToSettings();

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.settings_pin_switch), isDisplayed()));
        switch_.perform(click());

        ViewInteraction keyboardButtonView = onView(
                allOf(withId(R.id.pin_code_button_1),
                        withParent(withId(R.id.pin_code_first_row)),
                        isDisplayed()));
        keyboardButtonView.perform(click());


        ViewInteraction keyboardButtonView2 = onView(
                allOf(withId(R.id.pin_code_button_2),
                        withParent(withId(R.id.pin_code_first_row)),
                        isDisplayed()));
        keyboardButtonView2.perform(click());


        ViewInteraction keyboardButtonView3 = onView(
                allOf(withId(R.id.pin_code_button_3),
                        withParent(withId(R.id.pin_code_first_row)),
                        isDisplayed()));
        keyboardButtonView3.perform(click());


        ViewInteraction keyboardButtonView4 = onView(
                allOf(withId(R.id.pin_code_button_4),
                        withParent(withId(R.id.pin_code_second_row)),
                        isDisplayed()));
        keyboardButtonView4.perform(click());


        ViewInteraction keyboardButtonView5 = onView(
                allOf(withId(R.id.pin_code_button_1),
                        withParent(withId(R.id.pin_code_first_row)),
                        isDisplayed()));
        keyboardButtonView5.perform(click());

        ViewInteraction keyboardButtonView6 = onView(
                allOf(withId(R.id.pin_code_button_2),
                        withParent(withId(R.id.pin_code_first_row)),
                        isDisplayed()));
        keyboardButtonView6.perform(click());

        ViewInteraction keyboardButtonView7 = onView(
                allOf(withId(R.id.pin_code_button_3),
                        withParent(withId(R.id.pin_code_first_row)),
                        isDisplayed()));
        keyboardButtonView7.perform(click());

        ViewInteraction keyboardButtonView8 = onView(
                allOf(withId(R.id.pin_code_button_4),
                        withParent(withId(R.id.pin_code_second_row)),
                        isDisplayed()));
        keyboardButtonView8.perform(click());

        Assert.assertTrue(Prefs.getBoolean(Preference.PIN_ENABLED, false));

        goToSettings();

        ViewInteraction switch_2 = onView(
                allOf(withId(R.id.settings_pin_switch), isDisplayed()));
        switch_2.perform(click());

        Assert.assertFalse(Prefs.getBoolean(Preference.PIN_ENABLED, true));

    }

    private void goToSettings() {
        openNavigationDrawer();

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Settings"), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }

    @Test
    public void startingJumpNumberTest() {

        MainActivityTest.testBaseMode();

        goToSettings();

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.preference_start_count), isDisplayed()));
        appCompatEditText.perform(replaceText("10"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.preference_start_count), isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());

        JumpTest.navigateToJumpsList();

        goToSettings();

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.preference_start_count), isDisplayed()));
        appCompatEditText3.perform(replaceText("0"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.preference_start_count), isDisplayed()));
        appCompatEditText4.perform(pressImeActionButton());

        JumpTest.navigateToJumpsList();

    }

    @Test
    public void testBaseDefaults() {

        MainActivityTest.testBaseMode();
        goToSettings();

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.preference_default_slider_config_value), isDisplayed()));
        appCompatSpinner.perform(scrollTo(), click());

        int random = new Random().nextInt(Jump.getSliderConfigArray().length);
        String sliderSelect = Arrays.asList(Jump.getSliderConfigArray()).get(random);

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText(sliderSelect), isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        onView(allOf(withId(R.id.preference_default_pc_size_value))).perform(scrollTo());

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.preference_default_pc_size_value), isDisplayed()));
        appCompatSpinner2.perform(scrollTo(), click());

        random = new Random().nextInt(Jump.getPcSizeArray().length);
        String pcSize = Integer.toString(Arrays.asList(Jump.getPcSizeArray()).get(random));

        ViewInteraction appCompatCheckedTextView3 = onView(
                allOf(withId(android.R.id.text1), withText(pcSize), isDisplayed()));
        appCompatCheckedTextView3.perform(click());

        random = new Random().nextInt(Jump.getTypeArray().length);
        String jumpType = Arrays.asList(Jump.getTypeArray()).get(random);

        ViewInteraction appCompatSpinner3 = onView(
                allOf(withId(R.id.preference_default_jump_type_value), isDisplayed()));
        appCompatSpinner3.perform(scrollTo(), click());

        ViewInteraction appCompatCheckedTextView4 = onView(
                allOf(withId(android.R.id.text1), withText(jumpType), isDisplayed()));
        appCompatCheckedTextView4.perform(click());

        JumpTest.navigateToJumpsList();

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        //TODO fix slider issue
        //onView(withId(R.id.jump_edit_slider)).perform(scrollTo()).check(matches(withSpinnerText(containsString(sliderSelect))));
        onView(withId(R.id.jump_edit_pc_size)).perform(scrollTo()).check(matches(withSpinnerText(containsString(pcSize))));
        onView(withId(R.id.jump_edit_type)).perform(scrollTo()).check(matches(withSpinnerText(containsString(jumpType))));

    }
}
