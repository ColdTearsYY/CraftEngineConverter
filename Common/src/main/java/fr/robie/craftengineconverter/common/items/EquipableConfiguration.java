package fr.robie.craftengineconverter.common.items;

import fr.robie.craftengineconverter.common.utils.ItemConfigurationSerializable;
import net.momirealms.craftengine.core.entity.EquipmentSlot;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class EquipableConfiguration implements ItemConfigurationSerializable {
    private final String assetId;
    private final EquipmentSlot equipmentSlot;

    public EquipableConfiguration(String assetId, EquipmentSlot equipmentSlot) {
        this.assetId = assetId;
        this.equipmentSlot = equipmentSlot;
    }

    @Override
    public void serialize(@NotNull YamlConfiguration yamlConfiguration, @NotNull String path, @NotNull ConfigurationSection itemSection) {
        ConfigurationSection ceEquipableSection = this.assetId != null && !this.assetId.isBlank() ? getOrCreateSection(getOrCreateSection(itemSection, "settings"), "equippable") : getOrCreateSection(getOrCreateSection(itemSection, "data"), "equippable");
        if (this.assetId != null && !this.assetId.isBlank()) {
            ceEquipableSection.set("asset-id", this.assetId);
        }
        if (this.equipmentSlot != null) {
            ceEquipableSection.set("slot", this.equipmentSlot.name().toLowerCase());
        }
    }
}
