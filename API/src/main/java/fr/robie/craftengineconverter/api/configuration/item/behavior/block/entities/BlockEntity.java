package fr.robie.craftengineconverter.api.configuration.item.behavior.block.entities;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public interface BlockEntity {
    void serialize(@NotNull ConfigurationSection section);
}
