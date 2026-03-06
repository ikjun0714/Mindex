package com.jeongns.mindex.service.reward;

import com.jeongns.mindex.catalog.CatalogManager;
import com.jeongns.mindex.catalog.entity.MindexCategory;
import com.jeongns.mindex.catalog.entity.MindexEntry;
import com.jeongns.mindex.player.PlayerStateManager;
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
        if (!isCategoryCompleted(player, category)) {
            return CategoryRewardStatus.CATEGORY_NOT_COMPLETE;
        }
        if (!playerStateManager.claimCategoryReward(player.getUniqueId(), category.getId())) {
            return CategoryRewardStatus.ALREADY_CLAIMED;
        }

        rewardExecutor.execute(player, category.getReward());
        return CategoryRewardStatus.SUCCESS;
    }

    private boolean isCategoryCompleted(@NonNull Player player, @NonNull MindexCategory category) {
        for (MindexEntry entry : category.getEntries()) {
            if (!playerStateManager.isUnlocked(player.getUniqueId(), entry.getId())) {
                return false;
            }
        }
        return true;
    }
}
