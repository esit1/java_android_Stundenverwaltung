package de.christine.stundenverwaltung.convert;

public class ConvertString {

    public static String timeToString(int hour, int minute) {
        String stringHour = String.valueOf(hour);
        String stringMinute = String.valueOf(minute);

        if (hour < 10) {
            stringHour = "0" + stringHour;
        }
        if (minute < 10) {
            stringMinute = "0" + stringMinute;
        }

        return stringHour + ":" + stringMinute;
    }

    public static String dateToString(int day, int month, int year) {

        String stringDay = String.valueOf(day);
        String stringMonth = String.valueOf(month);

        if (day < 10) {
            stringDay = "0" + stringDay;
        }
        if (month < 10) {
            stringMonth = "0" + stringMonth;
        }
        return stringDay + "." + stringMonth + "." + year;
    }

    public static String calculateTotalTimeToString(String lokalStartTime, String lokalEndTime, String lokalBreakTime) {

        String time = "";
        int startTimeMinute = 0;
        int endTimeMinute = 0;
        int breakTimeMinute = 0;

        if (lokalStartTime == "" | lokalEndTime == "" | lokalBreakTime == "") {

            if (lokalEndTime != "" && lokalStartTime != "") {
                String[] splitStartTime = splitTime(lokalStartTime);
                String[] splitEndTime = splitTime(lokalEndTime);

                startTimeMinute = (Integer.parseInt(splitStartTime[0]) * 60) + Integer.parseInt(splitStartTime[1]);
                endTimeMinute = (Integer.parseInt(splitEndTime[0]) * 60) + Integer.parseInt(splitEndTime[1]);

            }
        } else {
            String[] splitStartTime = splitTime(lokalStartTime);
            String[] splitEndTime = splitTime(lokalEndTime);
            String[] splitBreakTime = splitTime(lokalBreakTime);

            startTimeMinute = (Integer.parseInt(splitStartTime[0]) * 60) + Integer.parseInt(splitStartTime[1]);
            endTimeMinute = (Integer.parseInt(splitEndTime[0]) * 60) + Integer.parseInt(splitEndTime[1]);
            breakTimeMinute = (Integer.parseInt(splitBreakTime[0]) * 60) + Integer.parseInt(splitBreakTime[1]);
        }

        int totalTimeInMinute = endTimeMinute - startTimeMinute - breakTimeMinute;

        int totalTimeRestMinute = totalTimeInMinute % 60;
        int totalTimeHour = totalTimeInMinute / 60;

        time = ConvertString.timeToString(totalTimeHour, totalTimeRestMinute);

        return time;
    }

    public static String[] splitTime(String time) {
        return time.split(":");

    }

    public static String monthNrToString(int nr) {
        String month = null;
        switch (nr) {
            case 1:
                month = "Januar";
                break;
            case 2:
                month = "Februar";
                break;
            case 3:
                month = "März";
                break;
            case 4:
                month = "April";
                break;
            case 5:
                month = "Mai";
                break;
            case 6:
                month = "Juni";
                break;
            case 7:
                month = "Juli";
                break;
            case 8:
                month = "August";
                break;
            case 9:
                month = "September";
                break;
            case 10:
                month = "Oktober";
                break;
            case 11:
                month = "November";
                break;
            case 12:
                month = "Dezember";
                break;
            default:
                month = "Alle Monate";

                break;
        }

        return month;
    }

    public static int timeToIntInMinute(String time) {

        String[] splitTime = splitTime(time);
        int minute = (Integer.parseInt(splitTime[0]) * 60) + Integer.parseInt(splitTime[1]);

        return minute;
    }


}
