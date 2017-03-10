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

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.logging.MetricsProto.MetricsEvent;

private static final String PREF_ROWS_PORTRAIT = "qs_rows_portrait";
private static final String PREF_ROWS_LANDSCAPE = "qs_rows_landscape";
private static final String PREF_COLUMNS = "qs_columns";
private static final String KEY_SYSUI_QQS_COUNT = "sysui_qqs_count_key";

private ListPreference mRowsPortrait;
private ListPreference mRowsLandscape;
private ListPreference mQsColumns;
private ListPreference mSysuiQqsCount;

public class NotificationSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

	private static final String DISABLE_IMMERSIVE_MESSAGE = "disable_immersive_message";

	private SwitchPreference mDisableIM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.notification_settings);
	mDisableIM = (SwitchPreference) findPreference(DISABLE_IMMERSIVE_MESSAGE);
        mDisableIM.setOnPreferenceChangeListener(this);
        int DisableIM = Settings.System.getInt(getContentResolver(),
                DISABLE_IMMERSIVE_MESSAGE, 0);
	mDisableIM.setChecked(DisableIM != 0);
        int defaultValue;

        mRowsPortrait = (ListPreference) findPreference(PREF_ROWS_PORTRAIT);
        int rowsPortrait = Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.QS_ROWS_PORTRAIT, 3);
        mRowsPortrait.setValue(String.valueOf(rowsPortrait));
        mRowsPortrait.setSummary(mRowsPortrait.getEntry());
        mRowsPortrait.setOnPreferenceChangeListener(this);

        defaultValue = getResources().getInteger(com.android.internal.R.integer.config_qs_num_rows_landscape_default);
        mRowsLandscape = (ListPreference) findPreference(PREF_ROWS_LANDSCAPE);
        int rowsLandscape = Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.QS_ROWS_LANDSCAPE, defaultValue);
        mRowsLandscape.setValue(String.valueOf(rowsLandscape));
        mRowsLandscape.setSummary(mRowsLandscape.getEntry());
        mRowsLandscape.setOnPreferenceChangeListener(this);

        mQsColumns = (ListPreference) findPreference(PREF_COLUMNS);
        int columnsQs = Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.QS_COLUMNS, 3);
        mQsColumns.setValue(String.valueOf(columnsQs));
        mQsColumns.setSummary(mQsColumns.getEntry());
        mQsColumns.setOnPreferenceChangeListener(this);

        mSysuiQqsCount = (ListPreference) findPreference(KEY_SYSUI_QQS_COUNT);
        int SysuiQqsCount = Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.QQS_COUNT, 5);
        mSysuiQqsCount.setValue(Integer.toString(SysuiQqsCount));
        mSysuiQqsCount.setSummary(mSysuiQqsCount.getEntry());
        mSysuiQqsCount.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        int intValue;
        int index;
        if (preference == mDisableIM) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), DISABLE_IMMERSIVE_MESSAGE,
                    value ? 1 : 0);
            return true;
        } else if (preference == mRowsPortrait) {
            intValue = Integer.valueOf((String) objValue);
            index = mRowsPortrait.findIndexOfValue((String) objValue);
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.QS_ROWS_PORTRAIT, intValue);
            preference.setSummary(mRowsPortrait.getEntries()[index]);
            return true;
        } else if (preference == mRowsLandscape) {
            intValue = Integer.valueOf((String) objValue);
            index = mRowsLandscape.findIndexOfValue((String) objValue);
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.QS_ROWS_LANDSCAPE, intValue);
            preference.setSummary(mRowsLandscape.getEntries()[index]);
            return true;
        } else if (preference == mQsColumns) {
            intValue = Integer.valueOf((String) objValue);
            index = mQsColumns.findIndexOfValue((String) objValue);
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.QS_COLUMNS, intValue);
            preference.setSummary(mQsColumns.getEntries()[index]);
            return true;
        } else if (preference == mSysuiQqsCount) {
            String SysuiQqsCount = (String) objValue;
            int SysuiQqsCountValue = Integer.parseInt(SysuiQqsCount);
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.QQS_COUNT, SysuiQqsCountValue);
            int SysuiQqsCountIndex = mSysuiQqsCount.findIndexOfValue(SysuiQqsCount);
            mSysuiQqsCount.setSummary(mSysuiQqsCount.getEntries()[SysuiQqsCountIndex]);
            return true;
        }
        return false;
	}

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.APPLICATION;
    }
}
