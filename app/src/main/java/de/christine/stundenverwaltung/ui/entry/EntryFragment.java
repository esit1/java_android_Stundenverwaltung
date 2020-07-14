package de.christine.stundenverwaltung.ui.entry;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import de.christine.stundenverwaltung.R;
import de.christine.stundenverwaltung.convert.ConvertString;
import de.christine.stundenverwaltung.database.DbHours;
import de.christine.stundenverwaltung.model.DayEntry;
import de.christine.stundenverwaltung.model.PreferencesKeys;

public class EntryFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText eTCommand;
    Button btnSaveEntry, btnChangeDate, btnStartTime, btnCurrentTimeStart, btnEndTime, btnCurrentTimeStop, btnBreakTime;
    TextView tVShowTotalTime;

    DayEntry entry = new DayEntry("", "", "", "", "", "");

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {


        final View root = inflater.inflate(R.layout.fragment_entry, container, false);

        /**
         * Erstellt preferences
         */
        SharedPreferences preferences = this.getActivity().getSharedPreferences(PreferencesKeys.PrefKeys.PREF_NAME, Context.MODE_PRIVATE);

        entry.setDate((preferences.getString(PreferencesKeys.PrefKeys.PREF_DATEVALUE, "")));
        entry.setStartTime((preferences.getString(PreferencesKeys.PrefKeys.PREF_STARTTIMEVALUE, "")));
        entry.setEndTime((preferences.getString(PreferencesKeys.PrefKeys.PREF_ENDTIMEVALUE, "")));
        entry.setBreakTime((preferences.getString(PreferencesKeys.PrefKeys.PREF_BREAKTIMEVALUE, "")));
        entry.setTotalTimeCalculate();
        entry.setComment((preferences.getString(PreferencesKeys.PrefKeys.PREF_COMMENTVALUE, "")));

        tVShowTotalTime = root.findViewById(R.id.tVShowTotalTime);
        eTCommand = root.findViewById(R.id.eTComment);
        btnSaveEntry = root.findViewById(R.id.btnSaveEntry);
        btnChangeDate = root.findViewById(R.id.btnChangeDate);
        btnStartTime = root.findViewById(R.id.btnStartTime);
        btnCurrentTimeStart = root.findViewById(R.id.btnCurrentTime1);
        btnEndTime = root.findViewById(R.id.btnEndTime);
        btnCurrentTimeStop = root.findViewById(R.id.btnCurrentTime2);
        btnBreakTime = root.findViewById(R.id.btnBreakTime);

        /**
         * Setzt den Text, aus Objekt entry
         */
        tVShowTotalTime.setText(entry.getTotalTime());
        eTCommand.setText(entry.getComment());
        btnChangeDate.setText(entry.getDate());
        btnStartTime.setText(entry.getStartTime());
        btnEndTime.setText(entry.getEndTime());
        btnBreakTime.setText(entry.getBreakTime());


        /**
         * Listener Button btnChangeDate
         */
        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        /**
         * Listener Button btnStartTime
         */
        btnStartTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialogStart();
            }
        });

        /**
         * Listener Button btnCurrentTime
         */
        btnCurrentTimeStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurrentTime(btnStartTime);
            }
        });

        /**
         * Listener Button btnEndTime
         */
        btnEndTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialogStop();
            }
        });

        /**
         * Listener Button btnCurrentTimeStop
         */
        btnCurrentTimeStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurrentTime(btnEndTime);
            }
        });

        /**
         * Listener Button btnBreakTime
         */
        btnBreakTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialogBreak();
            }
        });

        /**
         * Listener Button btnSaveEntry
         */
        btnSaveEntry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (btnStartTime.getText().toString() != "" && btnEndTime.getText().toString() != "" && (!(tVShowTotalTime.getText().toString().contains("-")) || (tVShowTotalTime.getText().toString().contains("Eingabefehler")))) {
                    saveDataToDB();
                    clearData(entry);
                    Toast.makeText(getActivity(), "Eintrag gespeichert.", Toast.LENGTH_LONG).show();
                } else {
                    if (btnStartTime.getText().toString() == "") {
                        Toast.makeText(getActivity(), "Eingabefehler: Startzeit nicht angeben.", Toast.LENGTH_LONG).show();
                    }
                    if (btnEndTime.getText().toString() == "") {
                        Toast.makeText(getActivity(), "Eingabefehler: Endzeit nicht angeben.", Toast.LENGTH_LONG).show();
                    }
                    if (tVShowTotalTime.getText().toString() == "" || (tVShowTotalTime.getText().toString().contains("Eingabefehler"))) {
                        Toast.makeText(getActivity(), "Eingabefehler: Stunden sind im minus Bereich.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return root;
    }

    /**
     * Methode, speichert bei "Stop" des Fragments die Daten in preferences
     */
    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences preferences = this.getActivity().getSharedPreferences(PreferencesKeys.PrefKeys.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(PreferencesKeys.PrefKeys.PREF_DATEVALUE, btnChangeDate.getText().toString());
        editor.putString(PreferencesKeys.PrefKeys.PREF_STARTTIMEVALUE, btnStartTime.getText().toString());
        editor.putString(PreferencesKeys.PrefKeys.PREF_ENDTIMEVALUE, btnEndTime.getText().toString());
        editor.putString(PreferencesKeys.PrefKeys.PREF_BREAKTIMEVALUE, btnBreakTime.getText().toString());
        editor.putString(PreferencesKeys.PrefKeys.PREF_TOTALTIMEVALUE, tVShowTotalTime.getText().toString());
        editor.putString(PreferencesKeys.PrefKeys.PREF_COMMENTVALUE, eTCommand.getText().toString());

        editor.commit();

    }

    /**
     * Die Methode, löscht die eingebenden Werte, und füllt diese mit Werten aus den Einstellungen, falls angeben.
     *
     * @param entry Objekt
     */
    public void clearData(DayEntry entry) {
        SharedPreferences preferences = this.getActivity().getSharedPreferences(PreferencesKeys.PrefKeys.PREF_NAME, Context.MODE_PRIVATE);

        entry.setCurrentDate();
        entry.setStartTime((preferences.getString(PreferencesKeys.PrefKeys.PREF_DEFAULTSTARTTIMEVALUE, "")));
        entry.setEndTime((preferences.getString(PreferencesKeys.PrefKeys.PREF_DEFAULTENDTIMEVALUE, "")));
        entry.setBreakTime((preferences.getString(PreferencesKeys.PrefKeys.PREF_DEFAULTBREAKTIMEVALUE, "")));
        entry.setComment("");

        btnChangeDate.setText(entry.getDate());
        btnStartTime.setText(entry.getStartTime());
        btnEndTime.setText(entry.getEndTime());
        btnBreakTime.setText(entry.getBreakTime());
        tVShowTotalTime.setText(entry.getTotalTime());
        eTCommand.setText(entry.getComment());
    }

    /**
     * Die Methode, updade sobald einen neue Zeit eingeben worden ist
     */
    public void toUpdateTime() {

        entry.setStartTime(btnStartTime.getText().toString());
        entry.setEndTime(btnEndTime.getText().toString());
        entry.setBreakTime(btnBreakTime.getText().toString());
        entry.setTotalTimeCalculate();

        if (entry.getTotalTime().contains("-")) {
            tVShowTotalTime.setText("Eingabefehler: Stunden sind im minus Bereich.");
        } else {
            tVShowTotalTime.setText(entry.getTotalTime());
        }
    }

    /**
     * Methode erstellt den Dialog Date Picker
     */
    public void showDatePickerDialog() {
        final Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.show();
    }

    /**
     * Methode, Darstellung Datum
     *
     * @param view       v
     * @param year       year
     * @param month      month
     * @param dayOfMonth day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        btnChangeDate.setText(ConvertString.dateToString(dayOfMonth, month + 1, year));
        toUpdateTime();
    }


    /**
     * Methode, setz die aktuelle Zeit
     */
    public void setCurrentTime(Button toChangeButton) {
        final Calendar calender = Calendar.getInstance();
        int hour = calender.get(Calendar.HOUR_OF_DAY);
        int minute = calender.get(Calendar.MINUTE);

        toChangeButton.setText(ConvertString.timeToString(hour, minute));
        toUpdateTime();
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
                    public void onTimeSet(TimePicker view, int hour, int minute) {

                        btnStartTime.setText(ConvertString.timeToString(hour, minute));
                        toUpdateTime();
                    }
                }, mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }

    /**
     * Methode, zeigt TimePickerDialog für top an
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
                    public void onTimeSet(TimePicker view, int hour, int minute) {

                        btnEndTime.setText(ConvertString.timeToString(hour, minute));
                        toUpdateTime();
                    }
                }, mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }

    private void showTimePickerDialogBreak() {

        // Get Current Time
        int mHour = 1;
        int mMinute = 0;

// Get Current Time
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {

                        btnBreakTime.setText(ConvertString.timeToString(hour, minute));
                        toUpdateTime();
                    }
                }, mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }

    private void saveDataToDB() {
        DbHours database = DbHours.getInstance(getActivity());

        String[] splitDate = btnChangeDate.getText().toString().split("\\.");

        DayEntry newEntry = new DayEntry(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]), btnStartTime.getText().toString(), btnEndTime.getText().toString(), btnBreakTime.getText().toString(), tVShowTotalTime.getText().toString(), eTCommand.getText().toString());
        database.insertNewEntry(newEntry);

    }
}

