package moe.kurumi.moegallery.activity;

import android.content.pm.PackageManager;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import org.androidannotations.annotations.AfterPreferences;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.PreferenceByKey;
import org.androidannotations.annotations.PreferenceScreen;

import moe.kurumi.moegallery.R;

/**
 * Created by kurumi on 15-5-30.
 */


@EActivity
@PreferenceScreen(R.xml.settings)
public class SettingsActivity extends PreferenceActivity {

    @PreferenceByKey(R.string.version_key)
    Preference version;

    @AfterPreferences
    void initPrefs() {
        try {
            PackageManager packageManager = getPackageManager();
            String versionString = packageManager.getPackageInfo(getPackageName(), 0).versionName;
            version.setSummary(versionString);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
