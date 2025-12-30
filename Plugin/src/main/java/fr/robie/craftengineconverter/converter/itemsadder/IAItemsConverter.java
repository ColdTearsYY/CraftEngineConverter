package fr.robie.craftengineconverter.converter.itemsadder;

import fr.robie.craftengineconverter.common.configuration.Configuration;
import fr.robie.craftengineconverter.common.logger.Logger;
import fr.robie.craftengineconverter.converter.Converter;
import fr.robie.craftengineconverter.converter.ItemConverter;
import fr.robie.craftengineconverter.utils.enums.IADirectionalMode;
import fr.robie.craftengineconverter.utils.enums.Template;
import fr.robie.craftengineconverter.utils.manager.InternalTemplateManager;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IAItemsConverter extends ItemConverter {
    private final ConfigurationSection iaItemSection;

    public IAItemsConverter(@NotNull String itemId, ConfigurationSection craftEngineItemSection, Converter converter, YamlConfiguration fileConfig, ConfigurationSection iaItemSection) {
        super(itemId, craftEngineItemSection, converter, fileConfig);
        this.iaItemSection = iaItemSection;
    }

    @Override
    public void convertMaterial(){
        ConfigurationSection resourceSection = this.iaItemSection.getConfigurationSection("resource");
        Material material = Configuration.defaultMaterial;
        if (isNotNull(resourceSection)){
            try {
                material = Material.valueOf(resourceSection.getString("material").toUpperCase());
            } catch (Exception ignored) {
            }
        }
        this.craftEngineItemUtils.setMaterial(material);
    }

    @Override
    public void convertItemName(){
        String itemName = this.iaItemSection.getString("name", this.iaItemSection.getString("display_name"));
        if (isValidString(itemName)){
            this.craftEngineItemUtils.setItemName(itemName);
        }
    }

    @Override
    public void convertLore(){
        List<String> lore = this.iaItemSection.getStringList("lore");
        if (!lore.isEmpty()){
            this.craftEngineItemUtils.setLore(lore);
        }
    }

    @Override
    public void convertDyedColor(){
        Object color = this.iaItemSection.get("graphics.color");
        if (isNotNull(color)){
            this.craftEngineItemUtils.getDataSection().set("dyed-color", color);
        }
    }

    @Override
    public void convertUnbreakable(){
        ConfigurationSection durabilitySection = this.iaItemSection.getConfigurationSection("durability");
        if (isNotNull(durabilitySection)){
            boolean unbreakable = durabilitySection.getBoolean("unbreakable", false);
            if (unbreakable){
                this.craftEngineItemUtils.getDataSection().set("unbreakable", true);
            }
        }
    }

    @Override
    public void convertItemFlags(){
        List<String> itemFlags = this.iaItemSection.getStringList("item_flags");
        if (!itemFlags.isEmpty()){
            this.craftEngineItemUtils.getDataSection().set("hide-tooltip", itemFlags);
        }
    }

    @Override
    public void convertAttributeModifiers(){
        ConfigurationSection attributesSection = this.iaItemSection.getConfigurationSection("attribute_modifiers");
        if (isNotNull(attributesSection)) {
            List<Map<String, Object>> ceAttributes = new ArrayList<>();
            //TODO: implement attribute modifiers conversion from ItemsAdder to CraftEngine
        }
    }

    @Override
    public void convertEnchantments(){
        ConfigurationSection enchantsSection = this.iaItemSection.getConfigurationSection("enchants");
        if (isNotNull(enchantsSection)){
            for (String enchantmentKey : enchantsSection.getKeys(false)){
                int enchantLevel = enchantsSection.getInt(enchantmentKey, 1);
                ConfigurationSection ceEnchantSection = getOrCreateSection(this.craftEngineItemUtils.getDataSection(), "enchantment");
                ceEnchantSection.set(enchantmentKey, enchantLevel);
            }
        }
        List<String> enchantments = this.iaItemSection.getStringList("enchants");
        if (!enchantments.isEmpty()){
            ConfigurationSection ceEnchantSection = getOrCreateSection(this.craftEngineItemUtils.getDataSection(), "enchantment");
            for (String enchantmentEntry : enchantments){
                String enchantName;
                int enchantLevel = 1;
                int lastIndexOf = enchantmentEntry.lastIndexOf(':');
                if (lastIndexOf != -1){
                    enchantName = enchantmentEntry.substring(0, lastIndexOf);
                    try {
                        enchantLevel = Integer.parseInt(enchantmentEntry.substring(lastIndexOf + 1));
                    } catch (NumberFormatException ignored){
                    }
                } else {
                    enchantName = enchantmentEntry;
                }
                ceEnchantSection.set(enchantName, enchantLevel);
            }
        }
    }

    @Override
    public void convertCustomModelData(){
        ConfigurationSection resourceSection = this.iaItemSection.getConfigurationSection("resource");
        if (isNotNull(resourceSection)){
            int customModelData = resourceSection.getInt("custom_model_data", resourceSection.getInt("model_id", 0));
            if (customModelData != 0){
                this.craftEngineItemUtils.getGeneralSection().set("custom-model-data", customModelData);
            }
        }
    }

    @Override
    public void convertItemModel(){
        String itemModel = this.iaItemSection.getString("item_model");
        if (isValidString(itemModel)){
            this.craftEngineItemUtils.getComponentsSection().set("item_model", itemModel);
        }
    }

    @Override
    public void convertMaxStackSize(){
        int maxStackSize = this.iaItemSection.getInt("max_stack_size", -1);
        if (maxStackSize > 0){
            this.craftEngineItemUtils.getComponentsSection().set("minecraft:max_stack_size", maxStackSize);
        }
    }

    @Override
    public void convertEnchantmentGlintOverride(){
        if (this.iaItemSection.getBoolean("glint", false)){
            this.craftEngineItemUtils.enableEnchantmentGlint();
        }
    }

    @Override
    public void convertFireResistance(){
        // Not supported ?
    }

    @Override
    public void convertMaxDamage(){
        ConfigurationSection durability = this.iaItemSection.getConfigurationSection("durability");
        if (isNotNull(durability)){
            int maxDamage = durability.getInt("max_durability", -1);
            if (maxDamage > 0){
                this.craftEngineItemUtils.getDataSection().set("max-damage", maxDamage);
            }
        }
    }

    @Override
    public void convertGlowDropColor(){
        ConfigurationSection dropSection = this.iaItemSection.getConfigurationSection("drop");
        if (isNotNull(dropSection)){
            ConfigurationSection glowSection = dropSection.getConfigurationSection("glow");
            if (isNotNull(glowSection)){
                boolean glow = glowSection.getBoolean("enabled", false);
                if (glow){
                    String color = glowSection.getString("color");
                    if (isValidString(color)){
                        this.craftEngineItemUtils.getSettingsSection().set("glow-color", color.toLowerCase());
                    }
                }
            }
        }
    }

    @Override
    public void convertDropShowName(){
        ConfigurationSection dropSection = this.iaItemSection.getConfigurationSection("drop");
        if (isNotNull(dropSection)){
            boolean showName = dropSection.getBoolean("show_name", true);
            if (!showName){
                this.craftEngineItemUtils.getSettingsSection().set("drop-display", true);
            }

        }
    }

    @Override
    public void convertHideTooltip(){
        // Not supported ?
    }

    @Override
    public void convertToolTipStyle(){
        String toolTipStyle = this.iaItemSection.getString("tooltip_style");
        if (isValidString(toolTipStyle)){
            this.craftEngineItemUtils.getDataSection().set("tooltip-style", toolTipStyle);
        }
    }

    @Override
    public void convertFood(){
        ConfigurationSection consumableSection = this.iaItemSection.getConfigurationSection("consumable");
        if (isNotNull(consumableSection)){
            int nutrition = consumableSection.getInt("nutrition", -1);
            float saturation = (float) consumableSection.getDouble("saturation", -1.0);
            if (nutrition > 0 || saturation > 0){
                ConfigurationSection foodSection = getOrCreateSection(this.craftEngineItemUtils.getComponentsSection(), "minecraft:food");
                if (nutrition > 0){
                    foodSection.set("nutrition", nutrition);
                }
                if (saturation > 0) {
                    foodSection.set("saturation", saturation);
                }
            }
        }
    }

    @Override
    public void convertJukeboxPlayable() {
        String song = this.iaItemSection.getString("jukebox_disc.song", this.iaItemSection.getString("behaviours.music_disc.song.name"));
        this.craftEngineItemUtils.setJukeboxPlayable(song);
    }

    @Override
    public void convertEquipable() {
        ConfigurationSection equipmentSection = this.iaItemSection.getConfigurationSection("equipment");
        if (isNotNull(equipmentSection)) {
            String assetId = equipmentSection.getString("id");
            assetId = namespaced(assetId);
            ConfigurationSection ceEquipableSection = this.isValidString(assetId) ? getOrCreateSection(this.craftEngineItemUtils.getSettingsSection(),"equippable") : getOrCreateSection(this.craftEngineItemUtils.getDataSection(),"equippable");
            if (isValidString(assetId)) {
                ceEquipableSection.set("asset-id", assetId);
                this.setAssetId(assetId);
            }
            String slot = null;
            if (this.itemId.endsWith("_helmet")){
                slot = "head";
            } else if (this.itemId.endsWith("_chestplate")){
                slot = "chest";
            } else if (this.itemId.endsWith("_leggings")){
                slot = "legs";
            } else if (this.itemId.endsWith("_boots")){
                slot = "feet";
            }
            if (isValidString(slot)){
                ceEquipableSection.set("slot", slot);
            }
        }
        ConfigurationSection specificPropertiesSection = this.iaItemSection.getConfigurationSection("specific_properties");
        if (isNotNull(specificPropertiesSection)) {
            ConfigurationSection armorSection = specificPropertiesSection.getConfigurationSection("armor");
            if (isNotNull(armorSection)) {
                String assetId = armorSection.getString("custom_armor");
                if (isValidString(assetId)){
                    assetId = namespaced(assetId);
                    this.isValidString(assetId);
                    ConfigurationSection ceEquipableSection = getOrCreateSection(this.craftEngineItemUtils.getSettingsSection(),"equippable");
                    ceEquipableSection.set("asset-id", assetId);
                    this.setAssetId(assetId);
                    String slot = armorSection.getString("slot");
                    if (isValidString(slot)){
                        ceEquipableSection.set("slot", slot);
                    }
                }
            }
        }
    }

    @Override
    public void convertItemTexture(){
        ConfigurationSection resourceSection = this.iaItemSection.getConfigurationSection("resource");
        if (isNotNull(resourceSection)){
            boolean generate = resourceSection.getBoolean("generate", false);
            if (generate){
                IADirectionalMode directionalMode = IADirectionalMode.NONE;
                try {
                    directionalMode = IADirectionalMode.valueOf(resourceSection.getString("specific_properties.block.placed_model.directional_mode","NONE").toUpperCase());
                } catch (Exception ignored){
                }
                switch (directionalMode){
                    case NONE -> {
                        String texturePath = getTexturePath(resourceSection);
                        if (isValidString(texturePath)){
                            texturePath = namespaced(texturePath);
                            Map<String, Object> parsedTemplate = InternalTemplateManager.parseTemplate(Template.MODEL_ITEM_GENERATED, "%model_path%", texturePath, "%texture_path%", texturePath);
                            this.craftEngineItemUtils.getGeneralSection().createSection("model",parsedTemplate);
                        }
                    }
                    case ALL,LOG -> {
                        List<String> faceTextures = resourceSection.getStringList("textures");
                        Map<BlockFace, String> faceTextureMap = new HashMap<>();
                        if (faceTextures.size() != 6){
                            Logger.debug("[IAItemsConverter] Directional mode ALL requires 6 textures for item " + this.itemId);
                            return;
                        }
                        for (String faceTexture : faceTextures){
                            String cleanedTexture = cleanPath(faceTexture);
                            if (isNotNull(cleanedTexture)){
                                if (cleanedTexture.endsWith("_down")){
                                    faceTextureMap.put(BlockFace.DOWN, namespaced(cleanedTexture));
                                } else if (cleanedTexture.endsWith("_up")){
                                    faceTextureMap.put(BlockFace.UP, namespaced(cleanedTexture));
                                } else if (cleanedTexture.endsWith("_north")){
                                    faceTextureMap.put(BlockFace.NORTH, namespaced(cleanedTexture));
                                } else if (cleanedTexture.endsWith("_south")){
                                    faceTextureMap.put(BlockFace.SOUTH, namespaced(cleanedTexture));
                                } else if (cleanedTexture.endsWith("_west")){
                                    faceTextureMap.put(BlockFace.WEST, namespaced(cleanedTexture));
                                } else if (cleanedTexture.endsWith("_east")) {
                                    faceTextureMap.put(BlockFace.EAST, namespaced(cleanedTexture));
                                } else {
                                    Logger.debug("[IAItemsConverter] Invalid texture name " + faceTexture + " for directional mode ALL for item " + this.itemId);
                                    return;
                                }
                            }
                        }
                        if (faceTextureMap.size() != 6){
                            Logger.debug("[IAItemsConverter] Directional mode ALL requires 6 valid textures for item " + this.itemId);
                            return;
                        }
                        Map<String, Object> parsedTemplate = InternalTemplateManager.parseTemplate(Template.MODEL_CUBE, "%model_path%", faceTextureMap.get(BlockFace.NORTH),
                                "%texture_down_path%", faceTextureMap.get(BlockFace.DOWN),
                                "%texture_up_path%", faceTextureMap.get(BlockFace.UP),
                                "%texture_north_path%", faceTextureMap.get(BlockFace.NORTH),
                                "%texture_south_path%", faceTextureMap.get(BlockFace.SOUTH),
                                "%texture_west_path%", faceTextureMap.get(BlockFace.WEST),
                                "%texture_east_path%", faceTextureMap.get(BlockFace.EAST)
                        );
                        this.setSavedModelTemplates(parsedTemplate);
                        ConfigurationSection stateSection = this.craftEngineItemUtils.getStateSection();
                        stateSection.set("auto-state", "note_block");
                        ConfigurationSection properties = getOrCreateSection(stateSection, "properties");
                        ConfigurationSection axis = getOrCreateSection(properties, "axis");
                        axis.set("type", "axis");
                        axis.set("default", "y");
                        stateSection.set("appearances", InternalTemplateManager.parseTemplate(Template.BLOCK_STATE_LOG_APPEARANCE, "%model%", parsedTemplate));
                        ConfigurationSection variants = getOrCreateSection(stateSection, "variants");
                        ConfigurationSection axisX = getOrCreateSection(variants, "axis=x");
                        axisX.set("appearance", "axisX");
                        ConfigurationSection axisY = getOrCreateSection(variants, "axis=y");
                        axisY.set("appearance", "axisY");
                        ConfigurationSection axisZ = getOrCreateSection(variants, "axis=z");
                        axisZ.set("appearance", "axisZ");
                    }
                    case FURNACE -> {
                        List<String> faceTextures = resourceSection.getStringList("textures");
                        Map<BlockFace, String> faceTextureMap = new HashMap<>();
                        if (faceTextures.size() != 6){
                            Logger.debug("[IAItemsConverter] Directional mode ALL requires 6 textures for item " + this.itemId);
                            return;
                        }
                        for (String faceTexture : faceTextures){
                            String cleanedTexture = cleanPath(faceTexture);
                            if (isNotNull(cleanedTexture)){
                                if (cleanedTexture.endsWith("_down")){
                                    faceTextureMap.put(BlockFace.DOWN, namespaced(cleanedTexture));
                                } else if (cleanedTexture.endsWith("_up")){
                                    faceTextureMap.put(BlockFace.UP, namespaced(cleanedTexture));
                                } else if (cleanedTexture.endsWith("_north")){
                                    faceTextureMap.put(BlockFace.NORTH, namespaced(cleanedTexture));
                                } else if (cleanedTexture.endsWith("_south")){
                                    faceTextureMap.put(BlockFace.SOUTH, namespaced(cleanedTexture));
                                } else if (cleanedTexture.endsWith("_west")){
                                    faceTextureMap.put(BlockFace.WEST, namespaced(cleanedTexture));
                                } else if (cleanedTexture.endsWith("_east")) {
                                    faceTextureMap.put(BlockFace.EAST, namespaced(cleanedTexture));
                                } else {
                                    Logger.debug("[IAItemsConverter] Invalid texture name " + faceTexture + " for directional mode ALL for item " + this.itemId);
                                    return;
                                }
                            }
                        }
                        if (faceTextureMap.size() != 6){
                            Logger.debug("[IAItemsConverter] Directional mode Furnace requires 6 valid textures for item " + this.itemId);
                            return;
                        }
                        Map<String, Object> parsedTemplate = InternalTemplateManager.parseTemplate(Template.MODEL_CUBE, "%model_path%", faceTextureMap.get(BlockFace.NORTH),
                                "%texture_down_path%", faceTextureMap.get(BlockFace.DOWN),
                                "%texture_up_path%", faceTextureMap.get(BlockFace.UP),
                                "%texture_north_path%", faceTextureMap.get(BlockFace.NORTH),
                                "%texture_south_path%", faceTextureMap.get(BlockFace.SOUTH),
                                "%texture_west_path%", faceTextureMap.get(BlockFace.WEST),
                                "%texture_east_path%", faceTextureMap.get(BlockFace.EAST)
                        );
                        this.setSavedModelTemplates(parsedTemplate);
                        ConfigurationSection stateSection = this.craftEngineItemUtils.getStateSection();
                        stateSection.set("auto-state", "note_block");
                        ConfigurationSection properties = getOrCreateSection(stateSection, "properties");
                        ConfigurationSection facing = getOrCreateSection(properties, "facing");
                        facing.set("type", "4-direction");
                        facing.set("default", "north");
                        stateSection.set("appearances", InternalTemplateManager.parseTemplate(Template.BLOCK_STATE_4_DIRECTIONS_APPEARANCE, "%model%", parsedTemplate));
                        ConfigurationSection variants = getOrCreateSection(stateSection, "variants");
                        ConfigurationSection facingNorth = getOrCreateSection(variants, "facing=north");
                        facingNorth.set("appearance", "north");
                        ConfigurationSection facingSouth = getOrCreateSection(variants, "facing=south");
                        facingSouth.set("appearance", "south");
                        ConfigurationSection facingWest = getOrCreateSection(variants, "facing=west");
                        facingWest.set("appearance", "west");
                        ConfigurationSection facingEast = getOrCreateSection(variants, "facing=east");
                        facingEast.set("appearance", "east");
                    }
                    default -> {
                        Logger.debug("[IAItemsConverter] Directional mode " + directionalMode + " is not supported for item " + this.itemId);
                    }
                }
            } else {
                String modelPath = resourceSection.getString("model_path");
                if (isValidString(modelPath)){
                    modelPath = cleanPath(modelPath);
                    if (isNull(modelPath)){
                        Logger.debug("[IAItemsConverter] Invalid model path for item " + this.itemId);
                        return;
                    }

                    String namespacedModelPath = namespaced(modelPath);
                    Map<String, Object> parsedTemplate = InternalTemplateManager.parseTemplate(Template.MODEL_ITEM_DEFAULT, "%model_path%", namespacedModelPath);
                    this.setSavedModelTemplates(parsedTemplate);
                    this.craftEngineItemUtils.getGeneralSection().createSection("model",parsedTemplate);
                }
            }
        }
    }
}