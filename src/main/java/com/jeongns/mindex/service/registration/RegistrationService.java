package com.jeongns.mindex.service.registration;

import com.jeongns.mindex.catalog.CatalogManager;
import com.jeongns.mindex.catalog.entity.MindexEntry;
import com.jeongns.mindex.catalog.entity.UnlockType;
import com.jeongns.mindex.player.PlayerStateManager;
import com.jeongns.mindex.service.reward.RewardExecutor;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class RegistrationService {
    @NonNull
    private final CatalogManager catalogManager;
    @NonNull
    private final PlayerStateManager playerStateManager;
    @NonNull
    private final RewardExecutor rewardExecutor;

    public RegistrationService(
            @NonNull CatalogManager catalogManager,
            @NonNull PlayerStateManager playerStateManager,
            @NonNull RewardExecutor rewardExecutor
    ) {
        this.catalogManager = catalogManager;
        this.playerStateManager = playerStateManager;
        this.rewardExecutor = rewardExecutor;
    }

    public RegistrationStatus register(@NonNull Player player, @NonNull String entryId) {
        Optional<MindexEntry> entryOptional = catalogManager.findEntry(entryId);
        if (entryOptional.isEmpty()) {
            return RegistrationStatus.ENTRY_NOT_FOUND;
        }

        MindexEntry entry = entryOptional.get();
        if (entry.getUnlockType() != UnlockType.ITEM) {
            return RegistrationStatus.UNSUPPORTED_UNLOCK_TYPE;
        }
        if (!canRegister(player, entry)) {
            return RegistrationStatus.REQUIREMENT_NOT_MET;
        }

        boolean unlocked = playerStateManager.unlock(player.getUniqueId(), entry.getId());
        if (!unlocked) {
            return RegistrationStatus.ALREADY_REGISTERED;
        }

        rewardExecutor.execute(player, entry.getReward());
        return RegistrationStatus.SUCCESS;
    }

    private boolean canRegister(@NonNull Player player, @NonNull MindexEntry entry) {
        ItemStack required = new ItemStack(entry.getItem(), 1);
        return player.getInventory().containsAtLeast(required, entry.getAmount());
    }
}
