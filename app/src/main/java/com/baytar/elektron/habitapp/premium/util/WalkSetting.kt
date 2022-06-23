package com.baytar.elektron.habitapp.premium.util

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.baytar.elektron.habitapp.R

class WalkSetting : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.walk_preference)
    }
}