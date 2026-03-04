package fr.robie.craftengineconverter.api.configuration.item.settings;

import fr.robie.craftengineconverter.api.configuration.ItemConfigurationSerializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ProjectileSettingConfiguration implements ItemConfigurationSerializable {
    private final Map<String, Object> settings;

    public ProjectileSettingConfiguration(Map<String, Object> settings) {
        this.settings = settings;
    }

    @Override
    public void serialize(@NotNull YamlConfiguration yamlConfiguration, @NotNull String path, @NotNull ConfigurationSection itemSection, @NotNull String itemId) {
        ConfigurationSection settings = getOrCreateSection(itemSection, "settings");
        settings.set("projectile", this.settings);
    }
}
