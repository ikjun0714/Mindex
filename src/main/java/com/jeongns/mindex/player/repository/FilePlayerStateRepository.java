package com.jeongns.mindex.player.repository;

import com.jeongns.mindex.player.entity.PlayerMindexState;
import lombok.NonNull;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class FilePlayerStateRepository implements PlayerStateRepository {
    private static final String PLAYER_UUID_KEY = "player-uuid";
    private static final String UNLOCKED_ENTRY_IDS_KEY = "unlocked-entry-ids";
    private static final String CLAIMED_CATEGORY_REWARD_IDS_KEY = "claimed-category-reward-ids";

    @NonNull
    private final File playerStateDirectory;

    public FilePlayerStateRepository(@NonNull JavaPlugin plugin) {
        this.playerStateDirectory = new File(plugin.getDataFolder(), "data/player-state");
    }

    @Override
    public Optional<PlayerMindexState> findByPlayerId(@NonNull UUID playerId) {
        File stateFile = getStateFile(playerId);
        if (!stateFile.exists()) {
            return Optional.empty();
        }
        return Optional.of(loadState(stateFile, playerId));
    }

    @Override
    public void create(@NonNull UUID playerId) {
        File stateFile = getStateFile(playerId);
        if (stateFile.exists()) {
            return;
        }
        saveState(new PlayerMindexState(playerId, new HashSet<>(), new HashSet<>()));
    }

    @Override
    public boolean unlock(@NonNull UUID playerId, @NonNull String entryId) {
        return updateState(playerId, playerState -> playerState.unlock(entryId));
    }

    @Override
    public boolean claimCategoryReward(@NonNull UUID playerId, @NonNull String categoryId) {
        return updateState(playerId, playerState -> playerState.claimCategoryReward(categoryId));
    }

    @Override
    public void reset(@NonNull UUID playerId) {
        try {
            Files.deleteIfExists(getStateFile(playerId).toPath());
        } catch (IOException e) {
            throw new IllegalStateException("플레이어 상태 파일 삭제에 실패했습니다: " + playerId, e);
        }
    }

    private boolean updateState(@NonNull UUID playerId, @NonNull Function<PlayerMindexState, Boolean> changeAction) {
        PlayerMindexState playerState = findByPlayerId(playerId)
                .orElseGet(() -> new PlayerMindexState(playerId, new HashSet<>(), new HashSet<>()));

        boolean changed = changeAction.apply(playerState);
        if (!changed) {
            return false;
        }

        saveState(playerState);
        return true;
    }

    private PlayerMindexState loadState(@NonNull File stateFile, @NonNull UUID expectedPlayerId) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(stateFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new IllegalStateException("플레이어 상태 파일 로드에 실패했습니다: " + stateFile.getAbsolutePath(), e);
        }

        String playerUuidValue = config.getString(PLAYER_UUID_KEY, expectedPlayerId.toString());
        UUID playerId = parsePlayerId(playerUuidValue, stateFile);
        if (!playerId.equals(expectedPlayerId)) {
            throw new IllegalStateException("플레이어 상태 파일의 UUID가 파일명과 일치하지 않습니다: "
                    + stateFile.getAbsolutePath());
        }

        Set<String> unlockedEntryIds = new HashSet<>(config.getStringList(UNLOCKED_ENTRY_IDS_KEY));
        Set<String> claimedCategoryRewardIds = new HashSet<>(config.getStringList(CLAIMED_CATEGORY_REWARD_IDS_KEY));
        return new PlayerMindexState(playerId, unlockedEntryIds, claimedCategoryRewardIds);
    }

    private void saveState(@NonNull PlayerMindexState playerState) {
        ensureDirectoryExists();

        YamlConfiguration config = new YamlConfiguration();
        config.set(PLAYER_UUID_KEY, playerState.getPlayerUuid().toString());
        config.set(UNLOCKED_ENTRY_IDS_KEY, new java.util.ArrayList<>(playerState.getUnlockedEntryIds()));
        config.set(CLAIMED_CATEGORY_REWARD_IDS_KEY, new java.util.ArrayList<>(playerState.getClaimedCategoryRewardIds()));

        try {
            config.save(getStateFile(playerState.getPlayerUuid()));
        } catch (IOException e) {
            throw new IllegalStateException("플레이어 상태 파일 저장에 실패했습니다: " + playerState.getPlayerUuid(), e);
        }
    }

    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(playerStateDirectory.toPath());
        } catch (IOException e) {
            throw new IllegalStateException("플레이어 상태 디렉토리 생성에 실패했습니다: "
                    + playerStateDirectory.getAbsolutePath(), e);
        }
    }

    private File getStateFile(@NonNull UUID playerId) {
        return new File(playerStateDirectory, playerId + ".yml");
    }

    private UUID parsePlayerId(@NonNull String playerIdValue, @NonNull File stateFile) {
        try {
            return UUID.fromString(playerIdValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("플레이어 상태 파일의 UUID 값이 올바르지 않습니다: "
                    + stateFile.getAbsolutePath(), e);
        }
    }

}
