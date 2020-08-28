package com.github.jaraya81.util;

import com.github.jaraya81.exception.TelegramException;

public class Condition {

    public static void check(boolean condition, String reason) {
        if (!condition) {
            new TelegramException(reason);
        }
    }
}
