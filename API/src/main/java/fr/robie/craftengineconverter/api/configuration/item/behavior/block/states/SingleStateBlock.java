package fr.robie.craftengineconverter.api.configuration.item.behavior.block.states;

import fr.robie.craftengineconverter.api.enums.CraftEngineBlockState;
import fr.robie.craftengineconverter.api.enums.Plugins;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SingleStateBlock implements StateBlock {
    private final BlockAppearance blockAppearance;

    public SingleStateBlock(Plugins requiredPlugin, CraftEngineBlockState autoState, @NotNull Map<String, Object> model, String itemId) {
        this.blockAppearance = new BlockAppearance(requiredPlugin, autoState, model, itemId);
    }

    public SingleStateBlock(String visualState, @NotNull Map<String, Object> model) {
        this.blockAppearance = new BlockAppearance(visualState, model);
    }

    @Override
    public void serialize(@NotNull ConfigurationSection blockBehaviorSection) {
        ConfigurationSection state = getOrCreateSection(blockBehaviorSection, "state");
        this.blockAppearance.serialize(state);
    }
}
