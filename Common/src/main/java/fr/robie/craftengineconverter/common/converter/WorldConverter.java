package fr.robie.craftengineconverter.common.converter;

import fr.robie.craftengineconverter.common.enums.Plugins;
import fr.robie.craftengineconverter.common.records.ChunkPosition;
import org.bukkit.entity.ItemDisplay;
import org.jetbrains.annotations.NotNull;

public interface WorldConverter {

    boolean applyItemDisplayConversion(ChunkPosition chunkPosition, ItemDisplay itemDisplay);

    @NotNull
    Plugins getPlugin();
}
