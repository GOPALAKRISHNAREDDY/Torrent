package in.gopalakrishnareddy.torrent.Implemented;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.app.NavUtils;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import androidx.appcompat.widget.Toolbar;
import in.gopalakrishnareddy.torrent.InputFilterMinMax;
import in.gopalakrishnareddy.torrent.R;
import in.gopalakrishnareddy.torrent.core.TorrentEngine;
import in.gopalakrishnareddy.torrent.core.utils.Utils;
import in.gopalakrishnareddy.torrent.settings.SettingsActivity;
import in.gopalakrishnareddy.torrent.settings.SettingsManager;

import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/*import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;(/

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class Settings extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));}*/
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                //if (preference.equals("auto_clear_history")){
                //Toast.makeText(preference.getContext(), "never", Toast.LENGTH_SHORT).show();}
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }
    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs2 = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(R.style.AppTheme2);
        /*if (prefs2.getBoolean("auto_night_mode", true)) {
            java.util.Calendar c = java.util.Calendar.getInstance();
            int timeOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            OneChange oneChange=new OneChange();
            if(timeOfDay > oneChange.night_stoptime && timeOfDay < oneChange.night_starttime){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { this.getWindow().setStatusBarColor(Color.parseColor("#ffffff")); }
            }else if(timeOfDay >= oneChange.night_starttime || timeOfDay < oneChange.night_stoptime){
                setTheme(R.style.NightMode);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { this.getWindow().setStatusBarColor(Color.parseColor("#4d4e56")); }
                getListView().setBackgroundColor(Color.TRANSPARENT);
                getListView().setCacheColorHint(Color.TRANSPARENT);
                getListView().setBackgroundColor(Color.parseColor("#4d4e56"));
            }}
        else {
            if (prefs2.getBoolean("night_mode", true)) {
                setTheme(R.style.NightMode);
                getListView().setBackgroundColor(Color.TRANSPARENT);
                getListView().setCacheColorHint(Color.TRANSPARENT);
                getListView().setBackgroundColor(Color.parseColor("#4d4e56"));
                ActionBar actionBar = getSupportActionBar();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { this.getWindow().setStatusBarColor(Color.parseColor("#4d4e56")); }
                //actionBar.setBackgroundColor(Color.parseColor("#4d4e56"));
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { this.getWindow().setStatusBarColor(Color.parseColor("#ffffff")); }
            }}*/
        //language();
        //getLayoutInflater().inflate(R.layout.toolbar, (ViewGroup)findViewById(android.R.id.content));
        //Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        //toolbar.setBackgroundColor(Color.parseColor("#4d4e56"));
        setupActionBar();
        ActionBar actionBar = getSupportActionBar();

        //actionBar.setBackgroundColor(Color.parseColor("#4d4e56"));
        //getSupportActionBar().setIcon(R.drawable.settings_24x24);
        getSupportActionBar().setTitle(R.string.settings);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new GeneralPreferenceFragment()).commit();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //NavUtils.navigateUpFromSameTask(this);
                finish();
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.P)
    public void onBuildHeaders(List<Header> target) {
        //loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    /*@SuppressLint("ResourceType")
    private void language() {
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, true);
        //PreferenceManager.setDefaultValues(this, R.xml.pref_interest, true);
        //PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String value = prefs.getString("language", "en");
        if (value.equals("en")) {
            String languageToLoad = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            //this.setContentView(R.xml.pref_headers);
            //lan="en";
        }
        if (value.equals("hi")) {
            String languageToLoad = "hi"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            //this.setContentView(R.xml.pref_headers);
            //lan="hi";
        }
        if (value.equals("te")) {
            String languageToLoad = "te"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            //this.setContentView(R.xml.pref_headers);
            //lan="te";
        }
    }*/


    @TargetApi(Build.VERSION_CODES.P)
    public static class GeneralPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, Preference.OnPreferenceChangeListener {
        public SwitchPreference batterySaver, easy_access,savedata,notiftips;
        public ListPreference auto_clear_history;
        public Preference port_settings, advanced, about;
        public EditTextPreference name, max_up_speed, max_down_speed;
        Context context;
        MediaPlayer click_sound;
        public PreferenceScreen main;
        Toast easy_access_toast;
        AlertDialog.Builder dialog, port_dialog;
        private View battery_layout, portSettings_layout;
        private AlertDialog alert_dialog, port_alert_dialog;
        SwitchCompat savebattery, random_port;
        Switch custombattery;
        SeekBar battery_seekBar;
        TextView seekBar_perc;
        Button battery_cancel, battery_apply, port_apply, port_cancel;
        SharedPreferences prefs;
        String keyBatteryControl;
        String keyCustomBatteryControl;
        String keyCustomBatteryControlValue;
        String keyRandomPort;
        int custom_battery_level = 15;
        SwitchPreference auto_shutdown, wifi_only, auto_start, sequential_download;
        EditText port_number;
        //SharedPreferences.OnSharedPreferenceChangeListener listener;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            keyBatteryControl = getString(R.string.pref_key_battery_control);
            keyCustomBatteryControl = getString(R.string.pref_key_custom_battery_control);
            keyCustomBatteryControlValue = getString(R.string.pref_key_custom_battery_control_value);
            keyRandomPort = getString(R.string.pref_key_use_random_port);
            batterySaver = (SwitchPreference) findPreference("save_battery");
            port_settings = (Preference) getPreferenceManager().findPreference("port_settings");
            advanced = (Preference) getPreferenceManager().findPreference("advanced");
            about = (Preference) getPreferenceManager().findPreference("about");
            dialog = new AlertDialog.Builder(getActivity());
            port_dialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater2 = LayoutInflater.from(getContext());
            LayoutInflater inflater3 = LayoutInflater.from(getContext());
            battery_layout = inflater2.inflate(R.layout.battery_custom_dialog, null);
            portSettings_layout = inflater2.inflate(R.layout.port_custom_dialog, null);
            savebattery = (SwitchCompat) battery_layout.findViewById(R.id.saveBattery);
            custombattery = (Switch) battery_layout.findViewById(R.id.custom_Battery);
            battery_seekBar = (SeekBar) battery_layout.findViewById(R.id.battery_seekBar);
            seekBar_perc = (TextView) battery_layout.findViewById(R.id.seekBar_perc);
            battery_cancel = (Button) battery_layout.findViewById(R.id.battery_cancel);
            battery_apply = (Button) battery_layout.findViewById(R.id.battery_apply);
            random_port = (SwitchCompat) portSettings_layout.findViewById(R.id.random_port);
            port_number = (EditText) portSettings_layout.findViewById(R.id.port_number);
            port_cancel = (Button) portSettings_layout.findViewById(R.id.port_cancel);
            port_apply = (Button) portSettings_layout.findViewById(R.id.port_apply);
            auto_shutdown = (SwitchPreference) findPreference(getString(R.string.pref_key_shutdown_downloads_complete));
            max_down_speed = (EditTextPreference) findPreference(getString(R.string.pref_key_max_download_speed));
            max_up_speed = (EditTextPreference) findPreference(getString(R.string.pref_key_max_upload_speed));
            wifi_only = (SwitchPreference) findPreference(getString(R.string.pref_key_wifi_only));
            auto_start = (SwitchPreference) findPreference(getString(R.string.pref_key_autostart));
            sequential_download = (SwitchPreference) findPreference("show_sequential_download");

            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_max_download_speed)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_max_upload_speed)));

            max_down_speed.setDialogMessage(R.string.speed_limit_dialog);
            max_up_speed.setDialogMessage(R.string.speed_limit_dialog);

            InputFilter[] speedFilter = new InputFilter[]{ new InputFilterMinMax(0, Integer.MAX_VALUE) };
            String keyMaxDownloadSpeedLimit = getString(R.string.pref_key_max_download_speed);
            String value = Integer.toString(prefs.getInt(keyMaxDownloadSpeedLimit,
                    SettingsManager.Default.maxDownloadSpeedLimit) / 1024);
            max_down_speed.getEditText().setFilters(speedFilter);
            max_down_speed.setSummary(value);
            max_down_speed.setText(value);
            String keyMaxUploadSpeedLimit = getString(R.string.pref_key_max_upload_speed);
            max_up_speed.setDialogMessage(R.string.speed_limit_dialog);
            value = Integer.toString(prefs.getInt(keyMaxUploadSpeedLimit,
                    SettingsManager.Default.maxUploadSpeedLimit) / 1024);
            max_up_speed.getEditText().setFilters(speedFilter);
            max_up_speed.setSummary(value);
            max_up_speed.setText(value);

            InputFilter[] portFilter = new InputFilter[]{ new InputFilterMinMax(1, TorrentEngine.Settings.MAX_PORT_NUMBER)};
            port_number.setFilters(portFilter);

            //port.setText(value);

            batterySaver.setSummary(Html.fromHtml("<small>"+getResources().getString(R.string.battery_saver_summary) +"</small>"));
            auto_shutdown.setSummary(Html.fromHtml("<small>"+getResources().getString(R.string.battery_shutdown_summary) +"</small>"));
            wifi_only.setSummary(Html.fromHtml("<small>"+getResources().getString(R.string.pref_wifi_only_summary) +"</small>"));
            auto_start.setSummary(Html.fromHtml("<small>"+getResources().getString(R.string.pref_autostart_summary) +"</small>"));
            sequential_download.setSummary(Html.fromHtml("<small>"+ "By Enabling You Can Find Sequential Download Option In Torrent" +"</small>"));

            battery_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                    seekBar_perc.setText("" + progress+"%");custom_battery_level=progress;
                    if (progress<=90 && progress>=5){
                    seekBar_perc.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
                    }else if (progress>90){seekBar_perc.setText("Max 90%");
                    }else{seekBar_perc.setText("Min 5%"); }
                    //textView.setY(100); just added a value set this properly using screen with height aspect ratio , if you do not set it by default it will be there below seek bar

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (seekBar.getProgress()<5){
                        battery_seekBar.setProgress(5);
                        seekBar_perc.setText("5%");
                        custom_battery_level=5;//seekBar_perc.setX(350);
                    }
                    if (seekBar.getProgress()>90){
                        battery_seekBar.setProgress(90);
                        seekBar_perc.setText("90%");
                        custom_battery_level=90;//seekBar_perc.setX(350);
                    }
                    seekBar_perc.setX(350);
                }
            });


            batterySaver.setOnPreferenceChangeListener(this);
            savebattery.setOnCheckedChangeListener(this);
            custombattery.setOnCheckedChangeListener(this);
            random_port.setOnCheckedChangeListener(this);
            battery_cancel.setOnClickListener(this);
            battery_apply.setOnClickListener(this);
            port_cancel.setOnClickListener(this);
            port_apply.setOnClickListener(this);
            max_up_speed.setOnPreferenceChangeListener(this);
            max_down_speed.setOnPreferenceChangeListener(this);
            auto_start.setOnPreferenceChangeListener(this);

            //click_sound= MediaPlayer.create(getActivity(),R.raw.tik_click);

            //main.setBackgroundColor(Color.TRANSPARENT);
            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //General
            /*bindPreferenceSummaryToValue(findPreference("user_name"));
            bindPreferenceSummaryToValue(findPreference("startwith"));
            //bindPreferenceSummaryToValue(findPreference("date_format"));
            bindPreferenceSummaryToValue(findPreference("calendar_style"));
            bindPreferenceSummaryToValue(findPreference("auto_clear_history"));
            //Interest
            bindPreferenceSummaryToValue(findPreference("currency"));
            bindPreferenceSummaryToValue(findPreference("date_calc_formula"));*/
            checkBatteryConfig();
            checkPortConfig();
            //SharedPreferences.Editor prefsEditor = prefs2.edit();
            //prefsEditor.putBoolean("continue_current_activity",false);
            //prefsEditor.apply();
            /*chat.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Supporting supporting=new Supporting((Activity) preference.getContext());
                    supporting.isPackageExisted();
                    if (prefs2.getBoolean("app_sounds", true)) {click_sound.start();}
                    return true;
                }
            });*/
            /*batterySaver.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    show_battery_dialog();
                    return true;
                }
            });*/
            port_settings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    show_port_dialog();port_number.setSelection(port_number.getText().length());
                    return true;
                }
            });

            advanced.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    return true;
                }
            });

            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getActivity(), "Under Construction", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.battery_cancel:
                    checkBatteryConfig();
                    alert_dialog.cancel();
                    break;
                case R.id.battery_apply:
                    alert_dialog.cancel();
                    apply_battery_changes();
                    break;
                case R.id.port_cancel:
                    checkPortConfig();
                    port_alert_dialog.cancel();
                    break;
                case R.id.port_apply:
                    port_alert_dialog.cancel();
                    apply_port_changes();
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            switch (buttonView.getId()) {
                case R.id.saveBattery:
                    if (isChecked){
                        custombattery.setEnabled(true);
                    }else {
                        custombattery.setEnabled(false);
                        custombattery.setChecked(false);
                        seekBar_perc.setEnabled(false);
                        battery_seekBar.setEnabled(false);
                    }
                    break;
                case R.id.custom_Battery:
                    if (isChecked){
                        seekBar_perc.setEnabled(true);
                        battery_seekBar.setEnabled(true);
                    }else {
                        seekBar_perc.setEnabled(false);
                        battery_seekBar.setEnabled(false);
                    }
                    break;
                case R.id.random_port:
                    if (isChecked){
                        port_number.setEnabled(false);
                    }else {
                        port_number.setEnabled(true);
                    }
                    break;
            }
        }

        private void checkBatteryConfig(){
            if (prefs.getBoolean(keyBatteryControl, true)) {
                savebattery.setChecked(true);
                custombattery.setEnabled(true);
                batterySaver.setChecked(true);
                prefs.edit().putBoolean("save_battery", true).apply();
            }else {
                savebattery.setChecked(false);
                custombattery.setEnabled(false);
                batterySaver.setChecked(false);
                prefs.edit().putBoolean("save_battery", false).apply();
            }

            if (prefs.getBoolean(keyCustomBatteryControl, true)) {
                custombattery.setChecked(true);
                seekBar_perc.setEnabled(true);
                battery_seekBar.setEnabled(true);
            }else {
                custombattery.setChecked(false);
                seekBar_perc.setEnabled(false);
                battery_seekBar.setEnabled(false);
            }
            Integer progress = prefs.getInt(keyCustomBatteryControlValue,15);
            battery_seekBar.setProgress(progress);
            seekBar_perc.setText(progress+"%");
        }

        private void checkPortConfig(){
            if (prefs.getBoolean(keyRandomPort, true)) {
                port_number.setEnabled(false);
            }else {
                port_number.setEnabled(true);
            }
            random_port.setChecked(prefs.getBoolean(keyRandomPort, SettingsManager.Default.useRandomPort));
            String keyPort = getString(R.string.pref_key_port);
            String port_value = Integer.toString(prefs.getInt(keyPort, SettingsManager.Default.port));
            port_settings.setSummary(Html.fromHtml("<small>"+ port_value +"</small>"));//port_settings.setSummary(port_value);
            port_number.setText(port_value);
        }

        private void apply_battery_changes(){
            //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            if (savebattery.isChecked()){
                prefs.edit().putBoolean(keyBatteryControl, true).apply();
                if (custombattery.isChecked()){
                    prefs.edit().putBoolean(keyCustomBatteryControl, true).apply();
                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    prefsEditor.putInt(keyCustomBatteryControlValue, custom_battery_level).apply();
                }else {
                    prefs.edit().putBoolean(keyCustomBatteryControl, false).apply();
                }
                batterySaver.setChecked(true);
                prefs.edit().putBoolean("save_battery", true).apply();
            }else {
                prefs.edit().putBoolean(keyBatteryControl, false).apply();
                prefs.edit().putBoolean(keyCustomBatteryControl, false).apply();
                batterySaver.setChecked(false);
                prefs.edit().putBoolean("save_battery", false).apply();
            }
            Toast.makeText(getActivity(), "Applied Successfully", Toast.LENGTH_SHORT).show();
        }

        private void apply_port_changes(){
            //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            if (random_port.isChecked()){
                prefs.edit().putBoolean(keyRandomPort, true).apply();
            }else {
                prefs.edit().putBoolean(keyRandomPort, false).apply();
                if (!port_number.getText().toString().equals("")) {
                    prefs.edit().putInt(getString(R.string.pref_key_port), Integer.parseInt(port_number.getText().toString())).apply();
                    port_settings.setSummary(Html.fromHtml("<small>" + port_number.getText() + "</small>"));//port_settings.setSummary(port_number.getText());
                }else {
                    String keyPort = getString(R.string.pref_key_port);
                    String port_value = Integer.toString(prefs.getInt(keyPort, SettingsManager.Default.port));
                    port_number.setText(port_value);
                }
            }
            Toast.makeText(getActivity(), "Applied Successfully", Toast.LENGTH_SHORT).show();
        }

        private void show_battery_dialog(){
            //Dialog dialog = new Dialog(getActivity());
            //dialog.setContentView(battery_layout);
            dialog.setView(battery_layout);
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.95);
            //dialog.getWindow().setLayout(width, height);
            //dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            //dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

            dialog.setCancelable(false);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if(battery_layout.getParent()!=null){
                        ((ViewGroup)battery_layout.getParent()).removeView(battery_layout);
                    }
                }
            });
            //dialog.setCanceledOnTouchOutside(true);
            //dialog.setPositiveButton("Download",null);
            //dialog.show();
            alert_dialog = dialog.create();
            alert_dialog.show();
            //dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        private void show_port_dialog(){
            dialog.setView(portSettings_layout);
            dialog.setCancelable(false);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if(portSettings_layout.getParent()!=null){
                        ((ViewGroup)portSettings_layout.getParent()).removeView(portSettings_layout);
                    }
                }
            });
            port_alert_dialog = dialog.create();
            port_alert_dialog.show();
        }


        private void savedata_alert() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final SharedPreferences.Editor editor = prefs.edit();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //builder.setIcon(getIcon());
            builder.setTitle("Are You Sure Do You Want To Disable Save Data?");
            builder.setMessage("It Is Not Recommended To Disable This Option");
            builder.setPositiveButton("Disable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor.putBoolean("savedata", false);
                    editor.apply();
                }
            });
            builder.setNeutralButton("Keep It Enabled", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor.putBoolean("savedata", true);
                    editor.apply();
                    savedata.setChecked(true);
                    //dialog.cancel();
                }
            });
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        /*private void startwith() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor prefsEditor = prefs.edit();
            String value = prefs.getString("startwith", "continue");
            if (value.equals("continue")){}else {
            prefsEditor.putString("last_visited", value);}
            prefsEditor.putBoolean("continue_current_activity",false);
            prefsEditor.apply();
        }*/

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                //startActivity(new Intent(getActivity(), MainActivity.class));
                //NavUtils.navigateUpFromSameTask(this);
                getActivity().finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            SharedPreferences prefs2 = PreferenceManager.getDefaultSharedPreferences(getActivity());
            //if (prefs2.getBoolean("app_sounds", true)) {click_sound.start();}
            return true;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("auto_clear_history")){
                //Toast.makeText(getActivity(), "never", Toast.LENGTH_SHORT).show();
                //Set_Auto_clear_history();
                //Auto_clear_history_toast();
            }else if (key.equals("startwith")){
                //startwith();
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            SharedPreferences pref = SettingsManager.getPreferences(preference.getContext());
            if (preference.getKey().equals("save_battery")){
                show_battery_dialog();
            }
            else if (preference.getKey().equals(getString(R.string.pref_key_max_upload_speed)) ||
                    preference.getKey().equals(getString(R.string.pref_key_max_download_speed))) {
                int value = 0;
                int summary = 0;
                if (!TextUtils.isEmpty((String) newValue)) {
                    summary = Integer.parseInt((String) newValue);
                    value = summary * 1024;
                }
                pref.edit().putInt(preference.getKey(), value).apply();
                preference.setSummary(Integer.toString(summary));
            }else if (preference.getKey().equals(getString(R.string.pref_key_port))) {
                int value = TorrentEngine.Settings.DEFAULT_PORT;
                if (!TextUtils.isEmpty((String) newValue))
                    value = Integer.parseInt((String) newValue);
                pref.edit().putInt(preference.getKey(), value).apply();
                preference.setSummary(Integer.toString(value));
            }
            else if (preference.getKey().equals(getString(R.string.pref_key_autostart))) {
                Utils.enableBootReceiver(getActivity(), (boolean)newValue);
            }
            return  true;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("auto_clear_history")){

        }
        if (key.equals("savedata")) {
            if (sharedPreferences.getBoolean("savedata", false)) {
                Toast.makeText(this.getApplicationContext(), "Turn Off Saving Data Is Not Recommended", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.P)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Settings.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.P)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Settings.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        NotificationPreferenceFragment myFragment = (NotificationPreferenceFragment) getFragmentManager().findFragmentByTag("Prefs_More");
        if (myFragment != null && myFragment.isVisible()) {
            getFragmentManager().beginTransaction().replace(android.R.id.content, new GeneralPreferenceFragment(),"Prefs_General").commit();
        }
        else {
            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
            //NavUtils.navigateUpFromSameTask(this);
            finish();}
    }
}
