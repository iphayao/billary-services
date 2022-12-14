package th.co.readypaper.billary.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {
    public static final String[] THAI_MONTHS = {"", "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.", "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค."};
    public static final String[] FULL_MONTHS = {"", "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};

    public static YearMonth yearMonthOf(LocalDate date) {
        if (date == null) {
            return YearMonth.now();
        }
        return YearMonth.of(date.getYear(), date.getMonth());
    }

    public static LocalDateTime convertToUTC(LocalDate startDate) {
        return startDate.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    public static LocalDateTime convertToUTC(LocalDateTime dateTime) {
        return dateTime
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    public static LocalDateTime startDayOf(LocalDate date) {
        return date.atStartOfDay();
    }

    public static LocalDateTime endDayOf(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }

    public static String getDateFormat(LocalDate date) {
        return getDateFormat(date, "yyyyMM");
    }

    public static String getDateFormat(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String generate(String prefix, int year, int month, int number) {
        return String.format("%s%04d%02d%04d", prefix, year, month, number);
    }

    public static LocalDate firstDayOf(int year, int month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate lastDayOf(int year, int month) {
        return firstDayOf(year, month).with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate firstDayOf(int year) {
        return LocalDate.of(year, 1, 1);
    }

    public static LocalDate lastDayOf(int year) {
        return LocalDate.of(year, 12, 1).with(TemporalAdjusters.lastDayOfMonth());
    }

    public static int getMonth(String yearMonth) {
        return Integer.parseInt(yearMonth.substring(5, 7));
    }

    public static int getYear(String yearMonth) {
        return Integer.parseInt(yearMonth.substring(0, 4));
    }

    public static String getFullMonthOf(int monthInt) {
        return FULL_MONTHS[monthInt];
    }

    public static int getThaiYearOf(int year) {
        return year + 543;
    }

    public static LocalDate dateOf(String documentId) {
        String serialNumber = documentId.substring(documentId.length() - 10);
        int year = Integer.parseInt(serialNumber.substring(0, 4));
        int month = Integer.parseInt(serialNumber.substring(4, 6));

        return LocalDate.of(year, month, 1);
    }

}
