package fr.robie.craftengineconverter.api.configuration.item.loottables.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OnCooldownCondition extends AbstractLootCondition {
    private final String id;

    public OnCooldownCondition(@NotNull String id) {
        super("on_cooldown");
        this.id = Objects.requireNonNull(id, "id cannot be null");
    }

    @Override
    public void serialize(@NotNull ConfigurationSection section) {
        super.serialize(section);
        section.set("id", this.id);
    }
}
