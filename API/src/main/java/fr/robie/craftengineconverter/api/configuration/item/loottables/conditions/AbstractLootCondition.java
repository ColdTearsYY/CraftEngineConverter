package fr.robie.craftengineconverter.api.configuration.item.loottables.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLootCondition implements LootCondition {
    private final String type;

    protected AbstractLootCondition(@NotNull String type) {
        this.type = type;
    }

    @Override
    public void serialize(@NotNull ConfigurationSection section) {
        section.set("type", this.type);
    }
}
