package fr.robie.craftengineconverter.api.configurations.item.behavior.furniture;

import fr.robie.craftengineconverter.api.configurations.ItemConfigurationSerializable;
import fr.robie.craftengineconverter.api.configurations.utils.FurniturePlacement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class FurnitureConfiguration implements ItemConfigurationSerializable {
    private Settings settings;
    private Map<String,Object> loot;
    private final Map<FurniturePlacement, Placement> placements = new EnumMap<>(FurniturePlacement.class);

    @NotNull
    public Settings getOrCreateSettings(String itemId) {
        if (this.settings == null)
            this.settings = new Settings(itemId);
        return this.settings;
    }

    public Placement getOrCreatePlacement(FurniturePlacement type) {
        return this.placements.computeIfAbsent(type, Placement::new);
    }

    public void setLoot(Map<String, Object> loot) {
        this.loot = loot;
    }

    @Override
    public void serialize(@NotNull YamlConfiguration yamlConfiguration, @NotNull String path, @NotNull ConfigurationSection itemSection, @NotNull String itemId) {
        ConfigurationSection behaviorSection = getOrCreateSection(itemSection, "behavior");
        behaviorSection.set("type", "furniture_item");

        ConfigurationSection settingsSection = getOrCreateSection(behaviorSection, "settings");
        settingsSection.set("item", itemId);

        ConfigurationSection furnitureSection = getOrCreateSection(behaviorSection, "furniture");
        if (this.settings != null) {
            ConfigurationSection furnitureSettingsSection = getOrCreateSection(furnitureSection, "settings");
            furnitureSettingsSection.set("item", this.settings.getItem());
            if (this.settings.getHitTimes() != null) {
                furnitureSettingsSection.set("hit-times", this.settings.getHitTimes());
            }
            if (this.settings.getBreakSound() != null) {
                getOrCreateSection(furnitureSettingsSection, "sounds").set("break", this.settings.getBreakSound());
            }
            if (this.settings.getPlaceSound() != null) {
                getOrCreateSection(furnitureSettingsSection, "sounds").set("place", this.settings.getPlaceSound());
            }
            if (this.settings.getHitSound() != null) {
                getOrCreateSection(furnitureSettingsSection, "sounds").set("hit", this.settings.getHitSound());
            }
        }

        if (this.loot != null) {
            furnitureSection.createSection("loot", this.loot);
        }


        if (!this.placements.isEmpty()) {
            ConfigurationSection placementSection = getOrCreateSection(furnitureSection, "placement");
            for (Placement placement : this.placements.values()) {
                placement.serialize(placementSection);
            }
        }
    }
}