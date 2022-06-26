package net.buycraft.plugin.shared.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class BuycraftConfiguration {
    private final Properties properties;

    public BuycraftConfiguration() {
        this.properties = new Properties();
    }

    private void defaultSet(String key, String value) {
        if (properties.getProperty(key) == null)
            properties.setProperty(key, value);
    }

    public void load(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            properties.load(reader);
        }
    }

    public void save(Path path) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            properties.store(writer, "BuycraftX configuration file");
        }
    }

    public String getServerKey() {
        return properties.getProperty("server-key", null);
    }

    public boolean isPushCommandsEnabled() {
        return getBoolean("push-commands", false);
    }

    public boolean isAdminCommandsEnabled() {
        return getBoolean("admin-commands", false);
    }

    public Integer getPushCommandsPort() {
        String value = properties.getProperty("push-commands", "8282");
        if (value == null || value.equalsIgnoreCase("false")) {
            return null;
        }
        return Integer.valueOf(value);
    }

    public boolean isVerbose() {
        return getBoolean("verbose", false);
    }

    private boolean getBoolean(String key, boolean val) {
        if (!properties.containsKey(key))
            return val;
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    private Locale getLocale() {
        return Locale.forLanguageTag(properties.getProperty("language", "en_US"));
    }

    public BuycraftI18n createI18n() {
        return new BuycraftI18n(getLocale());
    }

    public void fillDefaults() {
        defaultSet("server-key", "INVALID");
        defaultSet("language", Locale.getDefault().toLanguageTag());
        defaultSet("verbose", "true");
        defaultSet("admin-commands", "false");
        defaultSet("push-commands", "false");
    }
}
