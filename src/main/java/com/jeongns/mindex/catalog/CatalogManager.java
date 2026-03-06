package com.jeongns.mindex.catalog;

import com.jeongns.mindex.catalog.entity.MindexCatalog;
import com.jeongns.mindex.catalog.entity.MindexCategory;
import com.jeongns.mindex.catalog.entity.MindexEntry;
import com.jeongns.mindex.catalog.loader.CatalogConfigLoader;
import com.jeongns.mindex.manager.Manager;
import lombok.Getter;
import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class CatalogManager implements Manager {
    @NonNull
    private final CatalogConfigLoader configLoader;

    @Getter
    private MindexCatalog catalog = new MindexCatalog(List.of());
    @NonNull
    private Map<String, MindexCategory> categoryIndex = Map.of();
    @NonNull
    private Map<String, MindexEntry> entryIndex = Map.of();

    public CatalogManager(@NonNull CatalogConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    @Override
    public void initialize() {
        applyCatalog(configLoader.loadCategories());
    }

    @Override
    public void reload() {
        applyCatalog(configLoader.loadCategories());
    }

    public List<MindexCategory> getCategories() {
        return catalog.getCategories();
    }

    public Optional<MindexCategory> findCategory(@NonNull String categoryId) {
        return Optional.ofNullable(categoryIndex.get(categoryId.toLowerCase(Locale.ROOT)));
    }

    public Optional<MindexEntry> findEntry(@NonNull String entryId) {
        return Optional.ofNullable(entryIndex.get(entryId.toLowerCase(Locale.ROOT)));
    }

    private void applyCatalog(@NonNull List<MindexCategory> categories) {
        this.catalog = new MindexCatalog(categories);
        buildIndexes(categories);
    }

    private void buildIndexes(@NonNull List<MindexCategory> categories) {
        Map<String, MindexCategory> nextCategoryIndex = new LinkedHashMap<>();
        Map<String, MindexEntry> nextEntryIndex = new LinkedHashMap<>();

        for (MindexCategory category : categories) {
            nextCategoryIndex.put(category.getId().toLowerCase(Locale.ROOT), category);
            for (MindexEntry entry : category.getEntries()) {
                nextEntryIndex.put(entry.getId().toLowerCase(Locale.ROOT), entry);
            }
        }

        this.categoryIndex = Map.copyOf(nextCategoryIndex);
        this.entryIndex = Map.copyOf(nextEntryIndex);
    }
}
