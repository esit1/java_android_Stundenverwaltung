package de.christine.stundenverwaltung.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

import de.christine.stundenverwaltung.convert.ConvertString;


public class DayEntry implements Serializable {
    private long id;
    private long dayDate;
    private long monthDate;
    private long yearDate;
    private String date;
    private String startTime;
    private String endTime;
    private String breakTime;
    private String totalTime;
    private String comment;


    public DayEntry(long dayDate, long monthDate, long yearDate, String startTime, String endTime, String breakTime, String totalTime, String comment) {
        this.dayDate = dayDate;
        this.monthDate = monthDate;
        this.yearDate = yearDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
        this.breakTime = breakTime;
        this.comment = comment;
    }

    public DayEntry(long id, long dayDate, long monthDate, long yearDate, String startTime, String endTime, String breakTime, String totalTime, String comment) {
        this.id = id;
        this.dayDate = dayDate;
        this.monthDate = monthDate;
        this.yearDate = yearDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
        this.comment = comment;
        this.breakTime = breakTime;
    }

    public DayEntry() {
        this.id = 0;
        this.dayDate = 0;
        this.monthDate = 0;
        this.yearDate = 0;
        this.startTime = "0";
        this.endTime = "0";
        this.breakTime = "0";
        this.totalTime = "0";
        this.comment = "comment";
    }

    public DayEntry(String date, String startTime, String endTime, String breakTime, String totalTime, String comment) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakTime = breakTime;
        this.totalTime = totalTime;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDayDate() {
        return dayDate;
    }

    public void setDayDate(long dayDate) {
        this.dayDate = dayDate;
    }

    public long getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(long monthDate) {
        this.monthDate = monthDate;
    }

    public long getYearDate() {
        return yearDate;
    }

    public void setYearDate(long yearDate) {
        this.yearDate = yearDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        DateFormat.getDateInstance().format(calendar.getTime());

        int year       = calendar.get(Calendar.YEAR);
        int month      = calendar.get(Calendar.MONTH);//Jan = 0, dec = 11
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        this.date = ConvertString.dateToString(dayOfMonth,month + 1,year) ;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public void setTotalTimeCalculate() {

        String lokalStartTime = startTime;
        String lokalEndTime = endTime;
        String lokalBreakTime = breakTime;

        String time = ConvertString.calculateTotalTimeToString(lokalStartTime, lokalEndTime, lokalBreakTime);

        this.totalTime = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    public String getDateToString() {
        String day = Long.toString(this.dayDate);
        String month = Long.toString(this.monthDate);
        String year = Long.toString(this.yearDate);

        if (this.dayDate < 9) {
            day = "0" + day;
        }
        if (this.monthDate < 9) {
            month = "0" + month;
        }

        return day + "." + month + "." + year;
    }

    public String[] splitTime(String time) {
        return time.split(":");
    }


}

