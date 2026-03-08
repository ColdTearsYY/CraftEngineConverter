package fr.robie.craftengineconverter.api.configuration.item.loottables.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class DistanceCondition extends AbstractLootCondition {
    private final double min;
    private final double max;

    public DistanceCondition(double min, double max) {
        super("distance");
        this.min = min;
        this.max = max;
    }

    @Override
    public void serialize(@NotNull ConfigurationSection section) {
        super.serialize(section);
        section.set("min", this.min);
        section.set("max", this.max);
    }
}
