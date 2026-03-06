package com.jeongns.mindex.service.reward;

import com.jeongns.mindex.catalog.CatalogManager;
import com.jeongns.mindex.catalog.entity.MindexCategory;
import com.jeongns.mindex.catalog.entity.MindexEntry;
import com.jeongns.mindex.player.PlayerStateManager;
import com.jeongns.mindex.player.entity.PlayerMindexState;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CategoryRewardService {
    @NonNull
    private final CatalogManager catalogManager;
    @NonNull
    private final PlayerStateManager playerStateManager;
    @NonNull
    private final RewardExecutor rewardExecutor;

    public CategoryRewardService(
            @NonNull CatalogManager catalogManager,
            @NonNull PlayerStateManager playerStateManager,
            @NonNull RewardExecutor rewardExecutor
    ) {
        this.catalogManager = catalogManager;
        this.playerStateManager = playerStateManager;
        this.rewardExecutor = rewardExecutor;
    }

    public CategoryRewardStatus claim(@NonNull Player player, @NonNull String categoryId) {
        Optional<MindexCategory> categoryOptional = catalogManager.findCategory(categoryId);
        if (categoryOptional.isEmpty()) {
            return CategoryRewardStatus.CATEGORY_NOT_FOUND;
        }

        MindexCategory category = categoryOptional.get();
        PlayerMindexState playerState = playerStateManager.getOrCreate(player.getUniqueId());
        if (!isCategoryCompleted(category, playerState)) {
            return CategoryRewardStatus.CATEGORY_NOT_COMPLETE;
        }
        if (playerState.hasClaimedCategoryReward(category.getId())) {
            return CategoryRewardStatus.ALREADY_CLAIMED;
        }

        rewardExecutor.execute(player, category.getReward());
        playerStateManager.claimCategoryReward(player.getUniqueId(), category.getId());
        return CategoryRewardStatus.SUCCESS;
    }

    private boolean isCategoryCompleted(@NonNull MindexCategory category, @NonNull PlayerMindexState playerState) {
        for (MindexEntry entry : category.getEntries()) {
            if (!playerState.isUnlocked(entry.getId())) {
                return false;
            }
        }
        return true;
    }
}
