package fr.robie.craftengineconverter.api.configurations;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public interface ItemConfigurationSerializable {

    @NotNull
    default ConfigurationSection getOrCreateSection(@NotNull ConfigurationSection parent, String key) {
        if (parent.isConfigurationSection(key)) {
            return parent.getConfigurationSection(key);
        }
        return parent.createSection(key);
    }

    void serialize(@NotNull YamlConfiguration yamlConfiguration, @NotNull String path, @NotNull ConfigurationSection itemSection, @NotNull String itemId);
}
