package org.fxformgenerator.core;

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
}
