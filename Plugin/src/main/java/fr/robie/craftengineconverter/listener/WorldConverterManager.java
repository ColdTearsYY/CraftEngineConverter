package fr.robie.craftengineconverter.listener;

import fr.robie.craftengineconverter.common.converter.WorldConverter;
import fr.robie.craftengineconverter.common.records.ChunkPosition;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class WorldConverterManager implements Listener {
    private final Set<WorldConverter> converters = new HashSet<>();
    private final Set<ChunkPosition> processedChunks = new HashSet<>();

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event){
        Chunk chunk = event.getChunk();
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
    }

    public void registerConverter(WorldConverter converter){
        this.converters.add(converter);
    }
}
