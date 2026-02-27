package fr.robie.craftengineconverter.converter;

import fr.robie.craftengineconverter.common.utils.ObjectUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class CraftEngineItemUtils extends ObjectUtils {
    private final ConfigurationSection craftEngineItemSection;

    public CraftEngineItemUtils(ConfigurationSection craftEngineItemSection){
        this.craftEngineItemSection = craftEngineItemSection;
    }

    public ConfigurationSection getSettingsSection() {
        return getOrCreateSection(craftEngineItemSection, "settings");
    }

    public ConfigurationSection getGeneralSection() {
        return this.craftEngineItemSection;
    }

    public ConfigurationSection getBehaviorSection() {
        return getOrCreateSection(craftEngineItemSection, "behavior");
    }

    public void setModel(Map<String, Object> model) {
        getGeneralSection().set("model", model);
    }
}
