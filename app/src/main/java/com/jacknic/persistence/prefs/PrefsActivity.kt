package com.jacknic.persistence.prefs

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * SharedPreference 基本使用
 */
class PrefsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mPrefsListener: SharedPreferences.OnSharedPreferenceChangeListener
    private val tagSettingFragment = "tag_setting_fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportFragmentManager.findFragmentByTag(tagSettingFragment) == null) {
            val prefsSettingsFragment = PrefsSettingsFragment()
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, prefsSettingsFragment, tagSettingFragment)
                .commit()
        }
        mPrefsListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if ("notifications" == key) {
                    val state = sharedPreferences.getBoolean(key, false)
                    Toast.makeText(this@PrefsActivity, "通知状态开关：$state", Toast.LENGTH_SHORT).show()
                }
            }
        sharedPreferences = getSharedPreferences(KEY_FILE_NAME_MAIN_SETTINGS, Context.MODE_PRIVATE)
        sharedPreferences.registerOnSharedPreferenceChangeListener(mPrefsListener)
    }


    override fun onDestroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(mPrefsListener)
        super.onDestroy()
    }
}