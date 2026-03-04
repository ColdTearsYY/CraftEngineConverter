package fr.robie.craftengineconverter.api.configuration.item.behavior.block.states;

import fr.robie.craftengineconverter.api.configuration.utils.SectionProvider;
import fr.robie.craftengineconverter.api.enums.CraftEngineBlockState;
import fr.robie.craftengineconverter.api.enums.Plugins;
import fr.robie.craftengineconverter.api.utils.BlockStateResult;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BlockAppearance implements SectionProvider {
    private final Plugins requiredPlugin;
    private final CraftEngineBlockState autoState;
    private final String visualState;

    private final Map<String, Object> model;

    private final String itemId;

    public BlockAppearance(@NotNull Plugins requiredPlugin,@NotNull CraftEngineBlockState autoState, @NotNull Map<String, Object> model,@NotNull String itemId) {
        this.requiredPlugin = requiredPlugin;
        this.autoState = autoState;
        this.itemId = itemId;
        this.visualState = null;
        this.model = model;
    }

    public BlockAppearance(@NotNull String visualState, @NotNull Map<String, Object> model) {
        this.itemId = null;
        this.requiredPlugin = null;
        this.autoState = null;
        this.visualState = visualState;
        this.model = model;
    }

    public void serialize(@NotNull ConfigurationSection section) {
        if (this.autoState != null) {
            BlockStateResult available = autoState.getAvailableAndIncrementNotLast(this.requiredPlugin);
            if (available != null) {
                if (available.isLast()) {
                    section.set("state", available.getBlockState().getLastBlockState());
                    ConfigurationSection entityRendererSection = getOrCreateSection(section, "entity-renderer");
                    entityRendererSection.set("item", this.itemId);
                } else {
                    section.set("auto-state", available.getName());
                }
            }
        } else if (this.visualState != null) {
            section.set("state", this.visualState);
        }
        section.set("model", this.model);
    }
}
