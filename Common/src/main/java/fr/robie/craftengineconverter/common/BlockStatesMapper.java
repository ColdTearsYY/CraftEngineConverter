package fr.robie.craftengineconverter.common;

import fr.robie.craftengineconverter.common.enums.Plugins;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.jetbrains.annotations.NotNull;
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

    public void convertNoteBlockState(@NotNull Plugins plugins, @NotNull String newCeName, int customVariation) throws IllegalArgumentException {
        int notesPerInstrument = Note.Tone.TONES_COUNT * 2;
        int variationsPerInstrument = notesPerInstrument * 2;

        int instrumentIndex = (customVariation - 1) / variationsPerInstrument;
        int noteIndex = ((customVariation - 1) % variationsPerInstrument) % notesPerInstrument;
        boolean powered = ((customVariation - 1) % variationsPerInstrument) >= notesPerInstrument;

        if (instrumentIndex == 0 && !powered) {
            noteIndex = noteIndex + 1;
        }

        int note = noteIndex;

        Instrument instrument = Instrument.values()[instrumentIndex];
        String instrumentName = switch (instrument) {
            case PIANO -> "harp";
            case BASS_DRUM -> "basedrum";
            case SNARE_DRUM -> "snare";
            case STICKS -> "hat";
            case BASS_GUITAR -> "bass";
            case FLUTE -> "flute";

            default -> instrument.name().toLowerCase();
        };
        String dataString = "[instrument="
                + instrumentName +
                ",note=" + note +
                ",powered=" + powered + "]";

        BlockData blockData = Bukkit.createBlockData(Material.NOTE_BLOCK, dataString);
        this.storeMapping(plugins, blockData, newCeName);
    }

    public void convertMushroomBlockState(@NotNull Plugins plugins, @NotNull String newCeName, int customVariation) throws IllegalArgumentException {
        BlockData blockData = Bukkit.createBlockData(Material.BROWN_MUSHROOM_BLOCK);
        if (blockData instanceof MultipleFacing multipleFacing) {

            multipleFacing.setFace(BlockFace.WEST,  (customVariation & 0b000001) != 0);
            multipleFacing.setFace(BlockFace.UP,    (customVariation & 0b000010) != 0);
            multipleFacing.setFace(BlockFace.SOUTH, (customVariation & 0b000100) != 0);
            multipleFacing.setFace(BlockFace.NORTH, (customVariation & 0b001000) != 0);
            multipleFacing.setFace(BlockFace.EAST,  (customVariation & 0b010000) != 0);
            multipleFacing.setFace(BlockFace.DOWN,  (customVariation & 0b100000) != 0);

            this.storeMapping(plugins, blockData, newCeName);
        }
    }

    public static BlockStatesMapper getInstance() {
        return INSTANCE;
    }

    public void clearMappingsForPlugin(Plugins plugins) {
        this.mappings.remove(plugins.name());
    }
}
