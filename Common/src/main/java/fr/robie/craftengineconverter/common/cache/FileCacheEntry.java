package fr.robie.craftengineconverter.common.cache;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileCacheEntry {
    private final long lastModified;
    private final File file;
    private final YamlConfiguration yamlConfiguration;

    public FileCacheEntry(File file, YamlConfiguration yamlConfiguration) {
        this.file = file;
        this.yamlConfiguration = yamlConfiguration;
        this.lastModified = file.lastModified();
    }

    public boolean isUpToDate() {
        return file.exists() && file.lastModified() == lastModified;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getYamlConfiguration() {
        return yamlConfiguration;
    }

    public long getLastModified() {
        return lastModified;
    }
}