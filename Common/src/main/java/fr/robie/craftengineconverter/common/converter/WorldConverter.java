package fr.robie.craftengineconverter.common.converter;

import fr.robie.craftengineconverter.common.records.ChunkPosition;
import org.bukkit.entity.ItemDisplay;

public interface WorldConverter {

    boolean applyItemDisplayConversion(ChunkPosition chunkPosition, ItemDisplay itemDisplay);
}
