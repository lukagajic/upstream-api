package rs.holycode.upstreamapi.util;

import java.time.LocalDate;

public class DateUtil {
    public static String getCurrentDayOfWeekAsString() {
        return LocalDate.now().getDayOfWeek().name().toLowerCase();
    }
}
