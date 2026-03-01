package fr.robie.craftengineconverter.api.configurations.item.data;

import fr.robie.craftengineconverter.api.configurations.ItemConfigurationSerializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentConfiguration implements ItemConfigurationSerializable {
    private final Map<String, Integer> enchantments = new HashMap<>();
    private boolean merge = false;

    public boolean hasEnchantments() {
        return !this.enchantments.isEmpty();
    }

    public void addEnchantment(@NotNull String enchantmentName, int level) {
        this.enchantments.put(enchantmentName, level);
    }

    public void setMerge(boolean merge) {
        this.merge = merge;
    }

    public boolean isMerge() {
        return merge;
    }

    @Override
    public void serialize(@NotNull YamlConfiguration yamlConfiguration, @NotNull String path, @NotNull ConfigurationSection itemSection, @NotNull String itemId) {
        ConfigurationSection data = getOrCreateSection(itemSection, "data");
        ConfigurationSection enchantment = getOrCreateSection(data, "enchantment");
        if (this.merge) {
            enchantment.set("merge", true);
            ConfigurationSection enchantmentsSection = getOrCreateSection(enchantment, "enchantments");
            for (Map.Entry<String, Integer> entry : this.enchantments.entrySet()) {
                enchantmentsSection.set(entry.getKey(), entry.getValue());
            }
        } else {
            for (Map.Entry<String, Integer> entry : this.enchantments.entrySet()) {
                enchantment.set(entry.getKey(), entry.getValue());
            }
        }
    }
}
