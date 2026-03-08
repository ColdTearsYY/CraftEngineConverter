package fr.robie.craftengineconverter.api.configuration.item.loottables.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class RandomCondition extends AbstractLootCondition {
    private final double value;

    public RandomCondition(double value) {
        super("random");
        this.value = value;
    }

    @Override
    public void serialize(@NotNull ConfigurationSection section) {
        super.serialize(section);
        section.set("value", this.value);
    }
}
