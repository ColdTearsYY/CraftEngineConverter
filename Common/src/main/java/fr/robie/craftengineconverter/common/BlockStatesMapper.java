package fr.robie.craftengineconverter.common;

import fr.robie.craftengineconverter.common.enums.Plugins;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.data.BlockData;
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
    public static BlockStatesMapper getInstance() {
        return INSTANCE;
    }

    public void clearMappingsForPlugin(Plugins plugins) {
        this.mappings.remove(plugins.name());
    }
}
