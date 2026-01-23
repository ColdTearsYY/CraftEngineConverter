package fr.robie.craftengineconverter.listener;

import com.moulberry.axiom.paperapi.AxiomCustomBlocksAPI;
import com.moulberry.axiom.paperapi.block.AxiomCustomBlockBuilder;
import fr.robie.craftengineconverter.common.CraftEngineConverterPlugin;
import fr.robie.craftengineconverter.common.logger.Logger;
import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.bukkit.api.event.CraftEngineReloadEvent;
import net.momirealms.craftengine.core.block.CustomBlock;
import net.momirealms.craftengine.core.item.CustomItem;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CraftEngineLoadListener implements Listener {
    private double lastReloadTime = 0;
    private final CraftEngineConverterPlugin plugin;

    public CraftEngineLoadListener(CraftEngineConverterPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onReload(CraftEngineReloadEvent event){
        Runnable runnable = ()-> {
            AxiomCustomBlocksAPI api = AxiomCustomBlocksAPI.getAPI();
            api.unregisterAll(plugin);
            Logger.info("Registering CraftEngine custom blocks into Axiom...");
            Set<Map.Entry<Key, CustomBlock>> entries = CraftEngineBlocks.loadedBlocks().entrySet();
            Logger.info("Found "+entries.size()+" custom blocks to register.");
            for (var entry : entries){
                Key key = entry.getKey();
                CustomBlock value = entry.getValue();
                BlockData bukkitBlockData = CraftEngineBlocks.getBukkitBlockData(value.defaultState());
                String keyString = key.asString();
                Logger.info("Registering custom block: "+ keyString +" with block data: "+bukkitBlockData.getAsString());
                AxiomCustomBlockBuilder single = api.createSingle(net.kyori.adventure.key.Key.key("craftengine", keyString.replace(":","_")), keyString, bukkitBlockData);
                single.sendServerPickBlockIfPossible(true);
                CustomItem<ItemStack> itemStackCustomItem = CraftEngineItems.byId(key);
                if (itemStackCustomItem != null){
                    single.pickBlockItemStack(itemStackCustomItem.buildItemStack());
                }
                single.automaticRotationAndMirroring(true);
                try {
                    api.register(plugin, single);
                } catch (Exception exception) {
                    Logger.info("Failed to register custom block: "+ keyString);
                    exception.printStackTrace();
                }
                Logger.info("Registered custom block: "+ keyString);
            }
            Logger.info("CraftEngine custom blocks registered into Axiom.");
        };
        if (System.currentTimeMillis() - lastReloadTime < 5000){
            Logger.info("");
            return;
        }
        lastReloadTime = System.currentTimeMillis();
        if (event.isFirstReload()){
            this.plugin.getFoliaCompatibilityManager().runLater(runnable, 20L, TimeUnit.SECONDS);
        } else {
            this.plugin.getFoliaCompatibilityManager().runNextTick(runnable);
        }
    }

}
