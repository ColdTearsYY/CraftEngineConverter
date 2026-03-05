package fr.robie.craftengineconverter.api.configuration.item.models.composite;

import fr.robie.craftengineconverter.api.configuration.item.models.ModelConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CompositeModelConfiguration implements ModelConfiguration {
    private final List<ModelConfiguration> models = new ArrayList<>();

    public CompositeModelConfiguration(@NotNull List<ModelConfiguration> models) {
        this.models.addAll(models);
    }

    public CompositeModelConfiguration() {
    }

    public void addModel(@NotNull ModelConfiguration model) {
        this.models.add(Objects.requireNonNull(model, "model cannot be null"));
    }

    @Override
    public void serialize(@NotNull ConfigurationSection section) {
        section.set("type", "minecraft:composite");
        List<Map<String, Object>> serializedModels = new ArrayList<>();
        for (ModelConfiguration model : this.models) {
            YamlConfiguration temp = new YamlConfiguration();
            model.serialize(temp);
            serializedModels.add(temp.getValues(true));
        }
        section.set("models", serializedModels);
    }
}
