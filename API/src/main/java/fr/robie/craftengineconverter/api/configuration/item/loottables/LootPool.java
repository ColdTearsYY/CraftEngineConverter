package fr.robie.craftengineconverter.api.configuration.item.loottables;

import fr.robie.craftengineconverter.api.configuration.conditions.Condition;
import fr.robie.craftengineconverter.api.configuration.item.loottables.entries.LootEntry;
import fr.robie.craftengineconverter.api.configuration.item.loottables.functions.LootFunction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LootPool implements LootConfiguration {
    private Object rolls = 1;
    private final List<Condition> conditions = new ArrayList<>();
    private final List<LootEntry> entries = new ArrayList<>();
    private final List<LootFunction> functions = new ArrayList<>();

    public void setRolls(@NotNull Object rolls) {
        this.rolls = Objects.requireNonNull(rolls, "rolls cannot be null");
    }

    public void addCondition(@NotNull Condition condition) {
        this.conditions.add(Objects.requireNonNull(condition, "condition cannot be null"));
    }

    public void addEntry(@NotNull LootEntry entry) {
        this.entries.add(Objects.requireNonNull(entry, "entry cannot be null"));
    }

    public void addFunction(@NotNull LootFunction function) {
        this.functions.add(Objects.requireNonNull(function, "function cannot be null"));
    }

    @Override
    public void serialize(@NotNull ConfigurationSection section) {
        section.set("rolls", this.rolls);

        if (!this.conditions.isEmpty()) {
            List<Map<String, Object>> serializedConditions = new ArrayList<>();
            for (Condition condition : this.conditions) {
                YamlConfiguration temp = new YamlConfiguration();
                condition.serialize(temp);
                serializedConditions.add(temp.getValues(true));
            }
            section.set("conditions", serializedConditions);
        }

        if (!this.entries.isEmpty()) {
            List<Map<String, Object>> serializedEntries = new ArrayList<>();
            for (LootEntry entry : this.entries) {
                YamlConfiguration temp = new YamlConfiguration();
                entry.serialize(temp);
                serializedEntries.add(temp.getValues(true));
            }
            section.set("entries", serializedEntries);
        }

        if (!this.functions.isEmpty()) {
            List<Map<String, Object>> serializedFunctions = new ArrayList<>();
            for (LootFunction function : this.functions) {
                YamlConfiguration temp = new YamlConfiguration();
                function.serialize(temp);
                serializedFunctions.add(temp.getValues(true));
            }
            section.set("functions", serializedFunctions);
        }
    }
}
