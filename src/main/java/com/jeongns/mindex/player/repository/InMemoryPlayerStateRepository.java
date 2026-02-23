package com.jeongns.mindex.player.repository;

import com.jeongns.mindex.player.entity.PlayerMindexState;
import lombok.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPlayerStateRepository implements PlayerStateRepository {
    private final Map<UUID, PlayerMindexState> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<PlayerMindexState> findByPlayerId(@NonNull UUID playerId) {
        return Optional.ofNullable(storage.get(playerId));
    }

    @Override
    public void save(@NonNull PlayerMindexState playerState) {
        storage.put(playerState.getPlayerUuid(), playerState);
    }

    @Override
    public void saveAll(@NonNull Collection<PlayerMindexState> playerStates) {
        for (PlayerMindexState playerState : playerStates) {
            save(playerState);
        }
    }

    @Override
    public void deleteByPlayerId(@NonNull UUID playerId) {
        storage.remove(playerId);
    }
}
