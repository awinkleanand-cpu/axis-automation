package com.axismaxlife.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private ConfigReader() {
    }

    private static void loadProperties() {
        Path externalConfig = Path.of("resources", "config.properties");
        if (Files.exists(externalConfig)) {
            try (InputStream inputStream = Files.newInputStream(externalConfig)) {
                PROPERTIES.load(inputStream);
                return;
            } catch (IOException exception) {
                throw new IllegalStateException("Unable to read external config.properties", exception);
            }
        }

        try (InputStream inputStream = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("config.properties not found in classpath or resources folder");
            }
            PROPERTIES.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load config.properties", exception);
        }
    }

    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing config key: " + key);
        }
        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static Path resolvePath(String key) {
        return Path.of(get(key)).toAbsolutePath().normalize();
    }
}
