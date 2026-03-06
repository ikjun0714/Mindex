package com.jeongns.mindex.mindexGui.model;

import lombok.Getter;
import lombok.NonNull;

@Getter
public final class GuiSettings {
    @NonNull
    private final GuiModel guiModel;
    @NonNull
    private final LockedEntryDisplay lockedEntryDisplay;
    @NonNull
    private final GuiSoundSettings guiSoundSettings;

    public GuiSettings(
            @NonNull GuiModel guiModel,
            @NonNull LockedEntryDisplay lockedEntryDisplay,
            @NonNull GuiSoundSettings guiSoundSettings
    ) {
        this.guiModel = guiModel;
        this.lockedEntryDisplay = lockedEntryDisplay;
        this.guiSoundSettings = guiSoundSettings;
    }

    public static GuiSettings defaultValue() {
        return new GuiSettings(
                GuiModel.empty(),
                LockedEntryDisplay.defaultValue(),
                GuiSoundSettings.defaultValue()
        );
    }
}
