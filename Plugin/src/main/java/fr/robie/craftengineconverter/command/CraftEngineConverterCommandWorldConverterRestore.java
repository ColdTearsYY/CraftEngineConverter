package fr.robie.craftengineconverter.command;

import fr.robie.craftengineconverter.CraftEngineConverter;
import fr.robie.craftengineconverter.api.BlockHistory;
import fr.robie.craftengineconverter.api.database.StorageManager;
import fr.robie.craftengineconverter.api.profile.ServerProfile;
import fr.robie.craftengineconverter.common.builder.TimerBuilder;
import fr.robie.craftengineconverter.common.format.Message;
import fr.robie.craftengineconverter.common.logger.Logger;
import fr.robie.craftengineconverter.common.permission.Permission;
import fr.robie.craftengineconverter.utils.command.CommandType;
import fr.robie.craftengineconverter.utils.command.VCommand;
import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CraftEngineConverterCommandWorldConverterRestore extends VCommand {

    public CraftEngineConverterCommandWorldConverterRestore(CraftEngineConverter plugin) {
        super(plugin);
        this.setPermission(Permission.COMMAND_WORLDCONVERTER_RESTORE);
        this.setDescription(Message.COMMAND__WORLD_CONVERTER__RESTORE__DESCRIPTION);
        this.addSubCommand("restore");
        this.addFlag("--confirm");
    }

    @Override
    protected CommandType perform(CraftEngineConverter plugin) {
        StorageManager dataBaseManager = this.plugin.getStorageManager();
        ServerProfile serverProfile = this.plugin.getServerProfile();

        if (!dataBaseManager.isEnabled()) {
            message(plugin, sender, Message.COMMAND__WORLD_CONVERTER__RESTORE__DATABASE_DISABLED);
            return CommandType.SUCCESS;
        }

        boolean confirm = this.containFlag("--confirm");

        // Get active conversions count from cache instead of database
        int activeConversions = serverProfile.getActiveBlockCount();

        if (activeConversions == 0) {
            message(plugin, sender, Message.COMMAND__WORLD_CONVERTER__RESTORE__ALL__CONFIRM,
                    "blocks", 0);
            return CommandType.SUCCESS;
        }

        if (!confirm) {
            message(plugin, sender, Message.COMMAND__WORLD_CONVERTER__RESTORE__ALL__CONFIRM,
                    "blocks", activeConversions);
            return CommandType.SUCCESS;
        }

        message(plugin, sender, Message.COMMAND__WORLD_CONVERTER__RESTORE__ALL__START,
                "blocks", activeConversions);

        long startTime = System.currentTimeMillis();
        AtomicInteger restoredCount = new AtomicInteger(0);
        AtomicInteger totalCount = new AtomicInteger(0);

        Collection<BlockHistory> allHistory = new ArrayList<>(serverProfile.getAllActiveConversions());

        final int BATCH_SIZE = 50;

        for (int i = 0; i < allHistory.size(); i += BATCH_SIZE) {
            final int end = Math.min(i + BATCH_SIZE, allHistory.size());
            final List<BlockHistory> batch = new ArrayList<>(allHistory).subList(i, end);
            final long tickDelay = i / BATCH_SIZE;

            plugin.getFoliaCompatibilityManager().runLater(() -> {
                for (BlockHistory history : batch) {
                    totalCount.incrementAndGet();

                    World world = Bukkit.getWorld(history.getWorldName());
                    if (world == null) {
                        continue;
                    }

                    org.bukkit.Chunk chunk = world.getChunkAt(history.getChunkX(), history.getChunkZ());
                    if (!chunk.isLoaded()) {
                        chunk.load();
                    }

                    Location location = new Location(
                            world,
                            history.getBlockX(),
                            history.getBlockY(),
                            history.getBlockZ()
                    );

                    try {
                        restoreBlock(location, history);
                        serverProfile.markBlockAsReverted(history);
                        restoredCount.incrementAndGet();
                    } catch (Exception e) {
                        Logger.showException("Failed to restore block at " + location, e);
                    }
                }
            }, tickDelay);
        }

        long totalDelayTicks = (long) Math.ceil((double) allHistory.size() / BATCH_SIZE);

        plugin.getFoliaCompatibilityManager().runLater(() -> {

            long endTime = System.currentTimeMillis();

            message(plugin, sender, Message.COMMAND__WORLD_CONVERTER__RESTORE__ALL__COMPLETE,
                    "restored", restoredCount.get(),
                    "total", totalCount.get(),
                    "time", TimerBuilder.formatTimeAuto(endTime - startTime));
        }, totalDelayTicks + 1);

        return CommandType.SUCCESS;
    }

    /**
     * Restores a block to its original state.
     *
     * @param location The location of the block
     * @param history The block history containing the original block data
     */
    private void restoreBlock(Location location, BlockHistory history) {
        Block block = location.getBlock();

        if (CraftEngineBlocks.isCustomBlock(block)) {
            CraftEngineBlocks.remove(block);

        }

        try {
            org.bukkit.block.data.BlockData blockData = Bukkit.createBlockData(history.getOriginalBlock());
            block.setBlockData(blockData, false);
        } catch (Exception e) {
            Logger.showException("Failed to parse block data: " + history.getOriginalBlock(), e);
            try {
                String materialName = history.getOriginalBlock().split("\\[")[0];
                Material material = Material.matchMaterial(materialName);
                if (material != null) {
                    block.setType(material, false);
                }
            } catch (Exception ex) {
                Logger.showException("Failed to restore block, setting to AIR", ex);
                block.setType(Material.AIR, false);
            }
        }
    }
}
