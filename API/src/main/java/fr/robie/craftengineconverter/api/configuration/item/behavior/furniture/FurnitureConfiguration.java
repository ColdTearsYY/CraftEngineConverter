package fr.robie.craftengineconverter.api.configuration.item.behavior.furniture;

import fr.robie.craftengineconverter.api.configuration.ItemConfigurationSerializable;
import fr.robie.craftengineconverter.api.configuration.item.loottables.LootConfiguration;
import fr.robie.craftengineconverter.api.configuration.utils.FurniturePlacement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class FurnitureConfiguration implements ItemConfigurationSerializable {
    private FurnitureSettings furnitureSettings;
    private final Map<FurniturePlacement, Placement> placements = new EnumMap<>(FurniturePlacement.class);
    private LootConfiguration loot;

    @NotNull
    public FurnitureSettings getOrCreateSettings(String itemId) {
        if (this.furnitureSettings == null)
            this.furnitureSettings = new FurnitureSettings(itemId);
        return this.furnitureSettings;
    }

    public Placement getOrCreatePlacement(FurniturePlacement type) {
        return this.placements.computeIfAbsent(type, Placement::new);
    }

    public void setLoot(@Nullable LootConfiguration loot) {
        this.loot = loot;
    }

    @Override
    public void serialize(@NotNull YamlConfiguration yamlConfiguration, @NotNull String path, @NotNull ConfigurationSection itemSection, @NotNull String itemId) {
        ConfigurationSection behaviorSection = getOrCreateSection(itemSection, "behavior");
        behaviorSection.set("type", "furniture_item");

        ConfigurationSection settingsSection = getOrCreateSection(behaviorSection, "settings");
        settingsSection.set("item", itemId);

        ConfigurationSection furnitureSection = getOrCreateSection(behaviorSection, "furniture");
        if (this.furnitureSettings != null) {
            ConfigurationSection furnitureSettingsSection = getOrCreateSection(furnitureSection, "settings");
            furnitureSettingsSection.set("item", this.furnitureSettings.getItem());
            if (this.furnitureSettings.getHitTimes() != null) {
                furnitureSettingsSection.set("hit-times", this.furnitureSettings.getHitTimes());
            }
            if (this.furnitureSettings.getBreakSound() != null) {
                getOrCreateSection(furnitureSettingsSection, "sounds").set("break", this.furnitureSettings.getBreakSound());
            }
            if (this.furnitureSettings.getPlaceSound() != null) {
                getOrCreateSection(furnitureSettingsSection, "sounds").set("place", this.furnitureSettings.getPlaceSound());
            }
            if (this.furnitureSettings.getHitSound() != null) {
                getOrCreateSection(furnitureSettingsSection, "sounds").set("hit", this.furnitureSettings.getHitSound());
            }
        }

        if (this.loot != null) {
            this.loot.serialize(furnitureSection);
        }

        if (!this.placements.isEmpty()) {
            ConfigurationSection placementSection = getOrCreateSection(furnitureSection, "placement");
            for (Placement placement : this.placements.values()) {
                placement.serialize(placementSection);
            }
        }
    }
}