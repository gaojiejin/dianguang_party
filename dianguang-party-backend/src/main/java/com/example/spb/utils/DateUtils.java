package com.example.spb.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

//@Component
public class DateUtils {
    public static String getCurrentYear() {
        return String.valueOf(LocalDate.now().getYear());
    }

    public static String getCurrentMonth() {
        return String.format("%02d", LocalDate.now().getMonthValue());
    }

    public static String getCurrentDay() {
        return String.format("%02d", LocalDate.now().getDayOfMonth());
    }
}
