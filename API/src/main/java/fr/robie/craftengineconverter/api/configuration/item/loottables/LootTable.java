package fr.robie.craftengineconverter.api.configuration.item.loottables;

import fr.robie.craftengineconverter.api.configuration.item.loottables.functions.LootFunction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LootTable implements LootConfiguration {
    private final List<LootFunction> functions = new ArrayList<>();
    private final List<LootPool> pools = new ArrayList<>();

    public void addFunction(@NotNull LootFunction function) {
        this.functions.add(Objects.requireNonNull(function, "function cannot be null"));
    }

    public void addPool(@NotNull LootPool pool) {
        this.pools.add(Objects.requireNonNull(pool, "pool cannot be null"));
    }

    @Override
    public void serialize(@NotNull ConfigurationSection section) {
        if (this.functions.isEmpty() && this.pools.isEmpty())
            return;

        ConfigurationSection lootSection = section.createSection("loot");

        if (!this.functions.isEmpty()) {
            List<Map<String, Object>> serializedFunctions = new ArrayList<>();
            for (LootFunction function : this.functions) {
                YamlConfiguration temp = new YamlConfiguration();
                function.serialize(temp);
                serializedFunctions.add(temp.getValues(true));
            }
            lootSection.set("functions", serializedFunctions);
        }

        if (!this.pools.isEmpty()) {
            List<Map<String, Object>> serializedPools = new ArrayList<>();
            for (LootPool pool : this.pools) {
                YamlConfiguration temp = new YamlConfiguration();
                pool.serialize(temp);
                serializedPools.add(temp.getValues(true));
            }
            lootSection.set("pools", serializedPools);
        }
    }
}
