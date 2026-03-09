package com.jeongns.mindex.player.repository.factory;

import com.jeongns.mindex.player.repository.PlayerStateRepository;
import com.jeongns.mindex.player.repository.impl.FilePlayerStateRepository;
import com.jeongns.mindex.player.repository.impl.InMemoryPlayerStateRepository;
import com.jeongns.mindex.player.repository.impl.MySqlPlayerStateRepository;
import com.jeongns.mindex.player.repository.impl.PostgresPlayerStateRepository;

import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerStateRepositoryFactory {
    @NonNull
    private final JavaPlugin plugin;

    public PlayerStateRepositoryFactory(@NonNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public PlayerStateRepository create() {
        String configuredType = plugin.getConfig().getString("player-state-storage", PlayerStateRepositoryType.FILE.name());
        PlayerStateRepositoryType repositoryType = PlayerStateRepositoryType.fromConfig(configuredType);

        return switch (repositoryType) {
            case FILE -> new FilePlayerStateRepository(plugin);
            case IN_MEMORY -> new InMemoryPlayerStateRepository();
            case POSTGRESQL -> new PostgresPlayerStateRepository(plugin);
            case MYSQL -> new MySqlPlayerStateRepository(plugin);
        };
    }
}
