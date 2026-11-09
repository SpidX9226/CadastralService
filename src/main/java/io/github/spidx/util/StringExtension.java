package io.github.spidx.util;

public final class StringExtension {
    private StringExtension() {
    }

    // true even if contains only whitespaces
    public static boolean IsNullOrEmpty(String str) {
        return str == null || str.isBlank();
    }
}
