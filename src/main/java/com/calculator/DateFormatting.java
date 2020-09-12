package com.calculator;

public class DateFormatting {

    public static boolean checkFormat(String input) {

        return input.matches("([0-9]{2})-([0-9]{2})-([0-9]{2})");
    }
}
