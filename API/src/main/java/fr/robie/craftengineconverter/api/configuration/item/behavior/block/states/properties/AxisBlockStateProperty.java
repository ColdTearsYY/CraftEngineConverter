package fr.robie.craftengineconverter.api.configuration.item.behavior.block.states.properties;

import fr.robie.craftengineconverter.api.configuration.item.behavior.block.states.BlockStateProperty;
import net.momirealms.craftengine.core.util.Direction;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class AxisBlockStateProperty implements BlockStateProperty<Direction.Axis> {
    private final String name;
    private final Direction.Axis value;

    public AxisBlockStateProperty(@NotNull String name, @NotNull Direction.Axis value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public Direction.@NonNull Axis getValue() {
        return this.value;
    }

    @Override
    public void serialize(@NotNull ConfigurationSection propertiesSection) {
        ConfigurationSection section = propertiesSection.createSection(this.name);
        section.set("type", "axis");
        section.set("default", this.value.name().toLowerCase());
    }
}
