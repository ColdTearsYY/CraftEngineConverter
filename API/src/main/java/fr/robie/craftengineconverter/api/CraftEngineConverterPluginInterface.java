package fr.robie.craftengineconverter.api;

import fr.robie.craftengineconverter.api.format.MessageFormatter;
import fr.robie.craftengineconverter.api.manager.FoliaCompatibilityManager;
import fr.robie.craftengineconverter.api.tag.ITagResolver;

public interface CraftEngineConverterPluginInterface {
    FoliaCompatibilityManager getFoliaCompatibilityManager();

    MessageFormatter getMessageFormatter();

    ITagResolver getTagResolver();
}
