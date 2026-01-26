package fr.robie.craftengineconverter.common;

import fr.robie.craftengineconverter.common.enums.Plugins;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockStatesMapper {
    private static final BlockStatesMapper INSTANCE = new BlockStatesMapper();
    private final Map<String, Map<BlockData, String>> mappings = new ConcurrentHashMap<>();

    private BlockStatesMapper() {}

    @Nullable
    public String getCeEquivalent(Plugins plugin, BlockData oldBlockState) {
        var pluginMappings = this.mappings.get(plugin.name());
        return pluginMappings != null ? pluginMappings.get(oldBlockState) : null;
    }

    public void storeMapping(Plugins plugin, BlockData oldBlockState, String newCeName) {
        this.mappings.computeIfAbsent(plugin.name(), k -> new ConcurrentHashMap<>())
                .put(oldBlockState, newCeName);
    }

    public static BlockStatesMapper getInstance() {
        return INSTANCE;
    }

    public void clearMappingsForPlugin(Plugins plugins) {
        this.mappings.remove(plugins.name());
    }
}
