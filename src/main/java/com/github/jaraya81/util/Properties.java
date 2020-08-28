package com.github.jaraya81.util;

import com.github.jaraya81.exception.TelegramException;
import lombok.Builder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Builder
public class Properties {

    private static final String DEFAULT_FILEPATH = "local.properties";

    private String filepath;

    public static String get(String key, String filepath) throws TelegramException {
        return Properties.builder().filepath(filepath).build().getProperty(key);
    }

    public String getProperty(String key) throws TelegramException {
        java.util.Properties properties = new java.util.Properties();
        try {
            properties.load(
                    new InputStreamReader(
                            new FileInputStream(
                                    filepath != null && !filepath.isEmpty() ? filepath : DEFAULT_FILEPATH
                            ), StandardCharsets.UTF_8
                    )
            );
        } catch (IOException e) {
            throw new TelegramException(e);
        }
        return properties.getProperty(key);
    }
}
