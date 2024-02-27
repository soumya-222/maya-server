package com.opentext.mayaserver.datagenerators.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DateUtils {
    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final DateTimeFormatter SimpleDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public DateUtils() {
    }

    public static long toMillisecondsSinceEpochFromEpochSecond(long date) {
        return date * 1000L;
    }

    public static long toMillisecondsSinceEpoch(Date date) {
        return date.getTime();
    }

    public static long toMillisecondsSinceEpoch(java.sql.Date date) {
        return date.getTime();
    }

    public static long toMillisecondsSinceEpoch(LocalDate date) {
        return date.atStartOfDay(UTC).toEpochSecond() * 1000L;
    }

    public static long toMillisecondsSinceEpoch(LocalDateTime date) {
        return date.atZone(UTC).toInstant().toEpochMilli();
    }

    public static long toMillisecondsSinceEpochFromIso8601String(String iso8601) {
        DateTimeFormatter formatter;
        try {
            iso8601 = OffsetDateTime.parse(iso8601).toInstant().toString();
            formatter = DateTimeFormatter.ISO_DATE_TIME;
        } catch (DateTimeParseException var3) {
            formatter = (new DateTimeFormatterBuilder()).append(DateTimeFormatter.ISO_DATE_TIME).optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd().optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd().optionalStart().appendOffset("+HHMM", "+0000").optionalEnd().optionalStart().appendOffset("+HH", "Z").optionalEnd().toFormatter();
        }

        return LocalDateTime.parse(iso8601, formatter).atZone(UTC).toInstant().toEpochMilli();
    }

    public static long toMillisecondsSinceEpoch(Instant date) {
        return date.toEpochMilli();
    }

    public static long toSecondsSinceEpoch(Date date) {
        return date.getTime() / 1000L;
    }

    public static long toSecondsSinceEpoch(java.sql.Date date) {
        return date.getTime() / 1000L;
    }

    public static long toSecondsSinceEpoch(LocalDate date) {
        return date.atStartOfDay(UTC).toEpochSecond();
    }

    public static long toSecondsSinceEpoch(LocalDateTime date) {
        return date.atZone(UTC).toEpochSecond();
    }

    public static long toSecondsSinceEpochFromIso8601String(String iso8601) {
        DateTimeFormatter formatter;
        try {
            iso8601 = OffsetDateTime.parse(iso8601).toInstant().toString();
            formatter = DateTimeFormatter.ISO_DATE_TIME;
        } catch (DateTimeParseException var3) {
            formatter = (new DateTimeFormatterBuilder()).append(DateTimeFormatter.ISO_DATE_TIME).optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd().optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd().optionalStart().appendOffset("+HHMM", "+0000").optionalEnd().optionalStart().appendOffset("+HH", "Z").optionalEnd().toFormatter();
        }

        return LocalDateTime.parse(iso8601, formatter).atZone(UTC).toEpochSecond();
    }

    public static long toSecondsSinceEpoch(Instant date) {
        return date.getEpochSecond();
    }

    public static Date toJavaUtilDate(java.sql.Date date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return new Date(epoch);
    }

    public static Date toJavaUtilDateFromEpochSeconds(long date) {
        return new Date(toMillisecondsSinceEpochFromEpochSecond(date));
    }

    public static Date toJavaUtilDateFromEpochMilli(long date) {
        return new Date(date);
    }

    public static Date toJavaUtilDate(LocalDate date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return new Date(epoch);
    }

    public static Date toJavaUtilDate(LocalDateTime date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return new Date(epoch);
    }

    public static Date toJavaUtilDateFromIso8601String(String iso8601) {
        long epoch = toMillisecondsSinceEpochFromIso8601String(iso8601);
        return new Date(epoch);
    }

    public static Date toJavaUtilDate(Instant date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return new Date(epoch);
    }

    public static java.sql.Date toSqlDate(Date date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return new java.sql.Date(epoch);
    }

    public static java.sql.Date toSqlDateFromEpochMilli(long date) {
        return new java.sql.Date(date);
    }

    public static java.sql.Date toSqlDateFromEpochSeconds(long date) {
        return new java.sql.Date(toMillisecondsSinceEpochFromEpochSecond(date));
    }

    public static java.sql.Date toSqlDate(LocalDate date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return new java.sql.Date(epoch);
    }

    public static java.sql.Date toSqlDate(LocalDateTime date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return new java.sql.Date(epoch);
    }

    public static java.sql.Date toSqlDateFromIso8601String(String iso8601) {
        long epoch = toMillisecondsSinceEpochFromIso8601String(iso8601);
        return new java.sql.Date(epoch);
    }

    public static java.sql.Date toSqlDate(Instant date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return new java.sql.Date(epoch);
    }

    public static LocalDate toLocalDate(Date date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return Instant.ofEpochMilli(epoch).atZone(UTC).toLocalDate();
    }

    public static LocalDate toLocalDateFromEpochMilli(long date) {
        return Instant.ofEpochMilli(date).atZone(UTC).toLocalDate();
    }

    public static LocalDate toLocalDateFromEpochSecond(long date) {
        return Instant.ofEpochSecond(date).atZone(UTC).toLocalDate();
    }

    public static LocalDate toLocalDate(java.sql.Date date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return Instant.ofEpochMilli(epoch).atZone(UTC).toLocalDate();
    }

    public static LocalDate toLocalDate(LocalDateTime date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return Instant.ofEpochMilli(epoch).atZone(UTC).toLocalDate();
    }

    public static LocalDate toLocalDateFromIso8601String(String iso8601) {
        long epoch = toMillisecondsSinceEpochFromIso8601String(iso8601);
        return Instant.ofEpochMilli(epoch).atZone(UTC).toLocalDate();
    }

    public static LocalDate toLocalDate(Instant date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return Instant.ofEpochMilli(epoch).atZone(UTC).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(LocalDate date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return Instant.ofEpochMilli(epoch).atZone(UTC).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTimeFromEpochMilli(long date) {
        return Instant.ofEpochMilli(date).atZone(UTC).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTimeFromEpochSecond(long date) {
        return Instant.ofEpochSecond(date).atZone(UTC).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(java.sql.Date date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return Instant.ofEpochMilli(epoch).atZone(UTC).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return Instant.ofEpochMilli(epoch).atZone(UTC).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTimeFromIso8601String(String iso8601) {
        long epoch = toMillisecondsSinceEpochFromIso8601String(iso8601);
        return Instant.ofEpochMilli(epoch).atZone(UTC).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(Instant date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return Instant.ofEpochMilli(epoch).atZone(UTC).toLocalDateTime();
    }

    public static String toIso8601String(LocalDate date) {
        LocalDateTime ldt = toLocalDateTime(date);
        ZonedDateTime zoned = ldt.atZone(UTC);
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zoned);
    }

    public static String toIso8601StringFromEpochMilli(long date) {
        LocalDateTime ldt = toLocalDateTimeFromEpochMilli(date);
        ZonedDateTime zoned = ldt.atZone(UTC);
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zoned);
    }

    public static String toIso8601StringFromEpochSecond(long date) {
        LocalDateTime ldt = toLocalDateTimeFromEpochSecond(date);
        ZonedDateTime zoned = ldt.atZone(UTC);
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zoned);
    }

    public static String toIso8601String(java.sql.Date date) {
        LocalDateTime ldt = toLocalDateTime(date);
        ZonedDateTime zoned = ldt.atZone(UTC);
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zoned);
    }

    public static String toIso8601String(Date date) {
        LocalDateTime ldt = toLocalDateTime(date);
        ZonedDateTime zoned = ldt.atZone(UTC);
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zoned);
    }

    public static String toIso8601String(LocalDateTime date) {
        ZonedDateTime zoned = date.atZone(UTC);
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zoned);
    }

    public static String toIso8601String(Instant date) {
        LocalDateTime ldt = toLocalDateTime(date);
        ZonedDateTime zoned = ldt.atZone(UTC);
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zoned);
    }

    public static Instant toInstant(LocalDate date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return Instant.ofEpochMilli(epoch);
    }

    public static Instant toInstantFromEpochMilli(long date) {
        return Instant.ofEpochMilli(date);
    }

    public static Instant toInstantFromEpochSecond(long date) {
        return Instant.ofEpochSecond(date);
    }

    public static Instant toInstant(java.sql.Date date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return Instant.ofEpochMilli(epoch);
    }

    public static Instant toInstant(Date date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return Instant.ofEpochMilli(epoch);
    }

    public static Instant toInstant(LocalDateTime date) {
        long epoch = toMillisecondsSinceEpoch(date);
        return Instant.ofEpochMilli(epoch);
    }

    public static Instant toInstantFromIso8601String(String iso8601) {
        long epoch = toMillisecondsSinceEpochFromIso8601String(iso8601);
        return Instant.ofEpochMilli(epoch);
    }


    public static ZonedDateTime createZonedDateTime(LocalDate date, int hour, int minutes, int second) {
        LocalDate startDate = date;
        LocalTime localTime = LocalTime.of(hour, minutes, second);
        LocalDateTime localDateTime = LocalDateTime.of(startDate, localTime);
        return ZonedDateTime.of(localDateTime, ZoneId.of("Etc/UTC"));
    }

    public static List<String> prepareLineItemUsageDate(LocalDate date, int hour, int minutes, int second) {
        ZonedDateTime startZoneDateTime = createZonedDateTime(date, hour, minutes, second);
        String usageStartDate = DateTimeFormatter.ISO_INSTANT.format(startZoneDateTime);
        List<String> dateList = new ArrayList<>();
        ZonedDateTime EndZonedDateTime = startZoneDateTime.plusDays(1);
        String usageEndDate = DateTimeFormatter.ISO_INSTANT.format(EndZonedDateTime);
        dateList.add(usageStartDate);
        dateList.add(usageEndDate);
        return dateList;
    }

    public static String prepareBillDate(String date, int hour, int minutes, int second) {
        LocalDate startDate = toLocalDateFormat(date);
        ZonedDateTime zonedDateTime = createZonedDateTime(startDate, hour, minutes, second);
        return DateTimeFormatter.ISO_INSTANT.format(zonedDateTime);
    }

    public static LocalDate generateRandomDateInBetween(String startDate, String endDate) {
        long startEpochDay = toLocalDateFormat(startDate).toEpochDay();
        long endEpochDay = toLocalDateFormat(endDate).toEpochDay();
        long randomDay = ThreadLocalRandom
                .current()
                .nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }

    public static LocalDate toLocalDateFormat(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(date, dateTimeFormatter);
    }

    public static String getCurrentUtcDateTimeString() {
        return SimpleDateTimeFormatter.format(ZonedDateTime.now(UTC));
    }
}
