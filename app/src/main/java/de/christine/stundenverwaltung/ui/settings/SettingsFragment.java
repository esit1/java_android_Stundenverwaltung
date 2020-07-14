package de.christine.stundenverwaltung.ui.settings;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.christine.stundenverwaltung.R;
import de.christine.stundenverwaltung.convert.ConvertString;
import de.christine.stundenverwaltung.database.DbHours;
import de.christine.stundenverwaltung.model.DayEntry;
import de.christine.stundenverwaltung.model.PreferencesKeys;
import de.christine.stundenverwaltung.ui.overview.adapter.OverviewListAdapter;

public class SettingsFragment extends Fragment {

    EditText eTComanyName, eTUserName;
    Button btnDefaultStartTime, btnDefaultEndTime, btnDefaultBreakTime, btnDefaultStartTimeDelete, btnDefaultEndTimeDelete, btnDefaultBreakTimeDelete;
    String[] monthSpinnerData = {"Aktueller Monat", "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};
    private List<DayEntry> dataSource;
    private ListView listView;
    private OverviewListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        DbHours database = DbHours.getInstance(getActivity());
        dataSource = database.readAllDays();

        final SettingsViewModel settings = new SettingsViewModel("0", "0", "0", "0", "0");

        /**
         * Erstellt preferences
         */
        SharedPreferences preferences = this.getActivity().getSharedPreferences(PreferencesKeys.PrefKeys.PREF_NAME, Context.MODE_PRIVATE);

        settings.setDefaultStartTime(preferences.getString(PreferencesKeys.PrefKeys.PREF_DEFAULTSTARTTIMEVALUE, ""));
        settings.setDefaultEndTime(preferences.getString(PreferencesKeys.PrefKeys.PREF_DEFAULTENDTIMEVALUE, ""));
        settings.setDefaultBreakTime(preferences.getString(PreferencesKeys.PrefKeys.PREF_DEFAULTBREAKTIMEVALUE, ""));
        settings.setCompanyName(preferences.getString(PreferencesKeys.PrefKeys.PREF_COMPANYNAME, ""));
        settings.setUserName(preferences.getString(PreferencesKeys.PrefKeys.PREF_USERNAME, ""));


        eTComanyName = root.findViewById(R.id.eTComanyName);
        eTUserName = root.findViewById(R.id.eTUserName);
        btnDefaultStartTime = root.findViewById(R.id.btnDefaultStartTime);
        btnDefaultEndTime = root.findViewById(R.id.btnDefaultEndTime);
        btnDefaultBreakTime = root.findViewById(R.id.btnDefaultBreakTime);
        btnDefaultStartTimeDelete = root.findViewById(R.id.btnDefaultStartTimeDelete);
        btnDefaultEndTimeDelete = root.findViewById(R.id.btnDefaultEndTimeDelete);
        btnDefaultBreakTimeDelete = root.findViewById(R.id.btnDefaultBreakTimeDelete);



        eTComanyName.setText(settings.getCompanyName());
        eTUserName.setText(settings.getUserName());
        btnDefaultStartTime.setText(settings.getDefaultStartTime());
        btnDefaultEndTime.setText(settings.getDefaultEndTime());
        btnDefaultBreakTime.setText(settings.getDefaultBreakTime());


        /**
         * Listener Button btnDefaultStartTime
         */
        btnDefaultStartTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialogStart();
            }
        });

        /**
         * Listener Button btnDefaultEndTime
         */
        btnDefaultEndTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialogStop();
            }
        });

        /**
         * Listener Button btnDefaultBreakTime
         */
        btnDefaultBreakTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialogBreak();
            }
        });

        /**
         * Listener Button btnDefaultBreakTime
         */
        btnDefaultStartTimeDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnDefaultStartTime.setText("");
                settings.setDefaultStartTime("");
            }
        });

        /**
         * Listener Button btnDefaultEndTimeDelete
         */
        btnDefaultEndTimeDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnDefaultEndTime.setText("");
                settings.setDefaultEndTime("");
            }
        });

        /**
         * Listener Button btnDefaultBreakTimeDelete
         */
        btnDefaultBreakTimeDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnDefaultBreakTime.setText("");
                settings.setDefaultBreakTime("");
            }
        });

        return root;
    }



    @Override
    public void onStop() {

        super.onStop();

        SharedPreferences preferences = this.getActivity().getSharedPreferences(PreferencesKeys.PrefKeys.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(PreferencesKeys.PrefKeys.PREF_COMPANYNAME, eTComanyName.getText().toString());
        editor.putString(PreferencesKeys.PrefKeys.PREF_USERNAME, eTUserName.getText().toString());
        editor.putString(PreferencesKeys.PrefKeys.PREF_DEFAULTSTARTTIMEVALUE, btnDefaultStartTime.getText().toString());
        editor.putString(PreferencesKeys.PrefKeys.PREF_DEFAULTENDTIMEVALUE, btnDefaultEndTime.getText().toString());
        editor.putString(PreferencesKeys.PrefKeys.PREF_DEFAULTBREAKTIMEVALUE, btnDefaultBreakTime.getText().toString());

        editor.commit();
    }


    /**
     * Methode, zeigt TimePickerDialog für Start an
     */
    private void showTimePickerDialogStart() {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = 0;

// Get Current Time
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour,
                                          int minute) {

                        btnDefaultStartTime.setText(ConvertString.timeToString(hour, minute));

                    }
                }, mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }

    /**
     * Methode, zeigt TimePickerDialog für stop an
     */
    private void showTimePickerDialogStop() {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = 0;

// Get Current Time
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour,
                                          int minute) {
                        btnDefaultEndTime.setText(ConvertString.timeToString(hour, minute));
                    }
                }, mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }

    /**
     * Methode, zeigt TimePickerDialog für Break
     */
    private void showTimePickerDialogBreak() {

        // Get Current Time
        int mHour = 1;
        int mMinute = 0;

// Get Current Time
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {

                        btnDefaultBreakTime.setText(ConvertString.timeToString(hour, minute));

                    }
                }, mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }




}

