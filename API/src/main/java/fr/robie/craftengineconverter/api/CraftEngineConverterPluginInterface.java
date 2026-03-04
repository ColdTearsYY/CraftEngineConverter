package fr.robie.craftengineconverter.api;

import fr.robie.craftengineconverter.api.format.MessageFormatter;
import fr.robie.craftengineconverter.api.manager.FoliaCompatibilityManager;

public interface CraftEngineConverterPluginInterface {
    FoliaCompatibilityManager getFoliaCompatibilityManager();

    MessageFormatter getMessageFormatter();
}
