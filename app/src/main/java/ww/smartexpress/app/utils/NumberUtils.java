package ww.smartexpress.app.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import ww.smartexpress.app.constant.Constants;

public class NumberUtils {
    static DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    static DecimalFormat decimalFormat;

    public static String formatNumber(int number) {
        symbols.setGroupingSeparator(Constants.SYMBOLS);
        decimalFormat = new DecimalFormat("#,###", symbols);
        return decimalFormat.format(number);
    }

    public static String formatDoubleNumber(Double number) {
        symbols.setGroupingSeparator(Constants.SYMBOLS);
        decimalFormat = new DecimalFormat("#,###", symbols);
        return decimalFormat.format(number);
    }

    public static String formatSecondsToTime(Long seconds) {
        int hours = (int) (seconds / 3600);
        int minutes = (int) Math.ceil((1.0*seconds % 3600) / 60);

        if (hours <= 0) {
            return minutes + " " + Constants.MINUTE;
        } else {
            return hours + " " + Constants.HOUR + " " + minutes + " " + Constants.MINUTE;
        }
    }

    public static String formatDistance(Double distance) {
        double km = distance / 1000;
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(km) + " " + Constants.DISTANCE_MEASURE;
    }

    public static Float formatAverageRating(Double rating) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return Float.parseFloat(decimalFormat.format(rating));
    }
}
