package com.example.haruapp.weather.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {
    public static String getBaseDate() {
        LocalDateTime now = LocalDateTime.now().minusMinutes(45);  // 시간 보정
        return now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String getBaseTime() {
        LocalDateTime now = LocalDateTime.now().minusMinutes(45);
        int minute = now.getMinute();
        int baseMinute = (minute / 30) * 30;
        LocalDateTime adjusted = now.withMinute(baseMinute).withSecond(0).withNano(0);
        return adjusted.format(DateTimeFormatter.ofPattern("HHmm"));  // "0630"
    }

}
