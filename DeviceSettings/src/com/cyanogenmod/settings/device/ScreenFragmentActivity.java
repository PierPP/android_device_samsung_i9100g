/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.settings.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.cyanogenmod.settings.device.R;

public class ScreenFragmentActivity extends PreferenceFragment {

    private static final String PREF_ENABLED = "1";
    private static final String TAG = "GalaxyS2Settings_General";

    private static final String FILE_TOUCHKEY_LIGHT = "/data/.disable_touchlight";
    private static final String FILE_TOUCHKEY_NOTIFICATION = "/sys/class/sec/sec_touchkey/notification";
    private static final String FILE_TOUCHKEY_ENABLE_DISABLE = "/sys/class/sec/sec_touchkey/enable_disable";
    private static final String FILE_TOUCHKEY_DISABLE = "/sys/class/sec/sec_touchkey/force_disable";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screen_preferences);

        PreferenceScreen prefSet = getPreferenceScreen();

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        String key = preference.getKey();

        Log.w(TAG, "key: " + key);

        if (key.compareTo(DeviceSettings.KEY_TOUCHKEY_LIGHT) == 0) {
            Utils.writeValue(FILE_TOUCHKEY_LIGHT, ((CheckBoxPreference)preference).isChecked() ? "1" : "0");
            Utils.writeValue(FILE_TOUCHKEY_DISABLE, ((CheckBoxPreference)preference).isChecked() ? "0" : "1");
            Utils.writeValue(FILE_TOUCHKEY_NOTIFICATION, ("0"));
            Utils.writeValue(FILE_TOUCHKEY_ENABLE_DISABLE, ((CheckBoxPreference)preference).isChecked() ? "1" : "0");
        }
        return true;
    }

    public static boolean isSupported(String FILE) {
        return Utils.fileExists(FILE);
    }

    public static void restore(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        Boolean light = sharedPrefs.getBoolean(DeviceSettings.KEY_TOUCHKEY_LIGHT, true);
        String disabled;
        String enable_disable;

        if (light == true) {
            disabled = "0";
            enable_disable = "1";
        } else {
            disabled = "1";
            enable_disable = "0";
        }

        Utils.writeValue(FILE_TOUCHKEY_DISABLE, disabled);
        Utils.writeValue(FILE_TOUCHKEY_LIGHT, enable_disable);
        Utils.writeValue(FILE_TOUCHKEY_ENABLE_DISABLE, enable_disable);
    }
}
