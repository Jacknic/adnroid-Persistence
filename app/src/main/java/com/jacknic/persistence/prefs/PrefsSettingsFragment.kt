package com.jacknic.persistence.prefs

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.jacknic.persistence.R

const val KEY_FILE_NAME_MAIN_SETTINGS = "main_settings"

/**
 * 设置项页面
 */

class PrefsSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = KEY_FILE_NAME_MAIN_SETTINGS
        setPreferencesFromResource(R.xml.prefs_main_settings, rootKey)
    }
}
