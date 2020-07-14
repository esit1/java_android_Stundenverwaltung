package de.christine.stundenverwaltung.ui.overview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.christine.stundenverwaltung.R;
import de.christine.stundenverwaltung.model.DayEntry;


public class OverviewListAdapter extends ArrayAdapter<DayEntry> {
    public OverviewListAdapter(final Context context, final List<DayEntry> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {
        DayEntry currentEntry = getItem(position);

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_overview, parent, false);
        }

        ((TextView) view.findViewById(R.id.tVListItemDate)).setText(currentEntry.getDateToString());
        ((TextView) view.findViewById(R.id.tVListItemStartTime)).setText(currentEntry.getStartTime());
        ((TextView) view.findViewById(R.id.tVListItemEndTime)).setText(currentEntry.getEndTime());
        ((TextView) view.findViewById(R.id.tVListItemBreakTime)).setText(currentEntry.getBreakTime());
        ((TextView) view.findViewById(R.id.tVListItemTotalTime)).setText(currentEntry.getTotalTime());
        ((TextView) view.findViewById(R.id.tVListItemCommand)).setText(currentEntry.getComment());

        return view;
    }
}