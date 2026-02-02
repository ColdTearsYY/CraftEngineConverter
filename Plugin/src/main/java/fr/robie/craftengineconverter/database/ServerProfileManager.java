package fr.robie.craftengineconverter.database;

import fr.robie.craftengineconverter.CraftEngineConverter;
import fr.robie.craftengineconverter.api.BlockHistory;
import fr.robie.craftengineconverter.api.database.StorageManager;
import fr.robie.craftengineconverter.api.profile.ServerProfile;
import fr.robie.craftengineconverter.common.logger.LogType;
import fr.robie.craftengineconverter.common.logger.Logger;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages an in-memory cache of non-reverted block conversions.
 * This cache improves performance by reducing database queries when checking if blocks are already converted.
 */
public class ServerProfileManager implements ServerProfile {
    private final CraftEngineConverter plugin;
    private final StorageManager storageManager;

    private final Map<String, BlockHistory> activeBlockCache = new ConcurrentHashMap<>();

    public ServerProfileManager(CraftEngineConverter plugin) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();
    }

    /**
     * Loads all non-reverted block histories from the database into memory cache.
     */
    @Override
    public void load() {
        if (!this.storageManager.isEnabled()) {
            return;
        }

        long startTime = System.currentTimeMillis();

        List<BlockHistory> allBlockHistories = this.storageManager.getAllActiveConversions();
        
        for (BlockHistory history : allBlockHistories) {
            if (!history.isReverted()) {
                String key = createCacheKey(
                    history.getWorldName(),
                    history.getChunkX(),
                    history.getChunkZ(),
                    history.getBlockX(),
                    history.getBlockY(),
                    history.getBlockZ()
                );
                this.activeBlockCache.put(key, history);
            }
        }

        long loadTime = System.currentTimeMillis() - startTime;
        Logger.info(
            String.format("Loaded %d active block conversions into cache in %dms", 
                this.activeBlockCache.size(), loadTime),
            LogType.SUCCESS
        );
    }

    /**
     * Creates a cache key from block and chunk coordinates.
     *
     * @param worldName The world name
     * @param chunkX The chunk X coordinate
     * @param chunkZ The chunk Z coordinate
     * @param blockX The block X coordinate
     * @param blockY The block Y coordinate
     * @param blockZ The block Z coordinate
     * @return The cache key string in format: world=name:chunkx:chunkz:blockx:blocky:blockz
     */
    @NotNull
    private String createCacheKey(@NotNull String worldName, int chunkX, int chunkZ, int blockX, int blockY, int blockZ) {
        return worldName + ":" + chunkX + ":" + chunkZ + ":" + blockX + ":" + blockY + ":" + blockZ;
    }

    /**
     * Adds a new block conversion to the cache.
     * Should be called after inserting into the database.
     *
     * @param blockHistory The block history to add to cache
     */
    @Override
    public void addBlockHistory(@NotNull BlockHistory blockHistory) {
        if (!blockHistory.isReverted()) {
            String key = createCacheKey(
                blockHistory.getWorldName(), 
                blockHistory.getChunkX(),
                blockHistory.getChunkZ(),
                blockHistory.getBlockX(),
                blockHistory.getBlockY(), 
                blockHistory.getBlockZ()
            );
            this.activeBlockCache.put(key, blockHistory);
        }
        this.storageManager.upsertBlockHistory(blockHistory);
    }

    /**
     * Checks if a block is already converted (and not reverted) using the cache.
     * This is a fast O(1) operation.
     *
     * @param worldName The world name
     * @param blockX The block X coordinate
     * @param blockY The block Y coordinate
     * @param blockZ The block Z coordinate
     * @return true if the block is converted and not reverted
     */
    @Override
    public boolean isBlockConverted(@NotNull String worldName, int blockX, int blockY, int blockZ) {
        int chunkX = blockX >> 4; // Equivalent to blockX / 16
        int chunkZ = blockZ >> 4; // Equivalent to blockZ / 16
        String key = createCacheKey(worldName, chunkX, chunkZ, blockX, blockY, blockZ);
        return this.activeBlockCache.containsKey(key);
    }

    /**
     * Gets the current number of cached block conversions.
     *
     * @return The cache size
     */
    @Override
    public int getActiveBlockCount() {
        return activeBlockCache.size();
    }

    /**
     * Clears all entries from the cache.
     */
    @Override
    public void clearBlockHistory() {
        for (var blockHistory : this.activeBlockCache.values()) {
            this.storageManager.markBlockAsReverted(blockHistory);
        }
        this.activeBlockCache.clear();
    }

    @Override
    public void markBlockAsReverted(@NonNull BlockHistory history) {
        String key = createCacheKey(
            history.getWorldName(),
            history.getChunkX(),
            history.getChunkZ(),
            history.getBlockX(),
            history.getBlockY(),
            history.getBlockZ()
        );
        this.activeBlockCache.remove(key);
        this.storageManager.markBlockAsReverted(history);
    }

    /**
     * Gets all active (non-reverted) block conversions from the cache.
     *
     * @return Collection of all active BlockHistory records in the cache
     */
    @Override
    public @NonNull Collection<BlockHistory> getAllActiveConversions() {
        return this.activeBlockCache.values();
    }
}
