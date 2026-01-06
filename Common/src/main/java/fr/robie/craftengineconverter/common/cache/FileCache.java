package fr.robie.craftengineconverter.common.cache;

import fr.robie.craftengineconverter.common.logger.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FileCache {
    private final Map<Path, FileCacheEntry> cache = new HashMap<>();

    /**
     * Retrieves the cache entry for the specified path.
     *
     * @param path The path of the file.
     * @return An Optional containing the FileCacheEntry if available, otherwise empty.
     */
    public Optional<FileCacheEntry> getEntry(Path path) {
        File file = path.toFile();

        if (!file.exists()) {
            this.cache.remove(path);
            return Optional.empty();
        }

        FileCacheEntry entry = this.cache.get(path);

        if (entry != null && entry.isUpToDate()) {
            return Optional.of(entry);
        }

        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            entry = new FileCacheEntry(file, config);
            this.cache.put(path, entry);
            return Optional.of(entry);
        } catch (Exception e) {
            Logger.showException("Error loading YAML configuration from file: " + file.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

    /**
     * Retrieves the YAML configuration for the specified path.
     *
     * @param path The path of the file.
     * @return An Optional containing the YamlConfiguration if available, otherwise empty.
     */
    public Optional<YamlConfiguration> getConfiguration(Path path) {
        return getEntry(path).map(FileCacheEntry::getYamlConfiguration);
    }

    /**
     * Invalidates the cache entry for the specified path.
     *
     * @param path The path of the file to invalidate.
     */
    public void invalidate(Path path) {
        this.cache.remove(path);
    }

    /**
     * Clears all entries from the cache.
     */
    public void clearAll() {
        this.cache.clear();
    }

    /**
     * Gets the current size of the cache.
     *
     * @return The number of entries in the cache.
     */
    public int size() {
        return this.cache.size();
    }


    /**
     * Cleans stale entries from the cache.
     *
     * @return The number of entries removed.
     */
    public int cleanStaleEntries() {
        int removed = 0;
        var iterator = this.cache.entrySet().iterator();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (!entry.getValue().isUpToDate() || !entry.getValue().getFile().exists()) {
                iterator.remove();
                removed++;
            }
        }

        return removed;
    }
}