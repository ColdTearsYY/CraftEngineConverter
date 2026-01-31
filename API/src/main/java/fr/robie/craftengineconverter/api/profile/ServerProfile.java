package fr.robie.craftengineconverter.api.profile;

import fr.robie.craftengineconverter.api.BlockHistory;

import java.util.Collection;

public interface ServerProfile {

    void load();

    void addBlockHistory(BlockHistory blockHistory);

    boolean isBlockConverted(String worldName, int blockX, int blockY, int blockZ);

    int getActiveBlockCount();

    void clearBlockHistory();

    void markBlockAsReverted(BlockHistory history);

    /**
     * Gets all active (non-reverted) block conversions from the cache.
     *
     * @return Collection of all active BlockHistory records in the cache
     */
    Collection<BlockHistory> getAllActiveConversions();

}
