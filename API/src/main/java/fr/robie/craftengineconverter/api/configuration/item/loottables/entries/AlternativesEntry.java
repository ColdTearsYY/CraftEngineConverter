package fr.robie.craftengineconverter.api.configuration.item.loottables.entries;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AlternativesEntry extends AbstractLootEntry {
    private final List<LootEntry> children = new ArrayList<>();

    public AlternativesEntry() {
        super("alternatives");
    }

    public void addChild(@NotNull LootEntry entry) {
        this.children.add(Objects.requireNonNull(entry, "entry cannot be null"));
    }

    @Override
    public void serialize(@NotNull ConfigurationSection section) {
        super.serialize(section);
        if (!this.children.isEmpty()) {
            List<Map<String, Object>> serializedChildren = new ArrayList<>();
            for (LootEntry entry : this.children) {
                YamlConfiguration temp = new YamlConfiguration();
                entry.serialize(temp);
                serializedChildren.add(temp.getValues(true));
            }
            section.set("children", serializedChildren);
        }
    }
}
