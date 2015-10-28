package cn.nubia.util.jsonprocessor;

import java.util.Calendar;

/**
 * Created by 胡立 on 2015/9/21.
 */
public class TimeFormatConversion {

    public static String toTimeDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String toTimeDate(long time, int flag) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year =calendar.get(Calendar.YEAR);
        int month = (calendar.get(Calendar.MONTH) + 1);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return calendar.get(Calendar.YEAR) + "/" + (month >= 10 ? String.valueOf(month) : "0" + month) + "/"
                + (day >= 10 ? String.valueOf(day) : "0" + day);
//        return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" +
//                calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String toTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        return (hour >= 10 ? String.valueOf(hour) : "0" + hour) + ":" + (min >= 10 ? String.valueOf(min) : "0" + min);
        //return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    public static String toDateTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE);
    }

    public static long toTimeInMillis(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTimeInMillis();
    }

    public static int [] toDateTimeInArray(long time) {
        int [] array = new int[5];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        array[0] = calendar.get(Calendar.YEAR);
        array[1] = calendar.get(Calendar.MONTH);
        array[2] = calendar.get(Calendar.DAY_OF_MONTH);
        array[3] = calendar.get(Calendar.HOUR_OF_DAY);
        array[4] = calendar.get(Calendar.MINUTE);
        return array;
    }
}
