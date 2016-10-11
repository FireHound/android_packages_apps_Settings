/*
 * Copyright (C) 2016 Firehound ROMs
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

package com.android.settings.fh;

import android.app.Activity;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.TrafficStats;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.ListPreference;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.logging.MetricsProto.MetricsEvent;

public class AnimationSettings extends SettingsPreferenceFragment
    implements OnPreferenceChangeListener {

    private static final String TAG = "AnimationSettings";

    private static final String KEY_TOAST_ANIMATION = "toast_animation";

    private ListPreference mToastAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.animation_settings);
	PreferenceScreen prefSet = getPreferenceScreen();
	ContentResolver resolver = getActivity().getContentResolver();

	// Toast Animations
        mToastAnimation = (ListPreference) prefSet.findPreference(KEY_TOAST_ANIMATION);
        mToastAnimation.setSummary(mToastAnimation.getEntry());
        int CurrentToastAnimation = Settings.System.getInt(
                resolver, Settings.System.TOAST_ANIMATION, 1);
        mToastAnimation.setValueIndex(CurrentToastAnimation); //set to index of default value
        mToastAnimation.setSummary(mToastAnimation.getEntries()[CurrentToastAnimation]);
        mToastAnimation.setOnPreferenceChangeListener(this);
    }

    @Override
    protected int getMetricsCategory() {
       return MetricsEvent.APPLICATION;
   }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
	if (preference == mToastAnimation) {
            int index = mToastAnimation.findIndexOfValue((String) newValue);
            Settings.System.putString(resolver,
                    Settings.System.TOAST_ANIMATION, (String) newValue);
            mToastAnimation.setSummary(mToastAnimation.getEntries()[index]);
            Toast.makeText(getActivity(), "Toast test!!!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
