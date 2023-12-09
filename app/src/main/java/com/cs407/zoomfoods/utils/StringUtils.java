package com.cs407.zoomfoods.utils;

public final class StringUtils {

    public static final String EMPTY_STRING = "";

    private StringUtils() {
    }

    public static boolean isBlank(String st) {
        return st != null && st.trim().equals(EMPTY_STRING);
    }

    public static boolean isNotBlank(String st) {
        return !isBlank(st);
    }
}
