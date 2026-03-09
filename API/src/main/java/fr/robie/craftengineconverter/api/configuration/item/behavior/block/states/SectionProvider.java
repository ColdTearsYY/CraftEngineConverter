package fr.robie.craftengineconverter.api.configuration.item.behavior.block.states;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public interface SectionProvider {
    default ConfigurationSection getOrCreateSection(@NotNull ConfigurationSection parentSection, @NotNull String path) {
        if (parentSection.contains(path)) {
            return parentSection.getConfigurationSection(path);
        } else {
            return parentSection.createSection(path);
        }
    }
}
