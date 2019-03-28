package util.helper;

import java.text.DecimalFormat;

public class MoneyHelper {
    public static DecimalFormat FORMATTER = new DecimalFormat("#.00");
    
    public static String format(long amount) {
        return "$" + FORMATTER.format(amount);
    }
}
