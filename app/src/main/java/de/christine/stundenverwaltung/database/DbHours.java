package de.christine.stundenverwaltung.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import de.christine.stundenverwaltung.model.DayEntry;


public class DbHours extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "de.christine.zeitverwaltung";

    public static final String SQL_CREATE_TABOVERVIEDAY = " CREATE TABLE " + TableStructure.TabDayEntry.TABLE_NAME + "("
            + TableStructure.TabDayEntry.COL_ID + " INTEGER PRIMARY KEY, "
            + TableStructure.TabDayEntry.COL_DATEDAYVALUE + " INTEGER DEFAULT NULL,"
            + TableStructure.TabDayEntry.COL_DATEMONTHVALUE + " INTEGER DEFAULT NULL,"
            + TableStructure.TabDayEntry.COL_DATEYEARVALUE + " INTEGER DEFAULT NULL,"
            + TableStructure.TabDayEntry.COL_STARTTIMEVALUE + " TEXT NOT NULL, "
            + TableStructure.TabDayEntry.COL_ENDTIMEVALUE + " TEXT NOT NULL, "
            + TableStructure.TabDayEntry.COL_BREAKTIMEVALUE + " TEXT NOT NULL, "
            + TableStructure.TabDayEntry.COL_TOTALTIMEVALUE + " TEXT NOT NULL, "
            + TableStructure.TabDayEntry.COL_COMMENTVALUE + " TEXT NOT NULL)";

    public static DbHours INSTANCE = null;

    public DbHours(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DbHours(Context context, String name,
                   SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static DbHours getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DbHours(context);
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABOVERVIEDAY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Version 1 => no upgrade necessary
    }

    public long insertNewEntry(final DayEntry dayEntry) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TableStructure.TabDayEntry.COL_DATEDAYVALUE, dayEntry.getDayDate());
        values.put(TableStructure.TabDayEntry.COL_DATEMONTHVALUE, dayEntry.getMonthDate());
        values.put(TableStructure.TabDayEntry.COL_DATEYEARVALUE, dayEntry.getYearDate());
        values.put(TableStructure.TabDayEntry.COL_STARTTIMEVALUE, dayEntry.getStartTime());
        values.put(TableStructure.TabDayEntry.COL_ENDTIMEVALUE, dayEntry.getEndTime());
        values.put(TableStructure.TabDayEntry.COL_BREAKTIMEVALUE, dayEntry.getBreakTime());
        values.put(TableStructure.TabDayEntry.COL_TOTALTIMEVALUE, dayEntry.getTotalTime());
        values.put(TableStructure.TabDayEntry.COL_COMMENTVALUE, dayEntry.getComment());

        long newID = database.insert(TableStructure.TabDayEntry.TABLE_NAME, null, values);

        database.close();

        return newID;
    }

    public DayEntry readDayEntry(final long id) {
        SQLiteDatabase database = this.getReadableDatabase();
        DayEntry dayEntry = new DayEntry();

        Cursor cursor = database.query(TableStructure.TabDayEntry.TABLE_NAME,
                new String[]{TableStructure.TabDayEntry.COL_ID, TableStructure.TabDayEntry.COL_DATEDAYVALUE,
                        TableStructure.TabDayEntry.COL_DATEMONTHVALUE, TableStructure.TabDayEntry.COL_DATEYEARVALUE,
                        TableStructure.TabDayEntry.COL_STARTTIMEVALUE, TableStructure.TabDayEntry.COL_ENDTIMEVALUE,
                        TableStructure.TabDayEntry.COL_BREAKTIMEVALUE, TableStructure.TabDayEntry.COL_TOTALTIMEVALUE,
                        TableStructure.TabDayEntry.COL_COMMENTVALUE},
                TableStructure.TabDayEntry.COL_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    dayEntry.setId(cursor.getLong(cursor.getColumnIndex(TableStructure.TabDayEntry.COL_ID)));
                    dayEntry.setDayDate(cursor.getLong(cursor.getColumnIndex(TableStructure.TabDayEntry.COL_DATEDAYVALUE)));
                    dayEntry.setMonthDate(cursor.getLong(cursor.getColumnIndex(TableStructure.TabDayEntry.COL_DATEMONTHVALUE)));
                    dayEntry.setYearDate(cursor.getLong(cursor.getColumnIndex(TableStructure.TabDayEntry.COL_DATEYEARVALUE)));
                    dayEntry.setStartTime(cursor.getString(cursor.getColumnIndex(TableStructure.TabDayEntry.COL_STARTTIMEVALUE)));
                    dayEntry.setEndTime(cursor.getString(cursor.getColumnIndex(TableStructure.TabDayEntry.COL_ENDTIMEVALUE)));
                    dayEntry.setBreakTime(cursor.getString(cursor.getColumnIndex(TableStructure.TabDayEntry.COL_BREAKTIMEVALUE)));
                    dayEntry.setTotalTime(cursor.getString(cursor.getColumnIndex(TableStructure.TabDayEntry.COL_TOTALTIMEVALUE)));
                    dayEntry.setComment(cursor.getString(cursor.getColumnIndex(TableStructure.TabDayEntry.COL_COMMENTVALUE)));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        database.close();
        return dayEntry;
    }

    public List<DayEntry> readAllDays() {

        List<DayEntry> allDayEntry = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TableStructure.TabDayEntry.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                DayEntry dayEntry = readDayEntry(cursor.getLong(cursor.getColumnIndex(TableStructure.TabDayEntry.COL_ID)));
                if (dayEntry != null) {
                    allDayEntry.add(dayEntry);
                }
            } while (cursor.moveToNext());
        }
        database.close();

        return allDayEntry;
    }

    public List<DayEntry> readAllFromMonthDays(int searchMonth) {

        List<DayEntry> allDayEntry = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(TableStructure.TabDayEntry.TABLE_NAME,
                null, TableStructure.TabDayEntry.COL_DATEMONTHVALUE + "=" + searchMonth,
                null, null, null, TableStructure.TabDayEntry.COL_DATEDAYVALUE +" ASC");

        if (cursor.moveToFirst()) {
            do {
                DayEntry dayEntry = readDayEntry(cursor.getLong(cursor.getColumnIndex(TableStructure.TabDayEntry.COL_ID)));
                if (dayEntry != null) {
                    allDayEntry.add(dayEntry);
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return allDayEntry;
    }

    public DayEntry updateDayEntry(final DayEntry dayEntry) {

        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put(TableStructure.TabDayEntry.COL_DATEDAYVALUE, dayEntry.getDayDate());
        values.put(TableStructure.TabDayEntry.COL_DATEMONTHVALUE, dayEntry.getMonthDate());
        values.put(TableStructure.TabDayEntry.COL_DATEYEARVALUE, dayEntry.getYearDate());
        values.put(TableStructure.TabDayEntry.COL_STARTTIMEVALUE, dayEntry.getStartTime());
        values.put(TableStructure.TabDayEntry.COL_ENDTIMEVALUE, dayEntry.getEndTime());
        values.put(TableStructure.TabDayEntry.COL_BREAKTIMEVALUE, dayEntry.getEndTime());
        values.put(TableStructure.TabDayEntry.COL_TOTALTIMEVALUE, dayEntry.getTotalTime());
        values.put(TableStructure.TabDayEntry.COL_COMMENTVALUE, dayEntry.getComment());

        database.update(TableStructure.TabDayEntry.TABLE_NAME, values, TableStructure.TabDayEntry.COL_ID + " = ? ", new String[]{String.valueOf(dayEntry.getId())});

        database.close();

        return dayEntry;

    }

    public void deleteDayEntry(final DayEntry dayEntry) {
        SQLiteDatabase database = this.getReadableDatabase();
        database.delete(TableStructure.TabDayEntry.TABLE_NAME, TableStructure.TabDayEntry.COL_ID + " = ?", new String[]{String.valueOf(dayEntry.getId())});
        database.close();
    }

    public void deleteAllDayEntrys() {

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + TableStructure.TabDayEntry.TABLE_NAME);
        database.close();
    }

    public boolean getId() {
        return true;
    }
}



