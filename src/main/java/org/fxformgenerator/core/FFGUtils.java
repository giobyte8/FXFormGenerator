package org.fxformgenerator.core;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by giovanni on 4/7/16.
 */
public class FFGUtils {

    public static String camelCaseToHumanReadable(String camelCased) {
        StringBuilder humanReadable = new StringBuilder();

        // First char
        humanReadable.append(Character.toUpperCase(camelCased.charAt(0)));

        // Other elements
        for (int i = 1; i < camelCased.length(); i++) {
            if (camelCased.charAt(i) >= 'A' && camelCased.charAt(i) <= 'Z') {
                humanReadable.append(" ");
            }

            humanReadable.append(Character.toLowerCase(camelCased.charAt(i)));
        }

        return humanReadable.toString();
    }

    public static boolean isFloat(String strValue) {
        try {
            Float.parseFloat(strValue);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String strValue) {
        try {
            Double.parseDouble(strValue);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String strValue) {
        try {
            Integer.parseInt(strValue);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public static Date toDate(LocalDate localDate) {
        int day   = localDate.getDayOfMonth();
        int month = localDate.getMonthValue();
        int year  = localDate.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.MONTH, --month);
        calendar.set(Calendar.YEAR, year);

        return calendar.getTime();
    }

    public static LocalDate toLocalDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return LocalDate.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE)
        );
    }
}
