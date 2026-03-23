package fr.robie.craftengineconverter.api.configuration.item.loottables.functions;

import fr.robie.craftengineconverter.api.configuration.conditions.Condition;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractLootFunction implements LootFunction {
    private final String type;
    private final List<Condition> conditions = new ArrayList<>();

    protected AbstractLootFunction(@NotNull String type) {
        this.type = type;
    }

    @Override
    public void addCondition(Condition condition) {
        this.conditions.add(Objects.requireNonNull(condition, "condition cannot be null"));
    }

    @Override
    public List<Condition> getConditions() {
        return this.conditions;
    }

    @Override
    public void serialize(@NotNull ConfigurationSection section) {
        section.set("type", this.type);
        if (!this.conditions.isEmpty()) {
            List<Map<String, Object>> serializedConditions = new ArrayList<>();
            for (Condition condition : this.conditions) {
                YamlConfiguration temp = new YamlConfiguration();
                condition.serialize(temp);
                serializedConditions.add(temp.getValues(true));
            }
            section.set("conditions", serializedConditions);
        }
    }
}
