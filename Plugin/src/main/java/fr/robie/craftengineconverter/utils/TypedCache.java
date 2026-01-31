package fr.robie.craftengineconverter.utils;

import fr.robie.craftengineconverter.common.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Generic cache for batching objects before processing them.
 * Thread-safe implementation using ConcurrentHashMap with upsert behavior.
 *
 * @param <T> The type of objects to cache
 */
public class TypedCache<T> {
    private final Class<T> type;
    private final Map<String, T> cache;
    private final Consumer<List<T>> batchProcessor;
    private final int maxBatchSize;
    private final Function<T, String> keyExtractor;

    /**
     * Creates a new TypedCache.
     *
     * @param type The class type
     * @param batchProcessor Function to process a batch of objects
     * @param maxBatchSize Maximum number of objects to process per batch
     * @param keyExtractor Function to extract a unique key from an object
     */
    public TypedCache(Class<T> type, Consumer<List<T>> batchProcessor, int maxBatchSize, Function<T, String> keyExtractor) {
        this.type = type;
        this.cache = new ConcurrentHashMap<>();
        this.batchProcessor = batchProcessor;
        this.maxBatchSize = maxBatchSize;
        this.keyExtractor = keyExtractor;
    }

    /**
     * Adds an object to the cache.
     * If an object with the same key already exists, it will be replaced (upsert behavior).
     *
     * @param object The object to cache
     */
    public void add(T object) {
        if (object != null) {
            String key = this.keyExtractor.apply(object);
            this.cache.put(key, object);
        }
    }

    /**
     * Processes up to maxBatchSize objects from the cache.
     *
     * @return Number of objects processed
     */
    public void processBatch() {
        if (this.cache.isEmpty()) {
            return;
        }

        List<T> batch = new ArrayList<>();
        List<String> keysToRemove = new ArrayList<>();
        int count = 0;

        for (Map.Entry<String, T> entry : this.cache.entrySet()) {
            if (count >= this.maxBatchSize) {
                break;
            }
            batch.add(entry.getValue());
            keysToRemove.add(entry.getKey());
            count++;
        }

        if (!batch.isEmpty()) {
            try {
                this.batchProcessor.accept(batch);
                keysToRemove.forEach(this.cache::remove);
            } catch (Exception e) {
                Logger.showException(
                    "Failed to process batch of " + this.type.getSimpleName() + " objects",
                    e
                );
            }
        }
    }

    /**
     * Processes all remaining objects in the cache without batch size limit.
     * Should be called during shutdown.
     */
    public void flush() {
        while (!this.cache.isEmpty()) {
            List<T> batch = new ArrayList<>(this.cache.values());
            List<String> keysToRemove = new ArrayList<>(this.cache.keySet());
            
            if (!batch.isEmpty()) {
                try {
                    this.batchProcessor.accept(batch);
                    keysToRemove.forEach(this.cache::remove);
                } catch (Exception e) {
                    Logger.showException(
                        "Failed to flush " + this.type.getSimpleName() + " objects",
                        e
                    );
                    break;
                }
            }
        }
    }

    /**
     * Gets the number of objects currently in the cache.
     *
     * @return Cache size
     */
    public int size() {
        return this.cache.size();
    }

    /**
     * Checks if the cache is empty.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return this.cache.isEmpty();
    }

    /**
     * Gets the type of objects this cache handles.
     *
     * @return The class type
     */
    public Class<T> getType() {
        return this.type;
    }

    /**
     * Clears all objects from the cache without processing them.
     */
    public void clear() {
        this.cache.clear();
    }
}

