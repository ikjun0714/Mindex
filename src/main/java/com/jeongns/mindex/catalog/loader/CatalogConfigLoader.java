package com.jeongns.mindex.catalog.loader;

import com.jeongns.mindex.catalog.entity.MindexCategory;
import com.jeongns.mindex.catalog.entity.MindexEntry;
import com.jeongns.mindex.config.validation.ConfigValueValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CatalogConfigLoader {
    private static final String CATEGORY_LIST_PATH = "catalog.categories";
    private static final String ENTRY_LIST_PATH = "entries";

    @NonNull
    private final JavaPlugin plugin;

    public List<MindexCategory> loadCategories() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        return plugin.getConfig().getMapList(CATEGORY_LIST_PATH).stream()
                .map(this::toCategory)
                .collect(Collectors.toList());
    }

    private MindexCategory toCategory(@NonNull Map<?, ?> row) {
        String categoryId = ConfigValueValidator.requireString(valueAsString(row.get("id")), "catalog.categories.id");
        String categoryName = ConfigValueValidator.requireString(valueAsString(row.get("name")), "catalog.categories.name");
        String categoryFilePath = ConfigValueValidator.requireString(valueAsString(row.get("file")), "catalog.categories.file");
        String categoryReward = ConfigValueValidator.optionalString(valueAsString(row.get("reward")), "");

        ensureCategoryFile(categoryFilePath);
        List<MindexEntry> entries = loadEntries(categoryFilePath);

        return new MindexCategory(categoryId, categoryName, categoryReward, entries);
    }

    private void ensureCategoryFile(@NonNull String categoryFilePath) {
        File categoryFile = new File(plugin.getDataFolder(), categoryFilePath);
        if (!categoryFile.exists()) {
            plugin.saveResource(categoryFilePath, false);
        }
    }

    private List<MindexEntry> loadEntries(@NonNull String categoryFilePath) {
        File categoryFile = new File(plugin.getDataFolder(), categoryFilePath);
        YamlConfiguration categoryConfig = YamlConfiguration.loadConfiguration(categoryFile);

        return categoryConfig.getMapList(ENTRY_LIST_PATH).stream()
                .map(this::toEntry)
                .collect(Collectors.toList());
    }

    private MindexEntry toEntry(@NonNull Map<?, ?> row) {
        return new MindexEntry(
                ConfigValueValidator.requireString(valueAsString(row.get("id")), "entries.id"),
                ConfigValueValidator.requireString(valueAsString(row.get("name")), "entries.name"),
                ConfigValueValidator.requireString(valueAsString(row.get("description")), "entries.description"),
                ConfigValueValidator.parseMaterial(
                        ConfigValueValidator.requireString(valueAsString(row.get("material")), "entries.material")
                ),
                ConfigValueValidator.optionalString(valueAsString(row.get("reward")), "")
        );
    }

    private String valueAsString(Object value) {
        return value instanceof String stringValue ? stringValue : null;
    }
}
