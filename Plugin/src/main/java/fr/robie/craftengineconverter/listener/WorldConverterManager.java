package fr.robie.craftengineconverter.listener;

import fr.robie.craftengineconverter.common.BlockStatesMapper;
import fr.robie.craftengineconverter.common.CraftEnginePlacementTracker;
import fr.robie.craftengineconverter.common.converter.WorldConverter;
import fr.robie.craftengineconverter.common.enums.Plugins;
import fr.robie.craftengineconverter.common.records.ChunkPosition;
import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorldConverterManager implements Listener {
    private final Set<WorldConverter> converters = new HashSet<>();
    private final Set<ChunkPosition> processedChunks = new HashSet<>();
    private final CraftEnginePlacementTracker placementTracker;

    public WorldConverterManager(CraftEnginePlacementTracker placementTracker) {
        this.placementTracker = placementTracker;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event){
        Chunk chunk = event.getChunk();
        processChunk(chunk);
    }

    private void processChunk(Chunk chunk) {
        int x = chunk.getX();
        int z = chunk.getZ();
        String worldName = chunk.getWorld().getName();
        ChunkPosition position = new ChunkPosition(worldName, x, z);
        if (processedChunks.contains(position)) {
            return;
        }
        processedChunks.add(position);
        @NotNull Entity[] entities = chunk.getEntities();
        for (Entity entity : entities) {
            if (entity instanceof ItemDisplay itemDisplay){
                for (WorldConverter converter : this.converters){
                    if (converter.applyItemDisplayConversion(position, itemDisplay)){
                        break;
                    }
                }
            }
        }
        BlockStatesMapper blockStatesMapper = BlockStatesMapper.getInstance();
        for (int cx = 0; cx < 16; cx++){
            for (int cy = 0; cy < chunk.getWorld().getMaxHeight(); cy++){
                for (int cz = 0; cz < 16; cz++){
                    Block block = chunk.getBlock(cx, cy, cz);
                    if (CraftEngineBlocks.isCustomBlock(block)) continue;
                    Location blockLocation = block.getLocation();
                    for (var worldConverter : this.converters){
                        Plugins plugin = worldConverter.getPlugin();
                        String ceEquivalent = blockStatesMapper.getCeEquivalent(plugin, block.getBlockData());
                        if (ceEquivalent != null) {
                            this.placementTracker.placeBlock(ceEquivalent, blockLocation);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void executeChunck(){
        List<World> worlds = Bukkit.getServer().getWorlds();
        for (var world : worlds){
            for (var chunk : world.getLoadedChunks()){
                processChunk(chunk);
            }
        }
    }

    public void registerConverter(WorldConverter converter){
        this.converters.add(converter);
    }

    public void clearProcessedChunks() {
        this.processedChunks.clear();
    }
}
