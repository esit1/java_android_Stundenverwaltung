package de.christine.stundenverwaltung.database;

import android.provider.BaseColumns;

public final class TableStructure {
    public TableStructure() {
    }

    public static abstract class TabDayEntry implements BaseColumns {
        public static final String TABLE_NAME = "OverviewDay";
        public static final String COL_ID = "ID";
        public static final String COL_DATEDAYVALUE = "DateDayValue";
        public static final String COL_DATEMONTHVALUE = "DateMonthValue";
        public static final String COL_DATEYEARVALUE = "DateYearValue";
        public static final String COL_STARTTIMEVALUE = "startTimeValue";
        public static final String COL_ENDTIMEVALUE = "endTimeValue";
        public static final String COL_BREAKTIMEVALUE = "breakTimeValue";
        public static final String COL_TOTALTIMEVALUE = "totalTimeValue";
        public static final String COL_COMMENTVALUE = "commentValue";
    }
}

