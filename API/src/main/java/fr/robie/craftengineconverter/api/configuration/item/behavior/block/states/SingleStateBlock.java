package fr.robie.craftengineconverter.api.configuration.item.behavior.block.states;

import fr.robie.craftengineconverter.api.enums.CraftEngineBlockState;
import fr.robie.craftengineconverter.api.enums.Plugins;
import fr.robie.craftengineconverter.api.utils.BlockStateResult;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SingleStateBlock implements StateBlock {
    private final Plugins requiredPlugin;
    private final CraftEngineBlockState autoState;
    private final String visualState;

    private final Map<String, Object> model;

    private final String itemId;

    public SingleStateBlock(Plugins requiredPlugin, CraftEngineBlockState autoState, @NotNull Map<String, Object> model, String itemId) {
        this.requiredPlugin = requiredPlugin;
        this.autoState = autoState;
        this.itemId = itemId;
        this.visualState = null;
        this.model = model;
    }

    public SingleStateBlock(String visualState, @NotNull Map<String, Object> model) {
        this.itemId = null;
        this.requiredPlugin = null;
        this.autoState = null;
        this.visualState = visualState;
        this.model = model;
    }

    @Override
    public void serialize(@NotNull ConfigurationSection blockBehaviorSection) {
        ConfigurationSection state = getOrCreateSection(blockBehaviorSection, "state");
        if (this.autoState != null) {
            BlockStateResult available = autoState.getAvailableAndIncrementNotLast(this.requiredPlugin);
            if (available != null) {
                if (available.isLast()) {
                    state.set("state", available.getBlockState().getLastBlockState());
                    ConfigurationSection entityRendererSection = getOrCreateSection(state, "entity-renderer");
                    entityRendererSection.set("item", this.itemId);
                } else {
                    state.set("auto-state", available.getName());
                }
            }
        } else if (this.visualState != null) {
            state.set("state", this.visualState);
        }
        state.set("model", this.model);

    }
}
