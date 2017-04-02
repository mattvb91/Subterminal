package mavonie.subterminal.Utils;

import com.pixplicity.easyprefs.library.Prefs;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.Preference;
import mavonie.subterminal.R;

/**
 * User settings class.
 */
public class Settings {

    static final String SETTINGS_REQUIRES_SYNC = "SETTINGS_REQUIRES_SYNC";

    public Integer skydive_home_dz_id,
            skydive_start_jump_no,
            skydive_default_aircraft_id,
            skydive_default_jump_type,
            base_start_jump_no,
            base_default_slider_config,
            base_default_jump_type,
            base_default_pc_size,
            default_height_unit;

    public Settings() {
        if (Prefs.getInt(Preference.PREFS_DEFAULT_DROPZONE, 0) != 0) {
            this.skydive_home_dz_id = Prefs.getInt(Preference.PREFS_DEFAULT_DROPZONE, 0);
        }

        this.skydive_start_jump_no = Prefs.getInt(Preference.PREFS_SKYDIVE_START_COUNT, 0);
        this.skydive_default_aircraft_id = Prefs.getInt(Preference.PREFS_DEFAULT_AIRCRAFT, 1);
        this.skydive_default_jump_type = Prefs.getInt(Preference.PREFS_DEFAULT_SKYDIVE_TYPE, Skydive.SKYDIVE_TYPE_BELLY);

        this.base_default_jump_type = Prefs.getInt(Preference.PREFS_DEFAULT_JUMP_TYPE, Jump.TYPE_SLICK);
        this.base_start_jump_no = Prefs.getInt(Preference.PREFS_JUMP_START_COUNT, 0);
        this.base_default_slider_config = Prefs.getInt(Preference.PREFS_DEFAULT_SLIDER, Jump.SLIDER_DOWN);
        this.base_default_pc_size = Prefs.getInt(Preference.PREFS_DEFAULT_PC, 32);

        this.default_height_unit = Prefs.getInt(Preference.PREFS_DEFAULT_HEIGHT_UNIT, Subterminal.HEIGHT_UNIT_IMPERIAL);
    }

    public Integer getSkydiveHomeDzId() {
        return skydive_home_dz_id;
    }

    public void setSkydiveHomeDzId(Integer skydive_home_dz_id) {
        this.skydive_home_dz_id = skydive_home_dz_id;
        Prefs.putInt(Preference.PREFS_DEFAULT_DROPZONE, skydive_home_dz_id);
        notifyChanged();
    }

    public Integer getSkydiveStartJumpNo() {
        return skydive_start_jump_no;
    }

    public void setSkydiveStartJumpNo(Integer skydive_start_jump_no) {
        this.skydive_start_jump_no = skydive_start_jump_no;
        Prefs.putInt(Preference.PREFS_SKYDIVE_START_COUNT, skydive_start_jump_no);
        notifyChanged();
    }

    public Integer getSkydiveDefaultAircraftId() {
        return skydive_default_aircraft_id;
    }

    public void setSkydiveDefaultAircraftId(Integer skydive_default_aircraft_id) {
        this.skydive_default_aircraft_id = skydive_default_aircraft_id;
        Prefs.putInt(Preference.PREFS_DEFAULT_AIRCRAFT, skydive_default_aircraft_id);
        notifyChanged();
    }

    public Integer getSkydiveDefaultJumpType() {
        return skydive_default_jump_type;
    }

    public void setSkydiveDefaultJumpType(Integer skydive_default_jump_type) {
        this.skydive_default_jump_type = skydive_default_jump_type;
        Prefs.putInt(Preference.PREFS_DEFAULT_SKYDIVE_TYPE, skydive_default_jump_type);
        notifyChanged();
    }

    public Integer getBaseStartJumpNo() {
        return base_start_jump_no;
    }

    public void setBaseStartJumpNo(Integer base_start_jump_no) {
        this.base_start_jump_no = base_start_jump_no;
        Prefs.putInt(Preference.PREFS_JUMP_START_COUNT, base_start_jump_no);
        notifyChanged();
    }

    public Integer getBaseDefaultSliderConfig() {
        return base_default_slider_config;
    }

    public void setBaseDefaultSliderConfig(Integer base_default_slider_config) {
        this.base_default_slider_config = base_default_slider_config;
        Prefs.putInt(Preference.PREFS_DEFAULT_SLIDER, base_default_slider_config);
        notifyChanged();
    }

    public Integer getBaseDefaultJumpType() {
        return base_default_jump_type;
    }

    public void setBaseDefaultJumpType(Integer base_default_jump_type) {
        this.base_default_jump_type = base_default_jump_type;
        Prefs.putInt(Preference.PREFS_DEFAULT_JUMP_TYPE, base_default_jump_type);
        notifyChanged();
    }

    public Integer getBaseDefaultPcSize() {
        return base_default_pc_size;
    }

    public void setBaseDefaultPcSize(Integer base_default_pc_size) {
        this.base_default_pc_size = base_default_pc_size;
        Prefs.putInt(Preference.PREFS_DEFAULT_PC, base_default_pc_size);
        notifyChanged();
    }

    public Integer getDefaultHeightUnit() {
        return default_height_unit;
    }

    public void setDefaultHeightUnit(Integer default_height_unit) {
        this.default_height_unit = default_height_unit;
        Prefs.putInt(Preference.PREFS_DEFAULT_HEIGHT_UNIT, default_height_unit);
        notifyChanged();
    }

    public void notifyChanged() {
        if (!Subterminal.isTesting())
            UIHelper.toast(MainActivity.getActivity().getString(R.string.settings_updated));

        Prefs.putBoolean(SETTINGS_REQUIRES_SYNC, true);

        if (Subterminal.getUser().isPremium()) {
            Subterminal.getApi().updateUserSettings();
        }
    }

    public boolean requiresSync() {
        return Prefs.getBoolean(SETTINGS_REQUIRES_SYNC, false);
    }
}
