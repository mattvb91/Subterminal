package mavonie.subterminal;


import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.pixplicity.easyprefs.library.Prefs;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Settings"), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }
}