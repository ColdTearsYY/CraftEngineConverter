package fr.robie.craftengineconverter.api.configuration.item.loottables.entries;

import fr.robie.craftengineconverter.api.configuration.item.loottables.LootConfiguration;
import fr.robie.craftengineconverter.api.configuration.item.loottables.conditions.LootCondition;
import fr.robie.craftengineconverter.api.configuration.item.loottables.functions.LootFunction;

import java.util.List;

public interface LootEntry extends LootConfiguration {
    void addCondition(LootCondition condition);
    List<LootCondition> getConditions();
    void addFunction(LootFunction function);
    List<LootFunction> getFunctions();
}
