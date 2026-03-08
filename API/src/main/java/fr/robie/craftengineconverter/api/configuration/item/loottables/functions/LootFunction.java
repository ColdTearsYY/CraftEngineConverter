package fr.robie.craftengineconverter.api.configuration.item.loottables.functions;

import fr.robie.craftengineconverter.api.configuration.item.loottables.LootConfiguration;
import fr.robie.craftengineconverter.api.configuration.item.loottables.conditions.LootCondition;

import java.util.List;

public interface LootFunction extends LootConfiguration {
    void addCondition(LootCondition condition);
    List<LootCondition> getConditions();
}
