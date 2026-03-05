package fr.robie.craftengineconverter.converter.itemsadder;

import fr.robie.craftengineconverter.api.configuration.Configuration;
import fr.robie.craftengineconverter.api.configuration.item.LoreConfiguration;
import fr.robie.craftengineconverter.api.configuration.item.behavior.block.BlockConfiguration;
import fr.robie.craftengineconverter.api.configuration.item.behavior.block.BlockSettings;
import fr.robie.craftengineconverter.api.configuration.item.behavior.block.behaviors.OnLiquidBlockBehavior;
import fr.robie.craftengineconverter.api.configuration.item.behavior.block.states.BlockAppearance;
import fr.robie.craftengineconverter.api.configuration.item.behavior.block.states.BlockVariant;
import fr.robie.craftengineconverter.api.configuration.item.behavior.block.states.MultiStateBlock;
import fr.robie.craftengineconverter.api.configuration.item.behavior.block.states.SingleStateBlock;
import fr.robie.craftengineconverter.api.configuration.item.behavior.block.states.properties.AxisBlockStateProperty;
import fr.robie.craftengineconverter.api.configuration.item.behavior.block.states.properties.HorizontalDirectionBlockStateProperty;
import fr.robie.craftengineconverter.api.configuration.item.behavior.furniture.FurnitureConfiguration;
import fr.robie.craftengineconverter.api.configuration.item.behavior.furniture.ItemElement;
import fr.robie.craftengineconverter.api.configuration.item.behavior.furniture.Placement;
import fr.robie.craftengineconverter.api.configuration.item.behavior.furniture.element.ArmorStandElement;
import fr.robie.craftengineconverter.api.configuration.item.behavior.furniture.element.ItemDisplayElement;
import fr.robie.craftengineconverter.api.configuration.item.behavior.furniture.hitbox.Hitbox;
import fr.robie.craftengineconverter.api.configuration.item.behavior.furniture.hitbox.ShulkerHitbox;
import fr.robie.craftengineconverter.api.configuration.item.data.*;
import fr.robie.craftengineconverter.api.configuration.item.models.condition.ConditionModelConfiguration;
import fr.robie.craftengineconverter.api.configuration.item.models.model.GenerationConfiguration;
import fr.robie.craftengineconverter.api.configuration.item.models.model.SimpleModelConfiguration;
import fr.robie.craftengineconverter.api.configuration.item.models.range_dispatch.UseDurationRangeDispatchConfiguration;
import fr.robie.craftengineconverter.api.configuration.item.models.select.ChargeTypeSelectConfiguration;
import fr.robie.craftengineconverter.api.configuration.item.models.select.DisplayContentSelectConfiguration;
import fr.robie.craftengineconverter.api.configuration.item.settings.GlowDropColorConfiguration;
import fr.robie.craftengineconverter.api.configuration.utils.FurniturePlacement;
import fr.robie.craftengineconverter.api.configuration.utils.ItemDisplayType;
import fr.robie.craftengineconverter.api.enums.ComponentFlag;
import fr.robie.craftengineconverter.api.enums.CraftEngineBlockState;
import fr.robie.craftengineconverter.api.enums.Plugins;
import fr.robie.craftengineconverter.api.format.Message;
import fr.robie.craftengineconverter.api.logger.Logger;
import fr.robie.craftengineconverter.api.utils.FloatsUtils;
import fr.robie.craftengineconverter.common.enums.BukkitFlagToComponentFlag;
import fr.robie.craftengineconverter.common.utils.enums.BlockParent;
import fr.robie.craftengineconverter.common.utils.enums.Template;
import fr.robie.craftengineconverter.common.utils.enums.ia.IADirectionalMode;
import fr.robie.craftengineconverter.common.utils.enums.ia.IAEntityTypes;
import fr.robie.craftengineconverter.common.utils.enums.ia.IAModelsKeys;
import fr.robie.craftengineconverter.common.utils.enums.ia.IAPlacedModelTypes;
import fr.robie.craftengineconverter.converter.Converter;
import fr.robie.craftengineconverter.converter.ItemConverter;
import fr.robie.craftengineconverter.utils.manager.InternalTemplateManager;
import net.momirealms.craftengine.core.entity.EquipmentSlot;
import net.momirealms.craftengine.core.entity.display.Billboard;
import net.momirealms.craftengine.core.util.Direction;
import net.momirealms.craftengine.core.util.HorizontalDirection;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class IAItemsConverter extends ItemConverter {
    private final ConfigurationSection iaItemSection;
    private final String namespace;

    public IAItemsConverter(@NotNull String itemId, ConfigurationSection craftEngineItemSection, Converter converter, YamlConfiguration fileConfig, ConfigurationSection iaItemSection, String namespace) {
        super(itemId, craftEngineItemSection, converter, fileConfig);
        this.iaItemSection = iaItemSection;
        this.namespace = namespace;
    }

    @Override
    public void convertMaterial(){
        ConfigurationSection resourceSection = this.iaItemSection.getConfigurationSection("resource");
        if (isNotNull(resourceSection)){
            try {
                this.craftEngineItemsConfiguration.setMaterial(Material.valueOf(resourceSection.getString("material","").toUpperCase()));
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void convertItemName(){
        String itemName = this.iaItemSection.getString("name", this.iaItemSection.getString("display_name"));
        if (isValidString(itemName)){
            if (itemName.startsWith("display-name-")) {
                itemName = "<l10n:"+ itemName + ">";
            }
            this.craftEngineItemsConfiguration.addItemConfiguration(new ItemNameConfiguration(itemName, Configuration.disableDefaultItalic));
        }
    }

    @Override
    public void convertLore(){
        List<String> lore = this.iaItemSection.getStringList("lore");
        if (!lore.isEmpty()){
            for (int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                if (line.startsWith("lore-")) {
                    lore.set(i, "<l10n:" + line + ">");
                }
            }
            this.craftEngineItemsConfiguration.addItemConfiguration(new LoreConfiguration(lore, Configuration.disableDefaultItalic));
        }
    }

    @Override
    public void convertDyedColor(){
        Object color = this.iaItemSection.get("graphics.color");
        if (isNotNull(color)) {
            try {
                this.craftEngineItemsConfiguration.addItemConfiguration(DyedColorConfiguration.parse(color));
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void convertUnbreakable(){
        ConfigurationSection durabilitySection = this.iaItemSection.getConfigurationSection("durability");
        if (isNotNull(durabilitySection)){
            boolean unbreakable = durabilitySection.getBoolean("unbreakable", false);
            if (unbreakable){
                this.craftEngineItemsConfiguration.addItemConfiguration(new UnbreakableConfiguration(true));
            }
        }
    }

    @Override
    public void convertItemFlags(){
        List<String> itemFlags = this.iaItemSection.getStringList("item_flags");
        if (!itemFlags.isEmpty()){
            List<ComponentFlag> convertedFlags = new ArrayList<>();
            for (String flag : itemFlags){
                try {
                    ItemFlag bukkitFlag = ItemFlag.valueOf(flag.toUpperCase());
                    ComponentFlag componentFlag = BukkitFlagToComponentFlag.fromBukkitItemFlag(bukkitFlag);
                    if (componentFlag != null){
                        convertedFlags.add(componentFlag);
                    }
                } catch (Exception ignored){
                }
            }
            this.craftEngineItemsConfiguration.addItemConfiguration(new HideTooltipConfiguration(convertedFlags));
        }
    }

    @Override
    public void convertAttributeModifiers(){
        ConfigurationSection attributesSection = this.iaItemSection.getConfigurationSection("attribute_modifiers");
        if (isNotNull(attributesSection)) {
            List<fr.robie.craftengineconverter.api.configuration.utils.AttributeModifier> attributeModifiers = new ArrayList<>();

            for (String equipmentSlot : attributesSection.getKeys(false)) {
                ConfigurationSection slotSection = attributesSection.getConfigurationSection(equipmentSlot);
                if (isNull(slotSection)) continue;
                net.momirealms.craftengine.core.attribute.AttributeModifier.Slot slot;
                try {
                    slot = net.momirealms.craftengine.core.attribute.AttributeModifier.Slot.valueOf(equipmentSlot.toUpperCase());
                } catch (Exception e) {
                    Logger.debug("[IAItemsConverter] Invalid equipment slot " + equipmentSlot + " for attribute modifiers for item " + this.itemId);
                    continue;
                }
                for (String attributeKey : slotSection.getKeys(false)) {
                    try {
                        Attribute attribute = Registry.ATTRIBUTE.getOrThrow(NamespacedKey.fromString(attributeKey));
                        int amount = slotSection.getInt(attributeKey);
                        attributeModifiers.add(new fr.robie.craftengineconverter.api.configuration.utils.AttributeModifier(attribute.name(), slot, null, amount, net.momirealms.craftengine.core.attribute.AttributeModifier.Operation.ADD_VALUE, null));
                    } catch (Exception e) {
                        Logger.debug("[IAItemsConverter] Invalid attribute " + attributeKey + " for attribute modifiers for item " + this.itemId);
                    }
                }
            }

            if (!attributeModifiers.isEmpty())
                this.craftEngineItemsConfiguration.addItemConfiguration(new AttributeModifiersConfiguration(attributeModifiers));
        }
    }

    @Override
    public void convertEnchantments(){
        ConfigurationSection enchantsSection = this.iaItemSection.getConfigurationSection("enchants");
        if (isNotNull(enchantsSection)){
            fr.robie.craftengineconverter.api.configuration.item.data.EnchantmentConfiguration enchantmentConfiguration = new fr.robie.craftengineconverter.api.configuration.item.data.EnchantmentConfiguration();
            for (String enchantmentKey : enchantsSection.getKeys(false)){
                int enchantLevel = enchantsSection.getInt(enchantmentKey, 1);
                enchantmentConfiguration.addEnchantment(enchantmentKey, enchantLevel);
            }
            if (enchantmentConfiguration.hasEnchantments())
                this.craftEngineItemsConfiguration.addItemConfiguration(enchantmentConfiguration);
        }
        List<String> enchantments = this.iaItemSection.getStringList("enchants");
        if (!enchantments.isEmpty()){
            fr.robie.craftengineconverter.api.configuration.item.data.EnchantmentConfiguration enchantmentConfiguration = new fr.robie.craftengineconverter.api.configuration.item.data.EnchantmentConfiguration();
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
                enchantmentConfiguration.addEnchantment(enchantName, enchantLevel);
            }
            if (enchantmentConfiguration.hasEnchantments())
                this.craftEngineItemsConfiguration.addItemConfiguration(enchantmentConfiguration);
        }
    }

    @Override
    public void convertCustomModelData(){
        ConfigurationSection resourceSection = this.iaItemSection.getConfigurationSection("resource");
        if (isNotNull(resourceSection)){
            int customModelData = resourceSection.getInt("custom_model_data", resourceSection.getInt("model_id", 0));
            if (customModelData != 0){
                this.craftEngineItemsConfiguration.addItemConfiguration(new fr.robie.craftengineconverter.api.configuration.item.data.CustomModelDataConfiguration(customModelData));
            }
        }
    }

    @Override
    public void convertItemModel(){
        String itemModel = this.iaItemSection.getString("item_model");
        if (isValidString(itemModel)){
            this.craftEngineItemsConfiguration.addItemConfiguration(new fr.robie.craftengineconverter.api.configuration.item.components.ItemModelConfiguration(itemModel));
        }
    }

    @Override
    public void convertMaxStackSize(){
        int maxStackSize = this.iaItemSection.getInt("max_stack_size", -1);
        if (maxStackSize > 0 && maxStackSize <= 99){
            this.craftEngineItemsConfiguration.addItemConfiguration(new fr.robie.craftengineconverter.api.configuration.item.components.MaxStackSizeConfiguration(maxStackSize));
        }
    }

    @Override
    public void convertEnchantmentGlintOverride(){
        if (this.iaItemSection.getBoolean("glint", false)){
            this.craftEngineItemsConfiguration.addItemConfiguration(new fr.robie.craftengineconverter.api.configuration.item.components.EnchantmentGlintOverrideConfiguration(true));
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
                this.craftEngineItemsConfiguration.addItemConfiguration(new fr.robie.craftengineconverter.api.configuration.item.data.MaxDamageConfiguration(maxDamage));
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
                    try {
                        this.craftEngineItemsConfiguration.addItemConfiguration(new GlowDropColorConfiguration(DyeColor.valueOf(color.toLowerCase())));
                    } catch (Exception e){
                        Logger.debug(Message.ERROR__CONVERTER__INVALID_GLOW_DROP_COLOR,"converter", "IAItemsConverter", "item", this.itemId, "color", color, "valid_colors", Arrays.toString(DyeColor.values()));
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
                this.craftEngineItemsConfiguration.addItemConfiguration(new fr.robie.craftengineconverter.api.configuration.item.settings.DropDisplayConfiguration(false));
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
            this.craftEngineItemsConfiguration.addItemConfiguration(new fr.robie.craftengineconverter.api.configuration.item.data.TooltipStyleConfiguration(toolTipStyle));
        }
    }

    @Override
    public void convertFood(){
        ConfigurationSection consumableSection = this.iaItemSection.getConfigurationSection("consumable");
        if (isNotNull(consumableSection)){
            int nutrition = consumableSection.getInt("nutrition", -1);
            float saturation = (float) consumableSection.getDouble("saturation", -1.0);
            if (nutrition >= 0 && saturation >= 0){
                this.craftEngineItemsConfiguration.addItemConfiguration(new fr.robie.craftengineconverter.api.configuration.item.components.FoodConfiguration(nutrition, saturation));
            }
        }
    }

    @Override
    public void convertJukeboxPlayable() {
        String song = this.iaItemSection.getString("jukebox_disc.song", this.iaItemSection.getString("behaviours.music_disc.song.name"));
        if (isValidString(song)){
            this.craftEngineItemsConfiguration.addItemConfiguration(new fr.robie.craftengineconverter.api.configuration.item.components.JukeboxPlayableConfiguration(song));
        }
    }

    @Override
    public void convertEquippable() {
        convertEquipmentSection();
        convertSpecificPropertiesArmorSection();
    }

    private void convertEquipmentSection() {
        ConfigurationSection equipmentSection = this.iaItemSection.getConfigurationSection("equipment");
        if (!isNotNull(equipmentSection)) return;

        String assetId = equipmentSection.getString("id");
        if (!isValidString(assetId)) return;

        assetId = namespaced(assetId, this.namespace);
        EquipmentSlot equipmentSlot = resolveEquipmentSlot(equipmentSection);

        this.craftEngineItemsConfiguration.addItemConfiguration(new fr.robie.craftengineconverter.api.configuration.item.settings.EquippableConfiguration(assetId, equipmentSlot));
        applySlotAttributeModifiers(equipmentSection, equipmentSlot);
    }

    private EquipmentSlot resolveEquipmentSlot(ConfigurationSection equipmentSection) {
        EquipmentSlot fromItemId = getEquipmentSlotFromSuffix(this.itemId.toLowerCase(), false);
        if (fromItemId != null) return fromItemId;

        String slot = equipmentSection.getString("slot");
        if (isValidString(slot)) return null;

        return getEquipmentSlotFromSuffix(this.craftEngineItemsConfiguration.getMaterial().name(), true);
    }

    private EquipmentSlot getEquipmentSlotFromSuffix(String name, boolean uppercase) {
        String upString = uppercase ? name.toUpperCase() : name;
        if (upString.endsWith(uppercase ? "_HELMET" : "_helmet") || upString.endsWith("_SKULL") || upString.endsWith("_HAT")) return EquipmentSlot.HEAD;
        if (upString.endsWith(uppercase ? "_CHESTPLATE" : "_chestplate") || upString.endsWith("_ELYTRA")) return EquipmentSlot.CHEST;
        if (upString.endsWith(uppercase ? "_LEGGINGS" : "_leggings")) return EquipmentSlot.LEGS;
        if (upString.endsWith(uppercase ? "_BOOTS" : "_boots")) return EquipmentSlot.FEET;
        return null;
    }

    private void applySlotAttributeModifiers(ConfigurationSection equipmentSection, EquipmentSlot equipmentSlot) {
        if (equipmentSlot == null) return;

        ConfigurationSection slotAttributeModifiers = equipmentSection.getConfigurationSection("slot_attribute_modifiers");
        if (!isNotNull(slotAttributeModifiers)) return;

        net.momirealms.craftengine.core.attribute.AttributeModifier.Slot attributeSlot = toAttributeSlot(equipmentSlot);
        if (attributeSlot == null) return;

        double armor = slotAttributeModifiers.getDouble("armor", 0.0);
        fr.robie.craftengineconverter.api.configuration.utils.AttributeModifier modifier = new fr.robie.craftengineconverter.api.configuration.utils.AttributeModifier("minecraft:armor", attributeSlot, null, armor, net.momirealms.craftengine.core.attribute.AttributeModifier.Operation.ADD_VALUE, null);
        this.craftEngineItemsConfiguration.addItemConfiguration(new AttributeModifiersConfiguration(List.of(modifier)));
    }

    private net.momirealms.craftengine.core.attribute.AttributeModifier.Slot toAttributeSlot(EquipmentSlot equipmentSlot) {
        return switch (equipmentSlot) {
            case HEAD -> net.momirealms.craftengine.core.attribute.AttributeModifier.Slot.HEAD;
            case CHEST -> net.momirealms.craftengine.core.attribute.AttributeModifier.Slot.CHEST;
            case LEGS -> net.momirealms.craftengine.core.attribute.AttributeModifier.Slot.LEGS;
            case FEET -> net.momirealms.craftengine.core.attribute.AttributeModifier.Slot.FEET;
            default -> null;
        };
    }

    private void convertSpecificPropertiesArmorSection() {
        ConfigurationSection specificPropertiesSection = this.iaItemSection.getConfigurationSection("specific_properties");
        if (!isNotNull(specificPropertiesSection)) return;

        ConfigurationSection armorSection = specificPropertiesSection.getConfigurationSection("armor");
        if (!isNotNull(armorSection)) return;

        String assetId = armorSection.getString("custom_armor");
        if (!isValidString(assetId)) return;

        assetId = namespaced(assetId, this.namespace);
        this.isValidString(assetId);

        this.setAssetId(assetId);

        EquipmentSlot equipmentSlot = parseEquipmentSlot(armorSection.getString("slot"));
        this.craftEngineItemsConfiguration.addItemConfiguration(new fr.robie.craftengineconverter.api.configuration.item.settings.EquippableConfiguration(assetId, equipmentSlot));
    }

    private EquipmentSlot parseEquipmentSlot(String slot) {
        if (slot == null) return null;
        try {
            return EquipmentSlot.valueOf(slot.toUpperCase());
        } catch (IllegalArgumentException e) {
            Logger.debug("[IAItemsConverter] Invalid equipment slot '" + slot + "' for item " + this.itemId);
            return null;
        }
    }

    @Override
    public void convertItemTexture() {
        ConfigurationSection resourceSection = this.iaItemSection.getConfigurationSection("resource");

        if (isNotNull(resourceSection)) {
            handleResourceSection(resourceSection);
        } else {
            handleGraphicsSection();
        }
    }

    private void handleResourceSection(ConfigurationSection resourceSection) {
        boolean generate = resourceSection.getBoolean("generate", false);

        if (generate) {
            handleGeneratedResource(resourceSection);
        } else {
            handleExistingResource(resourceSection);
        }
    }

    private void handleGeneratedResource(ConfigurationSection resourceSection) {
        IADirectionalMode directionalMode = getDirectionalMode();

        switch (directionalMode) {
            case NONE -> handleNoneDirectionalMode(resourceSection);
            case ALL, LOG -> handleAllOrLogDirectionalMode(resourceSection);
            case FURNACE -> handleFurnaceDirectionalMode(resourceSection);
            default -> Logger.debug("[IAItemsConverter] Directional mode " + directionalMode + " is not supported for item " + this.itemId);
        }
    }

    private IADirectionalMode getDirectionalMode() {
        try {
            String mode = this.iaItemSection.getString("specific_properties.block.placed_model.directional_mode", "NONE");
            return IADirectionalMode.valueOf(mode.toUpperCase());
        } catch (Exception e) {
            return IADirectionalMode.NONE;
        }
    }

    private void handleNoneDirectionalMode(ConfigurationSection resourceSection) {
        String texturePath = getTexturePath(resourceSection);
        if (isValidString(texturePath)) {
            texturePath = namespaced(texturePath, this.namespace);
            ConfigurationSection blockSection = this.iaItemSection.getConfigurationSection("specific_properties.block");
            if (isNotNull(blockSection)){
                this.craftEngineItemsConfiguration.setModelConfiguration(new SimpleModelConfiguration(texturePath));
                handleBlockItem(resourceSection, blockSection);

                return;
            }
            SimpleModelConfiguration simpleModelConfiguration = new SimpleModelConfiguration(texturePath);
            GenerationConfiguration generation = new GenerationConfiguration("minecraft:item/generated");
            generation.addTexture("layer0", texturePath);
            simpleModelConfiguration.setGeneration(generation);
            this.craftEngineItemsConfiguration.setModelConfiguration(simpleModelConfiguration);
        }
    }

    private void handleAllOrLogDirectionalMode(ConfigurationSection resourceSection) {
        Map<BlockFace, String> faceTextureMap = buildFaceTextureMap(resourceSection, "ALL");
        if (faceTextureMap == null) return;

        SimpleModelConfiguration simpleModelConfiguration = createCubeModelTemplate(faceTextureMap);
        this.craftEngineItemsConfiguration.setModelConfiguration(simpleModelConfiguration);

        BlockConfiguration blockConfiguration = new BlockConfiguration(this.itemId);
        MultiStateBlock multiStateBlock = new MultiStateBlock();

        multiStateBlock.addAppearance("axisX", BlockAppearance.autoState(Plugins.ITEMS_ADDER, getBlockState(IAPlacedModelTypes.TILE), this.itemId, simpleModelConfiguration).postProcessor(section -> {
            ConfigurationSection model = getOrCreateSection(section, "model");
            model.set("x", 90);
            model.set("y", 90);
        }).build());

        multiStateBlock.addAppearance("axisY", BlockAppearance.autoState(Plugins.ITEMS_ADDER, getBlockState(IAPlacedModelTypes.TILE), this.itemId, simpleModelConfiguration).build());
        multiStateBlock.addAppearance("axisZ", BlockAppearance.autoState(Plugins.ITEMS_ADDER, getBlockState(IAPlacedModelTypes.TILE), this.itemId, simpleModelConfiguration).postProcessor(section -> {
            ConfigurationSection model = getOrCreateSection(section, "model");
            model.set("x", 90);
        }).build());
        AxisBlockStateProperty axis = new AxisBlockStateProperty("axis", Direction.Axis.Y);
        multiStateBlock.addProperty(axis);
        multiStateBlock.addVariant(new BlockVariant("axisX").addVariantCondition(axis, Direction.Axis.X));
        multiStateBlock.addVariant(new BlockVariant("axisY").addVariantCondition(axis, Direction.Axis.Y));
        multiStateBlock.addVariant(new BlockVariant("axisZ").addVariantCondition(axis, Direction.Axis.Z));
        blockConfiguration.setStateBlock(multiStateBlock);
        this.craftEngineItemsConfiguration.addItemConfiguration(blockConfiguration);

    }

    private void handleFurnaceDirectionalMode(ConfigurationSection resourceSection) {
        Map<BlockFace, String> faceTextureMap = buildFaceTextureMap(resourceSection, "Furnace");
        if (faceTextureMap == null) return;

        SimpleModelConfiguration simpleModelConfiguration = createCubeModelTemplate(faceTextureMap);
        this.craftEngineItemsConfiguration.setModelConfiguration(simpleModelConfiguration);

        BlockConfiguration blockConfiguration = new BlockConfiguration(this.itemId);
        MultiStateBlock multiStateBlock = new MultiStateBlock();
        multiStateBlock.addAppearance("east", BlockAppearance.autoState(Plugins.ITEMS_ADDER, getBlockState(IAPlacedModelTypes.TILE), this.itemId, simpleModelConfiguration).build());
        multiStateBlock.addAppearance("west", BlockAppearance.autoState(Plugins.ITEMS_ADDER, getBlockState(IAPlacedModelTypes.TILE), this.itemId, simpleModelConfiguration).build());
        multiStateBlock.addAppearance("north", BlockAppearance.autoState(Plugins.ITEMS_ADDER, getBlockState(IAPlacedModelTypes.TILE), this.itemId, simpleModelConfiguration).build());
        multiStateBlock.addAppearance("south", BlockAppearance.autoState(Plugins.ITEMS_ADDER, getBlockState(IAPlacedModelTypes.TILE), this.itemId, simpleModelConfiguration).build());
        HorizontalDirectionBlockStateProperty facing = new HorizontalDirectionBlockStateProperty("facing", HorizontalDirection.NORTH);
        multiStateBlock.addProperty(facing);
        multiStateBlock.addVariant(new BlockVariant("east").addVariantCondition(facing, HorizontalDirection.EAST));
        multiStateBlock.addVariant(new BlockVariant("west").addVariantCondition(facing, HorizontalDirection.WEST));
        multiStateBlock.addVariant(new BlockVariant("north").addVariantCondition(facing, HorizontalDirection.NORTH));
        multiStateBlock.addVariant(new BlockVariant("south").addVariantCondition(facing, HorizontalDirection.SOUTH));
        blockConfiguration.setStateBlock(multiStateBlock);

        this.craftEngineItemsConfiguration.addItemConfiguration(blockConfiguration);
    }

    private Map<BlockFace, String> buildFaceTextureMap(ConfigurationSection resourceSection, String modeName) {
        List<String> faceTextures = resourceSection.getStringList("textures");

        if (faceTextures.size() != 6) {
            Logger.debug("[IAItemsConverter] Directional mode " + modeName + " requires 6 textures for item " + this.itemId);
            return null;
        }


        Map<BlockFace, String> faceTextureMap = new HashMap<>();

        for (String faceTexture : faceTextures) {
            String cleanedTexture = cleanPath(faceTexture);
            if (isNull(cleanedTexture)) continue;

            BlockFace face = determineBlockFace(cleanedTexture);
            if (face != null) {
                faceTextureMap.put(face, namespaced(cleanedTexture, this.namespace));
            } else {
                Logger.debug("[IAItemsConverter] Invalid texture name " + faceTexture + " for directional mode " + modeName + " for item " + this.itemId);
                return null;
            }
        }

        if (faceTextureMap.size() != 6) {
            Logger.debug("[IAItemsConverter] Directional mode " + modeName + " requires 6 valid textures for item " + this.itemId);
            return null;
        }

        return faceTextureMap;
    }

    private BlockFace determineBlockFace(String textureName) {
        if (textureName.endsWith("_down")) return BlockFace.DOWN;
        if (textureName.endsWith("_up")) return BlockFace.UP;
        if (textureName.endsWith("_north")) return BlockFace.NORTH;
        if (textureName.endsWith("_south")) return BlockFace.SOUTH;
        if (textureName.endsWith("_west")) return BlockFace.WEST;
        if (textureName.endsWith("_east")) return BlockFace.EAST;
        return null;
    }

    private SimpleModelConfiguration createCubeModelTemplate(Map<BlockFace, String> faceTextureMap) {
        GenerationConfiguration generation = new GenerationConfiguration("minecraft:block/cube");
        generation.addTexture("down",  faceTextureMap.get(BlockFace.DOWN));
        generation.addTexture("up",    faceTextureMap.get(BlockFace.UP));
        generation.addTexture("north", faceTextureMap.get(BlockFace.NORTH));
        generation.addTexture("south", faceTextureMap.get(BlockFace.SOUTH));
        generation.addTexture("west",  faceTextureMap.get(BlockFace.WEST));
        generation.addTexture("east",  faceTextureMap.get(BlockFace.EAST));

        SimpleModelConfiguration model = new SimpleModelConfiguration(faceTextureMap.get(BlockFace.NORTH));
        model.setGeneration(generation);
        return model;
    }

    private void handleExistingResource(ConfigurationSection resourceSection) {
        ConfigurationSection blockSection = this.iaItemSection.getConfigurationSection("specific_properties.block");

        if (isNotNull(blockSection)) {
            handleBlockItem(resourceSection, blockSection);
        }
        String modelPath = resourceSection.getString("model_path");
        if (!isValidString(modelPath)) {
            return;
        }
        modelPath = namespaced(modelPath, this.namespace);
        if (isNull(modelPath)){
            Logger.debug("[IAItemsConverter] Missing model path for item " + this.itemId + ". Cannot convert item texture.");
            return;
        }
        Material itemMaterial = this.craftEngineItemsConfiguration.getMaterial();
        if (itemMaterial == Material.FISHING_ROD){
            handleFishingRod3D(modelPath,modelPath+"_cast");
            return;
        }
        if (itemMaterial == Material.BOW){
            handleBow3D(modelPath,modelPath+"_0",modelPath+"_1",modelPath+"_2");
            return;
        }
        if (itemMaterial == Material.SHIELD){
            handleShield3D(modelPath,modelPath+"_blocking");
            return;
        }
        handleSimpleModelPath(modelPath);
    }

    private void handleBlockItem(ConfigurationSection resourceSection, ConfigurationSection blockSection) {
        IAPlacedModelTypes placedModelType = getPlacedModelType(blockSection);

        BlockConfiguration blockConfiguration = new BlockConfiguration(this.itemId);
        BlockSettings blockSettings = blockConfiguration.getBlockSettings();

        configureBlockProperties(blockSection, blockSettings);
        configureBlockSounds(blockSection, blockSettings);
        configureLiquidPlacement(blockSection, blockConfiguration);

        String modelPath = resourceSection.getString("model_path");
        if (!isValidString(modelPath)) {
            boolean isGenerated = resourceSection.getBoolean("generate", false);
            if (isGenerated) {
                String texturePath = getTexturePath(resourceSection);
                if (!isValidString(texturePath)) {
                    Logger.debug("[IAItemsConverter] Missing texture path for generated block item " + this.itemId + ". Cannot convert item texture.");
                    return;
                }
                texturePath = namespaced(texturePath, this.namespace);

                GenerationConfiguration generation = new GenerationConfiguration("minecraft:block/cube_all");
                generation.addTexture("all", texturePath);

                SimpleModelConfiguration model = new SimpleModelConfiguration(texturePath);
                model.setGeneration(generation);

                blockConfiguration.setStateBlock(new SingleStateBlock(Plugins.ITEMS_ADDER, getBlockState(placedModelType), this.itemId, model));
                this.craftEngineItemsConfiguration.addItemConfiguration(blockConfiguration);
                return;
            } else {
                Logger.debug("[IAItemsConverter] Missing model path for block item " + this.itemId + ". Cannot convert item texture.");
                return;
            }
        }

        modelPath = namespaced(modelPath, this.namespace);

        blockConfiguration.setStateBlock(new SingleStateBlock(Plugins.ITEMS_ADDER, getBlockState(placedModelType), this.itemId, new SimpleModelConfiguration(modelPath)));
        this.craftEngineItemsConfiguration.addItemConfiguration(blockConfiguration);
    }

    private IAPlacedModelTypes getPlacedModelType(ConfigurationSection blockSection) {
        try {
            String type = blockSection.getString("placed_model.type", "REAL");
            IAPlacedModelTypes modelType = IAPlacedModelTypes.valueOf(type.toUpperCase());

            if (modelType == IAPlacedModelTypes.FIRE) {
                Logger.info("[IAItemsConverter] Placed model type FIRE is not supported by CraftEngine so it will be converted as REAL for item " + this.itemId);
                return IAPlacedModelTypes.REAL;
            }

            return modelType;
        } catch (Exception e) {
            return IAPlacedModelTypes.REAL;
        }
    }

    private void configureBlockProperties(ConfigurationSection iaBlockSection, BlockSettings blockSettings) {
        int lightLevel = iaBlockSection.getInt("light_level", 0);
        if (lightLevel > 0) {
            blockSettings.setLuminance(lightLevel);
        }

        double hardness = iaBlockSection.getDouble("hardness", 2f);
        blockSettings.setHardness((float) hardness);

        double blastResistance = iaBlockSection.getDouble("blast_resistance", 2d);
        blockSettings.setResistance((float) blastResistance);

        // TODO: implement break tools blacklist/whitelist conversion
        List<String> breakToolsBlackList = iaBlockSection.getStringList("break_tools_blacklist");
        List<String> breakToolsWhiteList = iaBlockSection.getStringList("break_tools_whitelist");
    }

    private void configureBlockSounds(ConfigurationSection blockSection, BlockSettings blockSettings) {
        ConfigurationSection soundSection = blockSection.getConfigurationSection("sounds");
        if (isNotNull(soundSection)) {
            String fallSound = soundSection.getString("fall");
            blockSettings.setFallSound(fallSound);
            String hitSound = soundSection.getString("hit");
            blockSettings.setHitSound(hitSound);
            String breakSound = soundSection.getString("break");
            blockSettings.setBreakSound(breakSound);
            String stepSound = soundSection.getString("step");
            blockSettings.setStepSound(stepSound);
            String placeSound = soundSection.getString("place");
            blockSettings.setPlaceSound(placeSound);
        }
    }

    private void configureLiquidPlacement(ConfigurationSection blockSection, BlockConfiguration blockConfiguration) {
        boolean placeableOnWater = blockSection.getBoolean("placeable_on_water", false);
        boolean placeableOnLava = blockSection.getBoolean("placeable_on_lava", false);

        if (placeableOnWater || placeableOnLava) {
            OnLiquidBlockBehavior onLiquidBlockBehavior = new OnLiquidBlockBehavior();
            if (placeableOnWater) onLiquidBlockBehavior.addLiquidType("water");
            if (placeableOnLava) onLiquidBlockBehavior.addLiquidType("lava");
            blockConfiguration.addBehavior(onLiquidBlockBehavior);
        }
    }

    public CraftEngineBlockState getBlockState(IAPlacedModelTypes placedModelType) {
        return switch (placedModelType) {
            case REAL_NOTE -> CraftEngineBlockState.NOTE_BLOCK;
            case REAL_TRANSPARENT -> CraftEngineBlockState.CHORUS;
            case REAL_WIRE -> CraftEngineBlockState.TRIPWIRE;
            case REAL -> CraftEngineBlockState.MUSHROOM;
            default -> CraftEngineBlockState.SOLID;
        };
    }

    private void handleSimpleModelPath(@NotNull String namespacedModelPath) {
        this.craftEngineItemsConfiguration.setModelConfiguration(new SimpleModelConfiguration(namespacedModelPath));

    }

    private void handleGraphicsSection() {
        ConfigurationSection graphicsSection = this.iaItemSection.getConfigurationSection("graphics");
        if (isNull(graphicsSection)) return;

        if (handleGraphicsModel(graphicsSection)) return;

        boolean isBlock = this.iaItemSection.contains("behaviours.block.placed_model.type");
        String texturePath = graphicsSection.getString("texture");

        if (isValidString(texturePath) && !isBlock) {
            handleSimpleTexture(texturePath);
        } else if (isBlock) {
            handleBlockGraphics(graphicsSection, texturePath);
        } else {
            handleComplexModels(graphicsSection);
        }
    }

    private boolean handleGraphicsModel(ConfigurationSection graphicsSection) {
        String modelPath = graphicsSection.getString("model");
        if (isValidString(modelPath)) {
            modelPath = namespaced(modelPath, this.namespace);
            this.craftEngineItemsConfiguration.setModelConfiguration(new SimpleModelConfiguration(modelPath));
            return true;
        }
        return false;
    }

    private void handleSimpleTexture(String texturePath) {
        texturePath = namespaced(texturePath, this.namespace);
        SimpleModelConfiguration modelConfiguration = new SimpleModelConfiguration(texturePath);
        GenerationConfiguration generation = new GenerationConfiguration("minecraft:item/generated");
        generation.addTexture("layer0", texturePath);
        modelConfiguration.setGeneration(generation);
        this.craftEngineItemsConfiguration.setModelConfiguration(modelConfiguration);
    }

    private void handleBlockGraphics(ConfigurationSection graphicsSection, String texturePath) {
        BlockParent parent = getBlockParent(graphicsSection);

        if (isNotNull(parent)) {
            handleBlockIcon(graphicsSection);

            if (parent == BlockParent.CROSS) {
                handleCrossBlock(graphicsSection);
            } else {
                Logger.debug("[IAItemsConverter] Block parent " + parent + " is not supported for item " + this.itemId + ". Please open an issue to request support.");
            }
        } else if (isValidString(texturePath)) {
            texturePath = namespaced(texturePath, this.namespace);
            SimpleModelConfiguration modelConfiguration = new SimpleModelConfiguration(texturePath);
            GenerationConfiguration generation = new GenerationConfiguration("minecraft:block/cube_all");
            generation.addTexture("all", texturePath);
            modelConfiguration.setGeneration(generation);
            this.craftEngineItemsConfiguration.setModelConfiguration(modelConfiguration);
        }
    }

    private BlockParent getBlockParent(ConfigurationSection graphicsSection) {
        try {
            String parentStr = graphicsSection.getString("parent", "");
            return BlockParent.valueOf(parentStr.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private void handleBlockIcon(ConfigurationSection graphicsSection) {
        String iconPath = graphicsSection.getString("icon");
        if (isValidString(iconPath)) {
            iconPath = namespaced(iconPath, this.namespace);
            SimpleModelConfiguration modelConfiguration = new SimpleModelConfiguration(iconPath);
            GenerationConfiguration generation = new GenerationConfiguration("minecraft:item/generated");
            generation.addTexture("layer0", iconPath);
            modelConfiguration.setGeneration(generation);
            this.craftEngineItemsConfiguration.setModelConfiguration(modelConfiguration);
        }
    }

    private void handleCrossBlock(ConfigurationSection graphicsSection) {
        String crossTexture = graphicsSection.getString("textures.cross", graphicsSection.getString("texture"));
        if (!isValidString(crossTexture)) return;

        crossTexture = namespaced(crossTexture, this.namespace);

        BlockConfiguration blockConfiguration = new BlockConfiguration(this.itemId);

        GenerationConfiguration generation = new GenerationConfiguration("minecraft:block/cross");
        generation.addTexture("cross", crossTexture);

        SimpleModelConfiguration model = new SimpleModelConfiguration(crossTexture);
        model.setGeneration(generation);

        blockConfiguration.setStateBlock(new SingleStateBlock(Plugins.ITEMS_ADDER, CraftEngineBlockState.SAPLING, this.itemId, model));

        this.craftEngineItemsConfiguration.addItemConfiguration(blockConfiguration);
    }

    private void handleComplexModels(ConfigurationSection graphicsSection) {
        ConfigurationSection texturesSection = graphicsSection.getConfigurationSection("textures");
        if (isNotNull(texturesSection)) {
            handle2DModels(texturesSection);
            return;
        }

        ConfigurationSection modelsSection = graphicsSection.getConfigurationSection("models");
        if (isNotNull(modelsSection)) {
            handle3DModels(modelsSection);
        }
    }

    private void handle2DModels(ConfigurationSection texturesSection) {
        Set<String> keys = texturesSection.getKeys(false);

        if (IAModelsKeys.BOW.containsAny(keys) && keys.size() == IAModelsKeys.BOW.getKeysCount()) {
            handleBow2D(texturesSection);
        } else if (IAModelsKeys.FISHING_ROD.containsAny(keys) && keys.size() == IAModelsKeys.FISHING_ROD.getKeysCount()) {
            handleFishingRod2D(texturesSection);
        } else if (IAModelsKeys.CROSSBOW.containsAny(keys) && keys.size() == IAModelsKeys.CROSSBOW.getKeysCount()) {
            handleCrossbow2D(texturesSection);
        }
    }

    private void handle3DModels(ConfigurationSection modelsSection) {
        Set<String> keys = modelsSection.getKeys(false);

        if (IAModelsKeys.BOW.containsAny(keys) && keys.size() == IAModelsKeys.BOW.getKeysCount()) {
            handleBow3D(namespaced(modelsSection.getString("normal"), this.namespace),
                    namespaced(modelsSection.getString("pulling_0"), this.namespace),
                    namespaced(modelsSection.getString("pulling_1"), this.namespace),
                    namespaced(modelsSection.getString("pulling_2"), this.namespace)
                );
        } else if (IAModelsKeys.FISHING_ROD.containsAny(keys) && keys.size() == IAModelsKeys.FISHING_ROD.getKeysCount()) {
            handleFishingRod3D(namespaced(modelsSection.getString("normal"),this.namespace),namespaced(modelsSection.getString("cast"),this.namespace));
        } else if (IAModelsKeys.CROSSBOW.containsAny(keys) && keys.size() == IAModelsKeys.CROSSBOW.getKeysCount()) {
            handleCrossbow3D(modelsSection);
        } else if (IAModelsKeys.TRIDENT.containsAny(keys) && keys.size() == IAModelsKeys.TRIDENT.getKeysCount()) {
            handleTrident3D(modelsSection);
        } else if (IAModelsKeys.SHIELD.containsAny(keys) && keys.size() == IAModelsKeys.SHIELD.getKeysCount()) {
            handleShield3D(namespaced(modelsSection.getString("normal"), this.namespace),
                    namespaced(modelsSection.getString("blocking"), this.namespace)
            );
        }
    }

    private void handleBow2D(ConfigurationSection texturesSection) {
        String normalTexture = namespaced(texturesSection.getString("normal"), this.namespace);
        String pulling0Texture = namespaced(texturesSection.getString("pulling_0"), this.namespace);
        String pulling1Texture = namespaced(texturesSection.getString("pulling_1"), this.namespace);
        String pulling2Texture = namespaced(texturesSection.getString("pulling_2"), this.namespace);

        UseDurationRangeDispatchConfiguration pullingDispatch = new UseDurationRangeDispatchConfiguration();
        pullingDispatch.setScale(0.05);
        pullingDispatch.addEntry(0.65, buildSimpleModel("minecraft:item/bow_pulling_1", pulling1Texture));
        pullingDispatch.addEntry(0.90, buildSimpleModel("minecraft:item/bow_pulling_2", pulling2Texture));
        pullingDispatch.setFallback(buildSimpleModel("minecraft:item/bow_pulling_0", pulling0Texture));

        ConditionModelConfiguration usingItemCondition = new ConditionModelConfiguration("minecraft:using_item");
        usingItemCondition.setOnTrue(pullingDispatch);
        usingItemCondition.setOnFalse(buildSimpleModel("minecraft:item/bow", normalTexture));

        this.craftEngineItemsConfiguration.setModelConfiguration(usingItemCondition);
    }

    private SimpleModelConfiguration buildSimpleModel(String parent, String texture) {
        GenerationConfiguration generation = new GenerationConfiguration(parent);
        generation.addTexture("layer0", texture);

        SimpleModelConfiguration model = new SimpleModelConfiguration(texture);
        model.setGeneration(generation);
        return model;
    }

    private void handleFishingRod2D(ConfigurationSection texturesSection) {
        String normalTexture = namespaced(texturesSection.getString("normal"), this.namespace);
        String castTexture = namespaced(texturesSection.getString("cast"), this.namespace);

        ConditionModelConfiguration castCondition = new ConditionModelConfiguration("minecraft:fishing_rod/cast");
        castCondition.setOnFalse(buildSimpleModel("minecraft:item/fishing_rod", normalTexture));
        castCondition.setOnTrue(buildSimpleModel("minecraft:item/fishing_rod",  castTexture));

        this.craftEngineItemsConfiguration.setModelConfiguration(castCondition);
    }

    private void handleCrossbow2D(ConfigurationSection texturesSection) {
        String normalTexture = namespaced(texturesSection.getString("normal"), this.namespace);
        String pulling0Texture = namespaced(texturesSection.getString("pulling_0"), this.namespace);
        String pulling1Texture = namespaced(texturesSection.getString("pulling_1"), this.namespace);
        String pulling2Texture = namespaced(texturesSection.getString("pulling_2"), this.namespace);
        String rocketTexture = namespaced(texturesSection.getString("rocket"), this.namespace);
        String arrowTexture = namespaced(texturesSection.getString("arrow"), this.namespace);

        ChargeTypeSelectConfiguration chargeTypeSelect = new ChargeTypeSelectConfiguration();
        chargeTypeSelect.addCase(ChargeTypeSelectConfiguration.ChargeType.ARROW, buildSimpleModel("minecraft:item/crossbow_arrow", arrowTexture));
        chargeTypeSelect.addCase(ChargeTypeSelectConfiguration.ChargeType.ROCKET, buildSimpleModel("minecraft:item/crossbow_firework", rocketTexture));
        chargeTypeSelect.setFallback(buildSimpleModel("minecraft:item/crossbow", normalTexture));

        UseDurationRangeDispatchConfiguration pullingDispatch = new UseDurationRangeDispatchConfiguration();
        pullingDispatch.addEntry(0.58, buildSimpleModel("minecraft:item/crossbow_pulling_1", pulling1Texture));
        pullingDispatch.addEntry(1.0, buildSimpleModel("minecraft:item/crossbow_pulling_2", pulling2Texture));
        pullingDispatch.setFallback(buildSimpleModel("minecraft:item/crossbow_pulling_0", pulling0Texture));

        ConditionModelConfiguration usingItemCondition = new ConditionModelConfiguration("minecraft:using_item");
        usingItemCondition.setOnFalse(chargeTypeSelect);
        usingItemCondition.setOnTrue(pullingDispatch);

        this.craftEngineItemsConfiguration.setModelConfiguration(usingItemCondition);
    }

    private void handleBow3D(String defaultModelPath, String pulling0ModelPath, String pulling1ModelPath, String pulling2ModelPath) {
        UseDurationRangeDispatchConfiguration pullingDispatch = new UseDurationRangeDispatchConfiguration();
        pullingDispatch.setScale(0.05);
        pullingDispatch.addEntry(0.65, new SimpleModelConfiguration(pulling1ModelPath));
        pullingDispatch.addEntry(0.90, new SimpleModelConfiguration(pulling2ModelPath));
        pullingDispatch.setFallback(new SimpleModelConfiguration(pulling0ModelPath));

        ConditionModelConfiguration usingItemCondition = new ConditionModelConfiguration("minecraft:using_item");
        usingItemCondition.setOnFalse(new SimpleModelConfiguration(defaultModelPath));
        usingItemCondition.setOnTrue(pullingDispatch);

        this.craftEngineItemsConfiguration.setModelConfiguration(usingItemCondition);
    }

    private void handleFishingRod3D(String defaultModelPath, String castingModelPath) {
        ConditionModelConfiguration castCondition = new ConditionModelConfiguration("minecraft:fishing_rod/cast");
        castCondition.setOnFalse(new SimpleModelConfiguration(defaultModelPath));
        castCondition.setOnTrue(new SimpleModelConfiguration(castingModelPath));

        this.craftEngineItemsConfiguration.setModelConfiguration(castCondition);
    }

    private void handleCrossbow3D(ConfigurationSection modelsSection) {
        String normalModel = namespaced(modelsSection.getString("normal"),    this.namespace);
        String pulling0Model = namespaced(modelsSection.getString("pulling_0"), this.namespace);
        String pulling1Model = namespaced(modelsSection.getString("pulling_1"), this.namespace);
        String pulling2Model = namespaced(modelsSection.getString("pulling_2"), this.namespace);
        String rocketModel = namespaced(modelsSection.getString("rocket"),    this.namespace);
        String arrowModel = namespaced(modelsSection.getString("arrow"),     this.namespace);

        ChargeTypeSelectConfiguration chargeTypeSelect = new ChargeTypeSelectConfiguration();
        chargeTypeSelect.addCase(ChargeTypeSelectConfiguration.ChargeType.ARROW, new SimpleModelConfiguration(arrowModel));
        chargeTypeSelect.addCase(ChargeTypeSelectConfiguration.ChargeType.ROCKET, new SimpleModelConfiguration(rocketModel));
        chargeTypeSelect.setFallback(new SimpleModelConfiguration(normalModel));

        UseDurationRangeDispatchConfiguration pullingDispatch = new UseDurationRangeDispatchConfiguration();
        pullingDispatch.addEntry(0.58, new SimpleModelConfiguration(pulling1Model));
        pullingDispatch.addEntry(1.0,  new SimpleModelConfiguration(pulling2Model));
        pullingDispatch.setFallback(new SimpleModelConfiguration(pulling0Model));

        ConditionModelConfiguration usingItemCondition = new ConditionModelConfiguration("minecraft:using_item");
        usingItemCondition.setOnFalse(chargeTypeSelect);
        usingItemCondition.setOnTrue(pullingDispatch);

        this.craftEngineItemsConfiguration.setModelConfiguration(usingItemCondition);
    }
    private void handleTrident3D(ConfigurationSection modelsSection) {
        String normalModel = namespaced(modelsSection.getString("normal"), this.namespace);
        String throwingModel = namespaced(modelsSection.getString("throwing"), this.namespace);

        ConditionModelConfiguration usingItemCondition = new ConditionModelConfiguration("minecraft:using_item");
        usingItemCondition.setOnTrue( new SimpleModelConfiguration(throwingModel));
        usingItemCondition.setOnFalse(new SimpleModelConfiguration(normalModel));

        DisplayContentSelectConfiguration displayContentSelect = new DisplayContentSelectConfiguration();
        displayContentSelect.addCase(new SimpleModelConfiguration(normalModel),
                DisplayContentSelectConfiguration.DisplayContent.GUI,
                DisplayContentSelectConfiguration.DisplayContent.GROUND,
                DisplayContentSelectConfiguration.DisplayContent.FIXED
        );
        displayContentSelect.setFallback(usingItemCondition);

        this.craftEngineItemsConfiguration.setModelConfiguration(displayContentSelect);
    }
    private void handleShield3D(String defaultModelPath, String blockingModelPath) {
        ConditionModelConfiguration usingItemCondition = new ConditionModelConfiguration("minecraft:using_item");
        usingItemCondition.setOnTrue( new SimpleModelConfiguration(blockingModelPath));
        usingItemCondition.setOnFalse(new SimpleModelConfiguration(defaultModelPath));

        this.craftEngineItemsConfiguration.setModelConfiguration(usingItemCondition);
    }

    @Override
    public void convertOther(){
        ConfigurationSection behavioursSection = this.iaItemSection.getConfigurationSection("behaviours");
        if (isNotNull(behavioursSection)){
            for (String behaviourKey : behavioursSection.getKeys(false)){
                switch (behaviourKey){
                    case "furniture" -> {
                        ConfigurationSection furnitureSection = behavioursSection.getConfigurationSection("furniture");
                        if (isNotNull(furnitureSection)){
                            this.convertFurniture(furnitureSection, behavioursSection);
                        }
                    }
                    case "fuel"->{
                        ConfigurationSection fuelSection = behavioursSection.getConfigurationSection("fuel");
                        if (isNotNull(fuelSection)){
                            int burnTicks = fuelSection.getInt("burn_ticks", -1);
                            if (burnTicks > 0){
                                this.craftEngineItemsConfiguration.addItemConfiguration(new fr.robie.craftengineconverter.api.configuration.item.settings.FuelTimeSettingConfiguration(burnTicks));
                            }
                            // machines fuel type not supported
                        }
                    }
                    default -> {

                    }
                }
            }
        }
    }

    private void convertFurniture(ConfigurationSection furnitureSection, ConfigurationSection behavioursSection) {
        IAEntityTypes entityType = IAEntityTypes.ITEM_FRAME;
        try {
            entityType = IAEntityTypes.valueOf(furnitureSection.getString("entity", "ITEM_FRAME").toUpperCase());
        } catch (Exception ignored) {}

        boolean isBig = furnitureSection.getBoolean("small", true);

        Set<FurniturePlacement> placements = new HashSet<>();
        ConfigurationSection placeableSection = furnitureSection.getConfigurationSection("placeable_on");
        if (isNotNull(placeableSection)) {
            if (placeableSection.getBoolean("floor", true)) placements.add(FurniturePlacement.GROUND);
            if (placeableSection.getBoolean("ceiling", true)) placements.add(FurniturePlacement.CEILING);
            if (placeableSection.getBoolean("wall", true)) placements.add(FurniturePlacement.WALL);
        } else {
            placements.addAll(List.of(FurniturePlacement.values()));
        }

        if (placements.isEmpty()) return;

        FurnitureConfiguration furnitureConfiguration = new FurnitureConfiguration();

        // --- Display properties ---
        Billboard transformType = Billboard.FIXED;
        ItemDisplayType displayType = ItemDisplayType.NONE;
        FloatsUtils displayTranslation = new FloatsUtils(3, new float[]{0f, 0.5f, 0f});
        if (isBig) displayTranslation.addValue(1, 1f);
        FloatsUtils scale = new FloatsUtils(3, new float[]{1f, 1f, 1f});

        ConfigurationSection displayTransformationSection = furnitureSection.getConfigurationSection("display_transformation");
        if (isNotNull(displayTransformationSection)) {
            try {
                displayType = ItemDisplayType.valueOf(displayTransformationSection.getString("transform", "FIXED").toUpperCase());
            } catch (Exception e) {
                Logger.debug(Message.WARNING__CONVERTER__IA__FURNITURE__UNKNOWN_DISPLAY_TRANSFORM, "item", this.itemId, "transform", displayTransformationSection.getString("transform"));
            }
            ConfigurationSection translationSection = displayTransformationSection.getConfigurationSection("translation");
            if (isNotNull(translationSection)) {
                double x = translationSection.getDouble("x");
                double y = translationSection.getDouble("y");
                double z = translationSection.getDouble("z");
                if (x != 0d) displayTranslation.setValue(0, (float) x);
                if (y != 0d) displayTranslation.setValue(1, (float) y);
                if (z != 0d) displayTranslation.setValue(2, (float) z);
            }
            ConfigurationSection scaleSection = displayTransformationSection.getConfigurationSection("scale");
            if (isNotNull(scaleSection)) {
                double x = scaleSection.getDouble("x", 1.0);
                double y = scaleSection.getDouble("y", 1.0);
                double z = scaleSection.getDouble("z", 1.0);
                if (x != 1.0) scale.setValue(0, (float) x);
                if (y != 1.0) scale.setValue(1, (float) y);
                if (z != 1.0) scale.setValue(2, (float) z);
            }
        }

        // --- Element ---
        ItemElement element;
        if (entityType == IAEntityTypes.ARMOR_STAND) {
            ArmorStandElement armorStand = new ArmorStandElement(this.itemId);
            if (scale.isUpdated())
                armorStand.setScale(scale.getValue(0), scale.getValue(1), scale.getValue(2));
            if (!isBig) armorStand.setSmall(true);
            element = armorStand;
        } else {
            ItemDisplayElement itemDisplay = new ItemDisplayElement(this.itemId);
            int light = furnitureSection.getInt("light_level", -1);
            if (light >= 0) itemDisplay.display().setBrightness(light, -1);
            if (displayType != ItemDisplayType.NONE) itemDisplay.setDisplayTransform(displayType);
            itemDisplay.display().setBillboard(transformType);
            if (displayTranslation.isUpdated())
                itemDisplay.display().setTranslation(displayTranslation.getValue(0), displayTranslation.getValue(1), displayTranslation.getValue(2));
            if (scale.isUpdated())
                itemDisplay.display().setScale(scale.getValue(0), scale.getValue(1), scale.getValue(2));
            element = itemDisplay;
        }

        // --- Hitboxes ---
        double sitHeight = behavioursSection.getDouble("furniture_sit.sit_height", 0d);
        List<Hitbox> hitboxList = new ArrayList<>();
        ConfigurationSection iaHitboxesSection = furnitureSection.getConfigurationSection("hitbox");
        if (isNotNull(iaHitboxesSection)) {
            parseItemsAdderHitboxes(iaHitboxesSection, hitboxList, sitHeight);
        }

        // --- Loot ---
        furnitureConfiguration.setLoot(InternalTemplateManager.parseTemplate(Template.LOOT_TABLE_BASIC_DROP, "%type%", "furniture_item", "%item%", this.itemId));

        // --- Placements ---
        for (FurniturePlacement furniturePlacement : placements) {
            Placement placement = furnitureConfiguration.getOrCreatePlacement(furniturePlacement);
            placement.addElement(element);
            hitboxList.forEach(placement::addHitbox);
        }

        this.getCraftEngineItemsConfiguration().addItemConfiguration(furnitureConfiguration);
    }

    private void parseItemsAdderHitboxes(ConfigurationSection iaHitboxesSection, List<Hitbox> hitboxes, double seatPosition) {
        if (iaHitboxesSection == null) return;

        int length = iaHitboxesSection.getInt("length", 1);
        int width = iaHitboxesSection.getInt("width", 1);
        int height = iaHitboxesSection.getInt("height", 1);
        int lengthOffset = iaHitboxesSection.getInt("length_offset", 0);
        int widthOffset = iaHitboxesSection.getInt("width_offset", 0);
        int heightOffset = iaHitboxesSection.getInt("height_offset", 0);

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < width; z++) {
                    ShulkerHitbox hitbox = new ShulkerHitbox();
                    hitbox.setPosition(x + lengthOffset, y + heightOffset, z + widthOffset);
                    if (x == 0 && y == 0 && z == 0)
                        hitbox.addSeat(0, (float) seatPosition, 0, 0);
                    hitboxes.add(hitbox);
                }
            }
        }
    }

}
