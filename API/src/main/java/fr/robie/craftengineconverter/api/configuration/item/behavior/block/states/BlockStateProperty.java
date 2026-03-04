package fr.robie.craftengineconverter.api.configuration.item.behavior.block.states;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public interface BlockStateProperty<T> {
    @NotNull String getName();

    @NotNull
    T getValue();

    default ConfigurationSection getOrCreateSection(@NotNull ConfigurationSection parentSection, @NotNull String path) {
        if (parentSection.isConfigurationSection(path)) {
            return parentSection.getConfigurationSection(path);
        }
        return parentSection.createSection(path);
    }

    void serialize(@NotNull ConfigurationSection propertiesSection);
}
