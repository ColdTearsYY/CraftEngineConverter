package fr.robie.craftengineconverter.converter.itemsadder;

import fr.robie.craftengineconverter.CraftEngineConverter;
import fr.robie.craftengineconverter.common.PluginNameMapper;
import fr.robie.craftengineconverter.common.enums.ConverterOptions;
import fr.robie.craftengineconverter.common.enums.Plugins;
import fr.robie.craftengineconverter.common.logger.LogType;
import fr.robie.craftengineconverter.common.logger.Logger;
import fr.robie.craftengineconverter.common.progress.BukkitProgressBar;
import fr.robie.craftengineconverter.converter.Converter;
import fr.robie.craftengineconverter.utils.ConfigFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ItemsAdderConverter extends Converter {
    public ItemsAdderConverter(CraftEngineConverter plugin) {
        super(plugin, "ItemsAdder");
    }

    @Override
    public CompletableFuture<Void> convertItems(boolean async, Optional<Player> player) {
        return executeTask(async, () -> convertItemsSync(player));
    }

    private void convertItemsSync(Optional<Player> player) {
        File inputFolder = new File("plugins/"+this.converterName+"/contents");
        File outputFolder = new File(this.plugin.getDataFolder(), "converted/"+converterName+"/CraftEngine/resources/craftengineconverter/configuration/items");
        if (!inputFolder.exists() || !inputFolder.isDirectory()) {
            Logger.debug("ItemsAdder contents folder not found: " + inputFolder.getAbsolutePath());
            return;
        }

        if (outputFolder.exists()){
            deleteDirectory(outputFolder);
        }

        if (!outputFolder.mkdirs()) {
            Logger.debug("Failed to create output folder: " + outputFolder.getAbsolutePath(), LogType.ERROR);
            return;
        }

        Queue<@NotNull ConfigFile> toConvert = new LinkedList<>();
        int totalItems = populateQueueIA(inputFolder, inputFolder, toConvert);

        if (toConvert.isEmpty()) {
            return;
        }

        BukkitProgressBar progressBar = createProgressBar(player, totalItems, "Converting ItemsAdder items", "items", ConverterOptions.ITEMS);
        progressBar.start();

        PluginNameMapper.getInstance().clearMappingsForPlugin(Plugins.ITEMS_ADDER);
        
        try {
            processItemFiles(toConvert, outputFolder, progressBar);
        } catch (Exception e) {
            Logger.showException("An error occurred during ItemsAdder item conversion", e);
        } finally {
            progressBar.stop();
        }
    }

    private void processItemFiles(Queue<ConfigFile> toConvert, File outputFolder, BukkitProgressBar progressBar) {
        while (!toConvert.isEmpty()) {
            ConfigFile configFile = toConvert.poll();
            convertItemsFile(configFile, outputFolder, progressBar);
        }
    }

    private void convertItemsFile(ConfigFile configFile, File outputFolder, BukkitProgressBar progressBar) {
        String fileName = configFile.sourceFile().getName();
        File itemFile = configFile.sourceFile();
        YamlConfiguration config = configFile.config();

        YamlConfiguration convertedConfig = new YamlConfiguration();
        String finalFileName = fileName.replace(".yml","");
        String namespace = config.getString("info.namespace", finalFileName);
        ConfigurationSection items = convertedConfig.createSection("items");
        ConfigurationSection originalItems = config.getConfigurationSection("items");
        if (isNull(originalItems)) {
            Logger.debug("[ItemsAdderConverter] No 'items' section found in: " + fileName);
            return;
        }

        List<String> itemsIds = new ArrayList<>();
        for (String itemId : originalItems.getKeys(false)){
            ConfigurationSection section = originalItems.getConfigurationSection(itemId);
            if (isNull(section)){
                Logger.debug("[ItemsAdderConverter] Skipped item (no section): " + itemId + " in file: " + fileName);
                progressBar.increment();
                continue;
            }
            String finalItemId = namespace + ":" + itemId;
            try {
                IAItemsConverter iaItemsConverter = new IAItemsConverter(
                        finalItemId,
                        items.createSection(finalItemId),
                        this,
                        convertedConfig,
                        section
                );
                iaItemsConverter.convertItem();

                if (!iaItemsConverter.isExcludeFromInventory()){
                    itemsIds.add(finalItemId);
                }
                PluginNameMapper.getInstance().storeMapping(Plugins.ITEMS_ADDER, itemId, finalItemId);
            } catch (Exception e) {
                Logger.showException("Failed to convert ItemsAdder item: " + itemId + " in file: " + fileName, e);
            }
            progressBar.increment();
        }
        generateCategorie(itemsIds, convertedConfig, finalFileName);
        if (this.settings.dryRunEnabled()) return;
        saveConvertedConfig(convertedConfig, configFile, itemFile, outputFolder, "items","item");
    }


    @Override
    public CompletableFuture<Void> convertEmojis(boolean async, Optional<Player> player) {
        return null;
    }

    @Override
    public CompletableFuture<Void> convertImages(boolean async, Optional<Player> player) {
        return null;
    }

    @Override
    public CompletableFuture<Void> convertLanguages(boolean async, Optional<Player> player) {
        return null;
    }

    @Override
    public CompletableFuture<Void> convertSounds(boolean async, Optional<Player> player) {
        return null;
    }

    @Override
    public CompletableFuture<Void> convertRecipes(boolean async, Optional<Player> player) {
        return null;
    }

    @Override
    public CompletableFuture<Void> convertPack(boolean async, Optional<Player> player) {
        return null;
    }

    protected int populateQueueIA(File baseDir, File currentDir, Queue<ConfigFile> toConvert) {
        int totalItems = 0;
        File[] listed = currentDir.listFiles();
        if (isNull(listed)) return 0;
        for (File f : listed){
            if (f.isDirectory()) {
                if (f.getName().equals("configs")) {
                    totalItems += addAllYmlFilesRecursively(f, baseDir, toConvert);
                }
                totalItems += populateQueueIA(baseDir, f, toConvert);
            }
        }
        return totalItems;
    }

    private int addAllYmlFilesRecursively(File dir, File baseDir, Queue<ConfigFile> toConvert) {
        int count = 0;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    count += addAllYmlFilesRecursively(f, baseDir, toConvert);
                } else if (f.isFile() && f.getName().endsWith(".yml")) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
                    ConfigurationSection itemsSection = config.getConfigurationSection("items");
                    if (itemsSection != null) {
                        toConvert.add(new ConfigFile(f, baseDir, config));
                        count += itemsSection.getKeys(false).size();
                    }
                }
            }
        }
        return count;
    }


}
