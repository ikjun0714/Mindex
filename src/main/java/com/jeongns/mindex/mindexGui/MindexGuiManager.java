package com.jeongns.mindex.mindexGui;

import com.jeongns.mindex.catalog.CatalogManager;
import com.jeongns.mindex.manager.Manager;
import com.jeongns.mindex.mindexGui.loader.GuiConfigLoader;
import com.jeongns.mindex.mindexGui.model.GuiSettings;
import com.jeongns.mindex.mindexGui.model.GuiModel;
import com.jeongns.mindex.mindexGui.model.GuiSoundSettings;
import com.jeongns.mindex.mindexGui.model.LockedEntryDisplay;
import com.jeongns.mindex.mindexGui.view.MindexCatalogGui;
import com.jeongns.mindex.player.PlayerStateManager;
import com.jeongns.mindex.service.registration.RegistrationService;
import com.jeongns.mindex.service.reward.CategoryRewardService;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.java.JavaPlugin;

public class MindexGuiManager implements Manager {
    @NonNull
    private final JavaPlugin plugin;
    @NonNull
    private final CatalogManager catalogManager;
    @NonNull
    private final PlayerStateManager playerStateManager;
    @NonNull
    private final RegistrationService registrationService;
    @NonNull
    private final CategoryRewardService categoryRewardService;
    @NonNull
    private final GuiConfigLoader configLoader;

    @Getter
    @NonNull
    private GuiModel guiModel;
    @Getter
    @NonNull
    private LockedEntryDisplay lockedEntryDisplay;
    @Getter
    @NonNull
    private GuiSoundSettings guiSoundSettings;

    public MindexGuiManager(
            @NonNull JavaPlugin plugin,
            @NonNull CatalogManager catalogManager,
            @NonNull PlayerStateManager playerStateManager,
            @NonNull RegistrationService registrationService,
            @NonNull CategoryRewardService categoryRewardService
    ) {
        this.plugin = plugin;
        this.catalogManager = catalogManager;
        this.playerStateManager = playerStateManager;
        this.registrationService = registrationService;
        this.categoryRewardService = categoryRewardService;
        this.configLoader = new GuiConfigLoader(plugin);
        this.guiModel = GuiModel.empty();
        this.lockedEntryDisplay = LockedEntryDisplay.defaultValue();
        this.guiSoundSettings = GuiSoundSettings.defaultValue();
    }

    @Override
    public void initialize() {
        reload();
    }

    @Override
    public void reload() {
        applyGuiSettings(configLoader.load());
    }

    public void applyGuiModel(@NonNull GuiModel guiModel) {
        this.guiModel = guiModel;
    }

    public void applyLockedEntryDisplay(@NonNull LockedEntryDisplay lockedEntryDisplay) {
        this.lockedEntryDisplay = lockedEntryDisplay;
    }

    public void applyGuiSoundSettings(@NonNull GuiSoundSettings guiSoundSettings) {
        this.guiSoundSettings = guiSoundSettings;
    }

    public void applyGuiSettings(@NonNull GuiSettings guiSettings) {
        applyGuiModel(guiSettings.getGuiModel());
        applyLockedEntryDisplay(guiSettings.getLockedEntryDisplay());
        applyGuiSoundSettings(guiSettings.getGuiSoundSettings());
    }

    public void openDefault(@NonNull Player player) {
        new MindexCatalogGui(
                player.getUniqueId(),
                catalogManager.getCatalog(),
                guiModel,
                lockedEntryDisplay,
                guiSoundSettings,
                playerStateManager,
                registrationService,
                categoryRewardService
        ).open(player);
    }

    public void openCategory(@NonNull Player player, @NonNull String categoryId) {
        MindexCatalogGui gui = new MindexCatalogGui(
                player.getUniqueId(),
                catalogManager.getCatalog(),
                guiModel,
                lockedEntryDisplay,
                guiSoundSettings,
                playerStateManager,
                registrationService,
                categoryRewardService
        );
        gui.setCategory(categoryId);
        gui.open(player);
    }

    public void handleOpen(@NonNull Player player, @NonNull MindexCatalogGui gui) {
        // Inventory open hook for GUI session lifecycle.
    }

    public void handleTopInventoryClick(
            @NonNull Player player,
            @NonNull MindexCatalogGui gui,
            int rawSlot,
            @NonNull ClickType clickType
    ) {
        gui.handleClick(player, rawSlot, clickType);
    }

    public void handleClose(@NonNull Player player, @NonNull MindexCatalogGui gui) {
        // Inventory close hook for GUI session lifecycle.
    }

    @Override
    public void shutdown() {
        // no-op
    }
}
