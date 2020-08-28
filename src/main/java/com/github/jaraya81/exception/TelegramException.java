package com.github.jaraya81.exception;

import java.io.IOException;

public class TelegramException extends Exception {
    public TelegramException(String reason) {
        super(reason);
    }

    public TelegramException(IOException e) {
        super(e);
    }

    public TelegramException(String msg, TelegramException e) {
    }
}
