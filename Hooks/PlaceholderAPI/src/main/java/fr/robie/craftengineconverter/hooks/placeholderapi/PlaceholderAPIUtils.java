package fr.robie.craftengineconverter.hooks.placeholderapi;

import fr.robie.craftengineconverter.api.configuration.Configuration;
import fr.robie.craftengineconverter.common.CraftEngineConverterPlugin;
import fr.robie.craftengineconverter.hooks.placeholderapi.itemsadder.ImgExpansion;

public class PlaceholderAPIUtils {

    public static void registerExpansions(CraftEngineConverterPlugin plugin) {
        if (Configuration.itemsAdderImgPlaceholderAPISupport){
            new ImgExpansion(plugin).register();
        }
    }
}
