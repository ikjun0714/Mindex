package com.jeongns.mindex.catalog;

import com.jeongns.mindex.catalog.entity.MindexCatalog;
import com.jeongns.mindex.catalog.entity.MindexCategory;
import com.jeongns.mindex.catalog.entity.MindexEntry;
import com.jeongns.mindex.catalog.loader.CatalogConfigLoader;
import com.jeongns.mindex.manager.Manager;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public class CatalogManager implements Manager {
    @NonNull
    private final CatalogConfigLoader configLoader;

    @Getter
    private MindexCatalog catalog = new MindexCatalog(List.of());

    public CatalogManager(@NonNull CatalogConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    @Override
    public void initialize() {
        this.catalog = new MindexCatalog(configLoader.loadCategories());
    }

    @Override
    public void reload() {
        this.catalog = new MindexCatalog(configLoader.loadCategories());
    }

    public List<MindexCategory> getCategories() {
        return catalog.getCategories();
    }

    public Optional<MindexCategory> findCategory(@NonNull String categoryId) {
        return catalog.getCategories().stream()
                .filter(category -> category.getId().equalsIgnoreCase(categoryId))
                .findFirst();
    }

    public Optional<MindexEntry> findEntry(@NonNull String entryId) {
        return catalog.getCategories().stream()
                .flatMap(category -> category.getEntries().stream())
                .filter(entry -> entry.getId().equalsIgnoreCase(entryId))
                .findFirst();
    }
}
