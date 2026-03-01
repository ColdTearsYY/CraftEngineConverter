package fr.robie.craftengineconverter.common.enums;

import org.bukkit.inventory.ItemFlag;

public enum BukkitFlagToComponentFlag {

    HIDE_MAX_STACK_SIZE(fr.robie.craftengineconverter.api.ComponentFlag.MAX_STACK_SIZE),
    HIDE_MAX_DAMAGE(fr.robie.craftengineconverter.api.ComponentFlag.MAX_DAMAGE),
    HIDE_DAMAGE(fr.robie.craftengineconverter.api.ComponentFlag.DAMAGE),
    HIDE_UNBREAKABLE(fr.robie.craftengineconverter.api.ComponentFlag.UNBREAKABLE),
    HIDE_CUSTOM_NAME(fr.robie.craftengineconverter.api.ComponentFlag.CUSTOM_NAME),
    HIDE_ITEM_NAME(fr.robie.craftengineconverter.api.ComponentFlag.ITEM_NAME),
    HIDE_ITEM_MODEL(fr.robie.craftengineconverter.api.ComponentFlag.ITEM_MODEL),
    HIDE_LORE(fr.robie.craftengineconverter.api.ComponentFlag.LORE),
    HIDE_RARITY(fr.robie.craftengineconverter.api.ComponentFlag.RARITY),
    HIDE_ENCHANTMENTS(fr.robie.craftengineconverter.api.ComponentFlag.ENCHANTMENTS),
    HIDE_CAN_PLACE_ON(fr.robie.craftengineconverter.api.ComponentFlag.CAN_PLACE_ON),
    HIDE_CAN_BREAK(fr.robie.craftengineconverter.api.ComponentFlag.CAN_BREAK),
    HIDE_ATTRIBUTE_MODIFIERS(fr.robie.craftengineconverter.api.ComponentFlag.ATTRIBUTE_MODIFIERS),
    HIDE_CUSTOM_MODEL_DATA(fr.robie.craftengineconverter.api.ComponentFlag.CUSTOM_MODEL_DATA),
    HIDE_TOOLTIP_DISPLAY(fr.robie.craftengineconverter.api.ComponentFlag.TOOLTIP_DISPLAY),
    HIDE_REPAIR_COST(fr.robie.craftengineconverter.api.ComponentFlag.REPAIR_COST),
    HIDE_ENCHANTMENT_GLINT_OVERRIDE(fr.robie.craftengineconverter.api.ComponentFlag.ENCHANTMENT_GLINT_OVERRIDE),
    HIDE_INTANGIBLE_PROJECTILE(fr.robie.craftengineconverter.api.ComponentFlag.INTANGIBLE_PROJECTILE),
    HIDE_FOOD(fr.robie.craftengineconverter.api.ComponentFlag.FOOD),
    HIDE_CONSUMABLE(fr.robie.craftengineconverter.api.ComponentFlag.CONSUMABLE),
    HIDE_USE_REMAINDER(fr.robie.craftengineconverter.api.ComponentFlag.USE_REMAINDER),
    HIDE_USE_COOLDOWN(fr.robie.craftengineconverter.api.ComponentFlag.USE_COOLDOWN),
    HIDE_DAMAGE_RESISTANT(fr.robie.craftengineconverter.api.ComponentFlag.DAMAGE_RESISTANT),
    HIDE_TOOL(fr.robie.craftengineconverter.api.ComponentFlag.TOOL),
    HIDE_WEAPON(fr.robie.craftengineconverter.api.ComponentFlag.WEAPON),
    HIDE_ENCHANTABLE(fr.robie.craftengineconverter.api.ComponentFlag.ENCHANTABLE),
    HIDE_EQUIPPABLE(fr.robie.craftengineconverter.api.ComponentFlag.EQUIPPABLE),
    HIDE_REPAIRABLE(fr.robie.craftengineconverter.api.ComponentFlag.REPAIRABLE),
    HIDE_GLIDER(fr.robie.craftengineconverter.api.ComponentFlag.GLIDER),
    HIDE_TOOLTIP_STYLE(fr.robie.craftengineconverter.api.ComponentFlag.TOOLTIP_STYLE),
    HIDE_DEATH_PROTECTION(fr.robie.craftengineconverter.api.ComponentFlag.DEATH_PROTECTION),
    HIDE_BLOCKS_ATTACKS(fr.robie.craftengineconverter.api.ComponentFlag.BLOCKS_ATTACKS),
    HIDE_STORED_ENCHANTMENTS(fr.robie.craftengineconverter.api.ComponentFlag.STORED_ENCHANTMENTS),
    HIDE_DYED_COLOR(fr.robie.craftengineconverter.api.ComponentFlag.DYED_COLOR),
    HIDE_MAP_COLOR(fr.robie.craftengineconverter.api.ComponentFlag.MAP_COLOR),
    HIDE_MAP_ID(fr.robie.craftengineconverter.api.ComponentFlag.MAP_ID),
    HIDE_MAP_DECORATIONS(fr.robie.craftengineconverter.api.ComponentFlag.MAP_DECORATIONS),
    HIDE_MAP_POST_PROCESSING(fr.robie.craftengineconverter.api.ComponentFlag.MAP_POST_PROCESSING),
    HIDE_CHARGED_PROJECTILES(fr.robie.craftengineconverter.api.ComponentFlag.CHARGED_PROJECTILES),
    HIDE_BUNDLE_CONTENTS(fr.robie.craftengineconverter.api.ComponentFlag.BUNDLE_CONTENTS),
    HIDE_POTION_CONTENTS(fr.robie.craftengineconverter.api.ComponentFlag.POTION_CONTENTS),
    HIDE_POTION_DURATION_SCALE(fr.robie.craftengineconverter.api.ComponentFlag.POTION_DURATION_SCALE),
    HIDE_SUSPICIOUS_STEW_EFFECTS(fr.robie.craftengineconverter.api.ComponentFlag.SUSPICIOUS_STEW_EFFECTS),
    HIDE_WRITABLE_BOOK_CONTENT(fr.robie.craftengineconverter.api.ComponentFlag.WRITABLE_BOOK_CONTENT),
    HIDE_WRITTEN_BOOK_CONTENT(fr.robie.craftengineconverter.api.ComponentFlag.WRITTEN_BOOK_CONTENT),
    HIDE_TRIM(fr.robie.craftengineconverter.api.ComponentFlag.TRIM),
    HIDE_INSTRUMENT(fr.robie.craftengineconverter.api.ComponentFlag.INSTRUMENT),
    HIDE_PROVIDES_TRIM_MATERIAL(fr.robie.craftengineconverter.api.ComponentFlag.PROVIDES_TRIM_MATERIAL),
    HIDE_OMINOUS_BOTTLE_AMPLIFIER(fr.robie.craftengineconverter.api.ComponentFlag.OMINOUS_BOTTLE_AMPLIFIER),
    HIDE_JUKEBOX_PLAYABLE(fr.robie.craftengineconverter.api.ComponentFlag.JUKEBOX_PLAYABLE),
    HIDE_PROVIDES_BANNER_PATTERNS(fr.robie.craftengineconverter.api.ComponentFlag.PROVIDES_BANNER_PATTERNS),
    HIDE_RECIPES(fr.robie.craftengineconverter.api.ComponentFlag.RECIPES),
    HIDE_LODESTONE_TRACKER(fr.robie.craftengineconverter.api.ComponentFlag.LODESTONE_TRACKER),
    HIDE_FIREWORK_EXPLOSION(fr.robie.craftengineconverter.api.ComponentFlag.FIREWORK_EXPLOSION),
    HIDE_FIREWORKS(fr.robie.craftengineconverter.api.ComponentFlag.FIREWORKS),
    HIDE_PROFILE(fr.robie.craftengineconverter.api.ComponentFlag.PROFILE),
    HIDE_NOTE_BLOCK_SOUND(fr.robie.craftengineconverter.api.ComponentFlag.NOTE_BLOCK_SOUND),
    HIDE_BANNER_PATTERNS(fr.robie.craftengineconverter.api.ComponentFlag.BANNER_PATTERNS),
    HIDE_BASE_COLOR(fr.robie.craftengineconverter.api.ComponentFlag.BASE_COLOR),
    HIDE_POT_DECORATIONS(fr.robie.craftengineconverter.api.ComponentFlag.POT_DECORATIONS),
    HIDE_CONTAINER(fr.robie.craftengineconverter.api.ComponentFlag.CONTAINER),
    HIDE_BLOCK_STATE(fr.robie.craftengineconverter.api.ComponentFlag.BLOCK_DATA),
    HIDE_CONTAINER_LOOT(fr.robie.craftengineconverter.api.ComponentFlag.CONTAINER_LOOT),
    HIDE_BREAK_SOUND(fr.robie.craftengineconverter.api.ComponentFlag.BREAK_SOUND),
    HIDE_VILLAGER_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.VILLAGER_VARIANT),
    HIDE_WOLF_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.WOLF_VARIANT),
    HIDE_WOLF_SOUND_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.WOLF_SOUND_VARIANT),
    HIDE_WOLF_COLLAR(fr.robie.craftengineconverter.api.ComponentFlag.WOLF_COLLAR),
    HIDE_FOX_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.FOX_VARIANT),
    HIDE_SALMON_SIZE(fr.robie.craftengineconverter.api.ComponentFlag.SALMON_SIZE),
    HIDE_PARROT_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.PARROT_VARIANT),
    HIDE_TROPICAL_FISH_PATTERN(fr.robie.craftengineconverter.api.ComponentFlag.TROPICAL_FISH_PATTERN),
    HIDE_TROPICAL_FISH_BASE_COLOR(fr.robie.craftengineconverter.api.ComponentFlag.TROPICAL_FISH_BASE_COLOR),
    HIDE_TROPICAL_FISH_PATTERN_COLOR(fr.robie.craftengineconverter.api.ComponentFlag.TROPICAL_FISH_PATTERN_COLOR),
    HIDE_MOOSHROOM_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.MOOSHROOM_VARIANT),
    HIDE_RABBIT_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.RABBIT_VARIANT),
    HIDE_PIG_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.PIG_VARIANT),
    HIDE_COW_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.COW_VARIANT),
    HIDE_CHICKEN_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.CHICKEN_VARIANT),
    HIDE_FROG_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.FROG_VARIANT),
    HIDE_HORSE_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.HORSE_VARIANT),
    HIDE_PAINTING_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.PAINTING_VARIANT),
    HIDE_LLAMA_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.LLAMA_VARIANT),
    HIDE_AXOLOTL_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.AXOLOTL_VARIANT),
    HIDE_CAT_VARIANT(fr.robie.craftengineconverter.api.ComponentFlag.CAT_VARIANT),
    HIDE_CAT_COLLAR(fr.robie.craftengineconverter.api.ComponentFlag.CAT_COLLAR),
    HIDE_SHEEP_COLOR(fr.robie.craftengineconverter.api.ComponentFlag.SHEEP_COLOR),
    HIDE_SHULKER_COLOR(fr.robie.craftengineconverter.api.ComponentFlag.SHULKER_COLOR);

    private final fr.robie.craftengineconverter.api.ComponentFlag componentFlag;

    BukkitFlagToComponentFlag(fr.robie.craftengineconverter.api.ComponentFlag componentFlag) {
        this.componentFlag = componentFlag;
    }

    public fr.robie.craftengineconverter.api.ComponentFlag getComponentFlag() {
        return componentFlag;
    }

    public static fr.robie.craftengineconverter.api.ComponentFlag fromBukkitItemFlag(ItemFlag itemFlag) {
        try {
            return valueOf(itemFlag.name()).componentFlag;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}