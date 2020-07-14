package de.christine.stundenverwaltung;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import de.christine.stundenverwaltung.database.DbHours;
import de.christine.stundenverwaltung.model.DayEntry;
import de.christine.stundenverwaltung.model.PreferencesKeys;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_entry, R.id.navigation_overview, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //verhindert das die Tastatur sofort geöffnet wird
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


       // testMethodeDatenbankStunden();
      // newEntry();

        firstAppStart();
    }

    /**
     * Beim ersten App start überprüft die App ob bereits Einstellungen vorgenommen worden sind.
     *
     * @return
     */
    public boolean firstAppStart() {
        SharedPreferences preferences = getSharedPreferences(PreferencesKeys.PrefKeys.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (preferences.getBoolean(PreferencesKeys.PrefKeys.PREF_NAME, true)) {

            editor.putBoolean(PreferencesKeys.PrefKeys.PREF_NAME, false);
            editor.commit();


            return true;
        } else {
            return false;
        }

    }

    public void newEntry(){
        DbHours database = DbHours.getInstance(MainActivity.this);

        for(int i=1; i<31; i++){

            int zufalsszahl = (int) (Math.random() * ( 3 - 0 ) +15);

            DayEntry day = new DayEntry(i, 8, 2020, "07:00",   zufalsszahl + ":" + "00", "00:45", "0" + (zufalsszahl - 8) +":15", "");
            database.insertNewEntry(day);
        }
    }

    public void testMethodeDatenbankStunden() {
        DbHours database = DbHours.getInstance(MainActivity.this);

        Log.i("test", "  _________________1______________________" );
        DayEntry day1 = new DayEntry(02, 7, 2020, "1212", "1515", "1234","122", "");

       // long testvariabel1 = database.insertNewEntry(day1);

        // Log.i("test", "Eingebendern Datensatz: ID: " + testvariabel1);

        Log.i("test", "  ___________________2____________________" );

        DayEntry testvariabel2 = database.readDayEntry(1);
        Log.i("test", "  ");
        Log.i("test", "Ausgelesender Datensatz: ID: " + testvariabel2.getId());
        Log.i("test", "Datum: " + testvariabel2.getDayDate() + "." + testvariabel2.getMonthDate() + "." + testvariabel2.getYearDate());
        Log.i("test", "Startzeit: " + testvariabel2.getStartTime());
        Log.i("test", "Endzeit: " + testvariabel2.getEndTime());
        Log.i("test", "Pausezeit: " + testvariabel2.getBreakTime());
        Log.i("test", "Gesamtzeit: " + testvariabel2.getTotalTime());
        Log.i("test", "Bemerkung: " + testvariabel2.getComment());


        Log.i("test", "  _____________________3_________________" );
        List<DayEntry> testvariabel3;

        testvariabel3 = database.readAllDays();

        Log.i("test", "  Menge der Einträge : " + testvariabel3.size());

        for (int i = 0; i < testvariabel3.size(); i++){
            Log.i("test", "Liste Einträge  ");
            Log.i("test", "Ausgelesender Datensatz: ID: " + testvariabel3.get(i).getId());
            Log.i("test", "Datum: " + testvariabel3.get(i).getDayDate() + "." + testvariabel3.get(i).getMonthDate() + "." + testvariabel3.get(i).getYearDate());
            Log.i("test", "Startzeit: " + testvariabel3.get(i).getStartTime());
            Log.i("test", "Endzeit: " + testvariabel3.get(i).getEndTime());
            Log.i("test", "Pausezeit: " + testvariabel3.get(i).getBreakTime());
            Log.i("test", "Gesamtzeit: " + testvariabel3.get(i).getTotalTime());
            Log.i("test", "Bemerkung: " + testvariabel3.get(i).getComment());
        }
        Log.i("test", "  _____________________4__________________" );

        List<DayEntry> testvariabel4;
        testvariabel4 = database.readAllFromMonthDays(7);


        Log.i("test", "  Menge der Einträge : " + testvariabel4.size());

        for (int i = 0; i < testvariabel4.size(); i++){
            Log.i("test", "Liste Einträge Monat " + testvariabel4.size());
            Log.i("test", "Ausgelesender Datensatz: ID: " + testvariabel4.get(i).getId());
            Log.i("test", "Datum: " + testvariabel4.get(i).getDayDate() + "." + testvariabel4.get(i).getMonthDate() + "." + testvariabel4.get(i).getYearDate());
            Log.i("test", "Startzeit: " + testvariabel4.get(i).getStartTime());
            Log.i("test", "Endzeit: " + testvariabel4.get(i).getEndTime());
            Log.i("test", "Pausezeit: " + testvariabel4.get(i).getBreakTime());
            Log.i("test", "Gesamtzeit: " + testvariabel4.get(i).getTotalTime());
            Log.i("test", "Bemerkung: " + testvariabel4.get(i).getComment());
        }

        Log.i("test", "  _____________________5_________________" );

        DayEntry day2 = new DayEntry(13,1, 1, 2021, "12", "12", "12", "15","fhghg");

        DayEntry testvariabel5 = database.updateDayEntry(day2);


        Log.i("test", "  ");
        Log.i("test", "Ausgelesender Datensatz: ID: " + testvariabel5.getId());
        Log.i("test", "Datum: " + testvariabel5.getDayDate() + "." + testvariabel5.getMonthDate() + "." + testvariabel5.getYearDate());
        Log.i("test", "Startzeit: " + testvariabel5.getStartTime());
        Log.i("test", "Endzeit: " + testvariabel5.getEndTime());
        Log.i("test", "Pausenzeit: " + testvariabel5.getBreakTime());
        Log.i("test", "Gesamtzeit: " + testvariabel5.getTotalTime());
        Log.i("test", "Bemerkung: " + testvariabel5.getComment());

        Log.i("test", "  _____________________6_________________" );

        int testvariabel6 = database.readAllDays().size();

        Log.i("test", "Größe vor löschen: " + testvariabel6);
        DayEntry day3 = new DayEntry(11,1, 1, "2021", "12", "12", "12", "fhghg");

        database.deleteDayEntry(day3);

        testvariabel6 = database.readAllDays().size();
        Log.i("test", "Größe nach dem löschen: " + testvariabel6);


    }

}