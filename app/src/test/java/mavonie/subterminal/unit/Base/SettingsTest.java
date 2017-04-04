package mavonie.subterminal.unit.Base;


import com.pixplicity.easyprefs.library.Prefs;

import org.junit.Test;

import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.Preference;
import mavonie.subterminal.Utils.Settings;

import static org.junit.Assert.assertEquals;

/**
 * Test the settings class
 */
public class SettingsTest extends BaseUnit {

    @Test
    public void testSettings() {
        Settings settings = new Settings();

        assertEquals(settings.getBaseDefaultJumpType(), (Integer) Jump.TYPE_SLICK);
        assertEquals(settings.getBaseDefaultPcSize(), (Integer) 32);
        assertEquals(settings.getBaseDefaultSliderConfig(), (Integer) Jump.SLIDER_DOWN);
        assertEquals(settings.getBaseStartJumpNo(), (Integer) 0);

        assertEquals(settings.getSkydiveDefaultAircraftId(), (Integer) 1);
        assertEquals(settings.getSkydiveDefaultJumpType(), (Integer) Skydive.SKYDIVE_TYPE_BELLY);
        assertEquals(settings.getSkydiveHomeDzId(), null);
        assertEquals(settings.getSkydiveStartJumpNo(), (Integer) 0);
    }

    @Test
    public void testUpdatingSettings() {
        Settings settings = new Settings();

        settings.setBaseDefaultJumpType(Jump.TYPE_WINGSUIT);
        assertEquals(Prefs.getInt(Preference.PREFS_DEFAULT_JUMP_TYPE, 0), Jump.TYPE_WINGSUIT);
        assertEquals(settings.getBaseDefaultJumpType(), (Integer) Prefs.getInt(Preference.PREFS_DEFAULT_JUMP_TYPE, 0));

        settings.setBaseDefaultPcSize(41);
        assertEquals(Prefs.getInt(Preference.PREFS_DEFAULT_PC, 0), 41);
        assertEquals(settings.getBaseDefaultPcSize(), (Integer) Prefs.getInt(Preference.PREFS_DEFAULT_PC, 0));

        settings.setBaseDefaultSliderConfig(Jump.SLIDER_UP);
        assertEquals(Prefs.getInt(Preference.PREFS_DEFAULT_SLIDER, 0), Jump.SLIDER_UP);
        assertEquals(settings.getBaseDefaultSliderConfig(), (Integer) Prefs.getInt(Preference.PREFS_DEFAULT_SLIDER, 0));

        settings.setBaseStartJumpNo(123);
        assertEquals(Prefs.getInt(Preference.PREFS_JUMP_START_COUNT, 0), 123);
        assertEquals(settings.getBaseStartJumpNo(), (Integer) Prefs.getInt(Preference.PREFS_JUMP_START_COUNT, 0));

        settings.setSkydiveDefaultAircraftId(32);
        assertEquals(Prefs.getInt(Preference.PREFS_DEFAULT_AIRCRAFT, 0), 32);
        assertEquals(settings.getSkydiveDefaultAircraftId(), (Integer) Prefs.getInt(Preference.PREFS_DEFAULT_AIRCRAFT, 0));

        settings.setSkydiveDefaultJumpType(Skydive.SKYDIVE_TYPE_CANOPY_CROSSCOUNTRY);
        assertEquals(Prefs.getInt(Preference.PREFS_DEFAULT_SKYDIVE_TYPE, 0), Skydive.SKYDIVE_TYPE_CANOPY_CROSSCOUNTRY);
        assertEquals(settings.getSkydiveDefaultJumpType(), (Integer) Prefs.getInt(Preference.PREFS_DEFAULT_SKYDIVE_TYPE, 0));

        settings.setSkydiveHomeDzId(223);
        assertEquals(Prefs.getInt(Preference.PREFS_DEFAULT_DROPZONE, 0), 223);
        assertEquals(settings.getSkydiveHomeDzId(), (Integer) Prefs.getInt(Preference.PREFS_DEFAULT_DROPZONE, 0));

        settings.setSkydiveStartJumpNo(1234);
        assertEquals(Prefs.getInt(Preference.PREFS_SKYDIVE_START_COUNT, 0), 1234);
        assertEquals(settings.getSkydiveStartJumpNo(), (Integer) Prefs.getInt(Preference.PREFS_SKYDIVE_START_COUNT, 0));
    }
}
