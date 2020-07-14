package de.christine.stundenverwaltung.ui.overview;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import de.christine.stundenverwaltung.MainActivity;
import de.christine.stundenverwaltung.R;
import de.christine.stundenverwaltung.convert.ConvertString;
import de.christine.stundenverwaltung.database.DbHours;
import de.christine.stundenverwaltung.model.DayEntry;

public class OverviewDetailListActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText etChangeComment;
    Button btnChangeDate1, btnChangeStartTime, btnChangeEndTime, btnChangeBreakTime, btnChangeEntry, btnDeleteEntry, btnBack;
    TextView tVChangeTotalTime;
    DbHours database = null;

    long id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_detail_list);

        //verhindert das die Tastatur sofort geöffnet wird
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        btnChangeDate1 = findViewById(R.id.btnChangeDate1);
        btnChangeStartTime = findViewById(R.id.btnChangeStartTime);
        btnChangeEndTime = findViewById(R.id.btnChangeEndTime);
        btnChangeBreakTime = findViewById(R.id.btnChangeBreakTime);
        tVChangeTotalTime = findViewById(R.id.tVChangeTotalTime);
        etChangeComment = findViewById(R.id.etChangeComment);
        btnChangeEntry = findViewById(R.id.btnChangeEntry);
        btnDeleteEntry = findViewById(R.id.btnDeleteEntry);
        btnBack = findViewById(R.id.btnBack);

        database = DbHours.getInstance(OverviewDetailListActivity.this);


// ID wird aus dem Fragment übergeben
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            id = extras.getLong("keyIdEntry");
        }

        final DayEntry entry = database.readDayEntry(id);

        btnChangeDate1.setText(entry.getDateToString());
        btnChangeStartTime.setText(entry.getStartTime());
        btnChangeEndTime.setText(entry.getEndTime());
        btnChangeBreakTime.setText(entry.getBreakTime());
        tVChangeTotalTime.setText(entry.getTotalTime());
        etChangeComment.setText(entry.getComment());


        /**
         * Listener Button btnChangeDate1
         */
        btnChangeDate1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        /**
         * Listener Button btnChangeStartTime
         */
        btnChangeStartTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialogStart();
            }
        });

        /**
         * Listener Button btnChangeEndTime
         */
        btnChangeEndTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialogStop();
            }
        });

        /**
         * Listener Button btnChangeBreakTime
         */
        btnChangeBreakTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialogBreak();
            }
        });

        /**
         * Listener Button btnChangeEntry
         */
        btnChangeEntry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateEntry();
            }
        });

        /**
         * Listener Button btnDeleteEntry
         */
        btnDeleteEntry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                database.deleteDayEntry(entry);
                Toast.makeText(getApplicationContext(), "Eintrag gelöscht.", Toast.LENGTH_LONG).show();
                back();
            }
        });

        /**
         * Listener Button btnBack
         */
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                back();
            }
        });
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

        btnChangeDate1.setText(ConvertString.dateToString(dayOfMonth, month, year));
        toUpdateTime();
    }

    /**
     * Methode erstellt den Dialog Date Picker
     */
    public void showDatePickerDialog() {
        final Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour,
                                          int minute) {

                        btnChangeStartTime.setText(ConvertString.timeToString(hour, minute));
                        toUpdateTime();

                    }
                }, mHour, mMinute, DateFormat.is24HourFormat(this));
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour,
                                          int minute) {

                        btnChangeEndTime.setText(ConvertString.timeToString(hour, minute));
                        toUpdateTime();

                    }
                }, mHour, mMinute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    private void showTimePickerDialogBreak() {

        // Get Current Time
        int mHour = 1;
        int mMinute = 0;

// Get Current Time
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {

                        btnChangeBreakTime.setText(ConvertString.timeToString(hour, minute));
                        toUpdateTime();
                    }
                }, mHour, mMinute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }


    private void toUpdateTime() {

        String lokalStartTime = btnChangeStartTime.getText().toString();
        String lokalEndTime = btnChangeEndTime.getText().toString();
        String lokalBreakTime = btnChangeBreakTime.getText().toString();

        String time = ConvertString.calculateTotalTimeToString(lokalStartTime, lokalEndTime, lokalBreakTime);

        if (time.contains("-")) {
            tVChangeTotalTime.setText("Eingabefehler: Stunden sind im minus Bereich.");
        } else {
            tVChangeTotalTime.setText(time);
        }
    }

    public String[] splitTime(String time) {
        return time.split(":");
    }

    private void updateEntry() {

        String[] splitDate = btnChangeDate1.getText().toString().split("\\.");

        DayEntry update = new DayEntry(id, Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]), btnChangeStartTime.getText().toString(), btnChangeEndTime.getText().toString(), btnChangeBreakTime.getText().toString(), tVChangeTotalTime.getText().toString(), etChangeComment.getText().toString());

        if (etChangeComment.getText().toString().contains("-")) {
            Toast.makeText(getApplicationContext(), "Eingabefehler: Stunden sind im minus Bereich.", Toast.LENGTH_LONG).show();
        } else {
            database.updateDayEntry(update);
            Toast.makeText(getApplicationContext(), "Eintrag geändert.", Toast.LENGTH_LONG).show();
            back();
        }
    }

    private void back() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}