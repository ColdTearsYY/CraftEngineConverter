package fr.robie.craftengineconverter.api.configuration.item.behavior.block;

import fr.robie.craftengineconverter.api.configuration.ItemConfigurationSerializable;
import fr.robie.craftengineconverter.api.configuration.item.behavior.block.states.StateBlock;
import fr.robie.craftengineconverter.api.configuration.item.loottables.LootConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlockConfiguration implements ItemConfigurationSerializable {
    private final BlockSettings blockSettings;
    private final List<BlockBehavior> behaviors = new ArrayList<>();
    private StateBlock stateBlock;
    private LootConfiguration lootConfiguration;

    public BlockConfiguration(@NotNull String itemId) {
        this.blockSettings = new BlockSettings(itemId);
    }

    @NotNull
    public BlockSettings getBlockSettings() {
        return blockSettings;
    }

    public void addBehavior(@NotNull BlockBehavior behavior) {
        this.behaviors.add(behavior);
    }

    public void setStateBlock(@NotNull StateBlock stateBlock) {
        this.stateBlock = stateBlock;
    }

    public void setLootConfiguration(@NotNull LootConfiguration lootConfiguration) {
        this.lootConfiguration = lootConfiguration;
    }


    @Override
    public void serialize(@NotNull YamlConfiguration yamlConfiguration, @NotNull String path, @NotNull ConfigurationSection itemSection, @NotNull String itemId) {
        ConfigurationSection behaviorSection = getOrCreateSection(itemSection, "behavior");
        behaviorSection.set("type", "block_item");

        ConfigurationSection blockBehaviorSection = getOrCreateSection(behaviorSection, "block");
        ConfigurationSection blockSettingsSection = getOrCreateSection(blockBehaviorSection, "settings");
        this.blockSettings.serialize(blockSettingsSection);

        if (!this.behaviors.isEmpty()) {
            List<Map<String, Object>> behaviorsList = new ArrayList<>();
            for (BlockBehavior behavior : this.behaviors) {
                behaviorsList.add(behavior.serialize());
            }
            blockBehaviorSection.set("behaviors", behaviorsList);
        }

        if (this.stateBlock != null) {
            this.stateBlock.serialize(blockBehaviorSection);
        }

        if (this.lootConfiguration != null) {
            this.lootConfiguration.serialize(blockBehaviorSection);
        }
    }
}
