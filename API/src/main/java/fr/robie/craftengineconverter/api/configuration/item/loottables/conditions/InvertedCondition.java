package fr.robie.craftengineconverter.api.configuration.item.loottables.conditions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class InvertedCondition extends AbstractLootCondition {
    private final LootCondition term;

    public InvertedCondition(@NotNull LootCondition term) {
        super("inverted");
        this.term = term;
    }

    @Override
    public void serialize(@NotNull ConfigurationSection section) {
        super.serialize(section);
        YamlConfiguration temp = new YamlConfiguration();
        this.term.serialize(temp);
        section.set("term", temp.getValues(true));
    }
}
