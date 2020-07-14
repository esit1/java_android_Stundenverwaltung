package de.christine.stundenverwaltung.ui.overview;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.christine.stundenverwaltung.R;
import de.christine.stundenverwaltung.convert.ConvertString;
import de.christine.stundenverwaltung.createPDF.CreatePDF;
import de.christine.stundenverwaltung.database.DbHours;
import de.christine.stundenverwaltung.model.DayEntry;
import de.christine.stundenverwaltung.ui.overview.adapter.OverviewListAdapter;

public class OverviewFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {

    Button btnPDF;
    String[] monthSpinnerData = {"Aktueller Monate", "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember", "Alle Monate"};
    int selectMonth = 0;

    private ListView listView;
    private List<DayEntry> dataSource;
    private OverviewListAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_overview, container, false);

        listView = root.findViewById(R.id.listViewHours);
        btnPDF = root.findViewById(R.id.btnPDF);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = root.findViewById(R.id.spinnerMonth);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, monthSpinnerData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        /**
         * Listener Button btnDefaultStartTime
         */
        btnPDF.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {


                CreatePDF.createPdf(getActivity(),dataSource, ConvertString.monthNrToString(selectMonth));
            }
        });

        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        DbHours database = DbHours.getInstance(getActivity());


        switch ((int) id) {
            case 0:
                GregorianCalendar cal = new GregorianCalendar();
                int month = cal.get(Calendar.MONTH ) + 1;
                dataSource = database.readAllFromMonthDays(month);
                selectMonth = month;
                break;
            case 1:
                dataSource = database.readAllFromMonthDays(1);
                selectMonth = 1;
                break;
            case 2:
                dataSource = database.readAllFromMonthDays(2);
                selectMonth = 2;
                break;
            case 3:
                dataSource = database.readAllFromMonthDays(3);
                selectMonth = 3;
                break;
            case 4:
                dataSource = database.readAllFromMonthDays(4);
                selectMonth = 4;
                break;
            case 5:
                dataSource = database.readAllFromMonthDays(5);
                selectMonth = 5;
                break;
            case 6:
                dataSource = database.readAllFromMonthDays(6);
                selectMonth = 6;
                break;
            case 7:
                dataSource = database.readAllFromMonthDays(7);
                selectMonth = 7;
                break;
            case 8:
                dataSource = database.readAllFromMonthDays(8);
                selectMonth = 8;
                break;
            case 9:
                dataSource = database.readAllFromMonthDays(9);
                selectMonth = 9;
                break;
            case 10:
                dataSource = database.readAllFromMonthDays(10);
                selectMonth = 10;
                break;
            case 11:
                dataSource = database.readAllFromMonthDays(11);
                selectMonth = 11;
                break;
            case 12:
                dataSource = database.readAllFromMonthDays(12);
                selectMonth = 12;
                break;
            default:
                dataSource = database.readAllDays();
                selectMonth = 13;
                break;
        }

        adapter = new OverviewListAdapter(getActivity(), dataSource);

        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Object element = adapterView.getAdapter().getItem(position);

                if (element instanceof DayEntry) {

                    DayEntry entryId = (DayEntry) element;

                    Intent intent = new Intent(getActivity(), OverviewDetailListActivity.class);
                    intent.putExtra("keyIdEntry", entryId.getId());
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        DbHours database = DbHours.getInstance(getActivity());

        GregorianCalendar cal = new GregorianCalendar();
        int month = cal.get(Calendar.MONTH);
        selectMonth = month;
        dataSource = database.readAllFromMonthDays(month+1);
    }
}

