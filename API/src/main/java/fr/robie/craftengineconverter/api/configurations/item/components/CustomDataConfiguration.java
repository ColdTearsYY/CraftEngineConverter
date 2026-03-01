package fr.robie.craftengineconverter.api.configurations.item.components;

import fr.robie.craftengineconverter.api.configurations.ItemConfigurationSerializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomDataConfiguration implements ItemConfigurationSerializable {
    private final List<CustomDataEntry> customDataEntries;

    public CustomDataConfiguration(@NotNull List<CustomDataEntry> customDataEntries) {
        this.customDataEntries = customDataEntries;
    }


    @Override
    public void serialize(@NotNull YamlConfiguration yamlConfiguration, @NotNull String path, @NotNull ConfigurationSection itemSection, @NotNull String itemId) {
        ConfigurationSection components = getOrCreateSection(itemSection, "components");
        ConfigurationSection customDataComponent = getOrCreateSection(components, "minecraft:custom_data");
        for (CustomDataEntry entry : this.customDataEntries) {
            customDataComponent.set(entry.key(), entry.value());
        }
    }

    public record CustomDataEntry(String key, Object value) {
    }
}
