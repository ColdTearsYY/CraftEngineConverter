package fr.robie.craftengineconverter.hooks.nexo;

import fr.robie.craftengineconverter.common.CraftEngineConverterPlugin;
import fr.robie.craftengineconverter.common.CraftEnginePlacementTracker;
import fr.robie.craftengineconverter.common.PluginNameMapper;
import fr.robie.craftengineconverter.common.configuration.Configuration;
import fr.robie.craftengineconverter.common.converter.FurnitureConverter;
import fr.robie.craftengineconverter.common.converter.WorldConverter;
import fr.robie.craftengineconverter.common.enums.Plugins;
import fr.robie.craftengineconverter.common.records.ChunkPosition;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class NexoWorldConverter implements WorldConverter {
    private final PluginNameMapper nameMapper;
    private final CraftEnginePlacementTracker placementTracker;

    public NexoWorldConverter(CraftEngineConverterPlugin plugin) {
        this.nameMapper = PluginNameMapper.getInstance();
        this.placementTracker = plugin.getPlacementTracker();
    }

    @Override
    public boolean applyItemDisplayConversion(ChunkPosition chunkPosition, ItemDisplay itemDisplay) {
        if (Configuration.nexoEnableChunkLoadConversion) {
            PersistentDataContainer persistentDataContainer = itemDisplay.getPersistentDataContainer();
            for (NamespacedKey key : persistentDataContainer.getKeys()) {
                if (key.asString().equalsIgnoreCase("nexo:furniture")){
                    String itemID = persistentDataContainer.get(key,  PersistentDataType.STRING);
                    String newName = this.nameMapper.getNewName(Plugins.NEXO, itemID);
                    if (newName == null || !FurnitureConverter.isRegisteredStatic(newName)) {
                        return false;
                    }
                    Location location = itemDisplay.getLocation();
                    itemDisplay.remove();
                    this.placementTracker.placeFurniture(newName, location.add(0, -0.5, 0));
                    return true;
                }
            }
        }
        return false;
    }
}
