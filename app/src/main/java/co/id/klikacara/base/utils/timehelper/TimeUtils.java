package co.id.klikacara.base.utils.timehelper;


import co.id.klikacara.base.utils.stringhelper.StringHelper;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dell on 8/19/2017.
 */

public class TimeUtils {

    public static String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
    public static String DATE_WITH_MONTH_FORMAT = "dd MMM yyyy";
    public static String DAY = "day";
    public static String HOUR = "hour";
    public static String MINUTE = "minute";
    public static String SECOND = "second";

    public static String getFormattedTime(long seconds) {
        String time;
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
        if (day != 0) {
            time = StringHelper.getStringBuilderToString(String.format("%02d", day), ":",
                    String.format("%02d", hours), ":",
                    String.format("%02d", minute), ":",
                    String.format("%02d", second));
        } else {
            time = StringHelper.getStringBuilderToString(String.format("%02d", hours), ":",
                    String.format("%02d", minute), ":",
                    String.format("%02d", second));
        }
        return time;
    }

    public static String getDateFormated(String format, long time) {
        Locale id = new Locale("in", "ID");
        return new SimpleDateFormat(format, id).format(new Date(time));
    }

    public static String getDateFormated(String time) {
        String reformattedDate = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM/yyyy");
        try {
            reformattedDate = outputFormat.format(inputFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reformattedDate;
    }

    public static String getDateFormatedNow(String format) {
        Locale id = new Locale("in", "ID");
        return new SimpleDateFormat(format, id).format(new Date());
    }

    public static Long getDate(String dateInString, String inputFormat) {
        return getDateFromString(dateInString, inputFormat).getTime();
    }

    private static Date getDateFromString(String dateInString, String inputFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
        Date date = null;
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Boolean isWeekend(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    public static Boolean isToday(Calendar calendar) {
        Calendar cal = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                calendar.get(Calendar.ERA) == cal.get(Calendar.ERA) &&
                calendar.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR);
    }

    public static Boolean isToday(Long timeInMillies) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMillies);
        return isToday(cal);
    }

    public static boolean isSameDate(Calendar calendarDay, Calendar calAcara) {
        return calendarDay.get(Calendar.YEAR) == calAcara.get(Calendar.YEAR) &&
                calendarDay.get(Calendar.ERA) == calAcara.get(Calendar.ERA) &&
                calendarDay.get(Calendar.DAY_OF_YEAR) == calAcara.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isSameDate(Long firstDate, Long secondDate) {
        return getDateFormated("ddMMyyyy", firstDate).equals(getDateFormated("ddMMyyyy", secondDate));
    }

    public static Long getBirthDateLimit() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -10);
        return calendar.getTimeInMillis();
    }

    @NotNull
    public static Long getTodayTimeInMilies() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static Map<String, String> getMapFormattedTimeWithoutDays(long seconds) {
        Map<String, String> time = new HashMap<>();
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
        time.put(HOUR, String.format(Locale.getDefault(), "%02d", hours));
        time.put(MINUTE, String.format(Locale.getDefault(), "%02d", minute));
        time.put(SECOND, String.format(Locale.getDefault(), "%02d", second));
        return time;
    }
}