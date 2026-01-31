package fr.robie.craftengineconverter.api.database;

import fr.robie.craftengineconverter.api.BlockHistory;

import java.util.Optional;

public interface StorageManager {

    void loadDatabase();

    void upsertBlockHistory(BlockHistory blockHistory);

    void markBlockAsReverted(BlockHistory blockHistory);

    Optional<BlockHistory> getBlockHistory(String worldName, int blockX, int blockY, int blockZ);

    boolean isBlockConverted(String worldName, int blockX, int blockY, int blockZ);

    java.util.List<BlockHistory> getChunkHistory(String worldName, int chunkX, int chunkZ);

    java.util.List<BlockHistory> getAllActiveConversions();

    long getTotalConversions();

    long getActiveConversions();

    boolean isEnabled();

    void close();
}
