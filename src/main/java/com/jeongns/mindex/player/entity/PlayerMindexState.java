package com.jeongns.mindex.player.entity;

import lombok.Getter;
import lombok.NonNull;

import java.util.Set;
import java.util.UUID;

@Getter
public final class PlayerMindexState {
    @NonNull
    private UUID playerUuid;
    @NonNull
    private Set<String> unlockedEntryIds;

    public PlayerMindexState(@NonNull UUID playerUuid, @NonNull Set<String> unlockedEntryIds) {
        this.playerUuid = playerUuid;
        this.unlockedEntryIds = unlockedEntryIds;
    }

    public boolean isUnlocked(String entryId) {
        return unlockedEntryIds.contains(entryId);
    }

    public boolean unlock(String entryId) {
        return unlockedEntryIds.add(entryId);
    }

    public int getUnlockedCount() {
        return unlockedEntryIds.size();
    }
}
