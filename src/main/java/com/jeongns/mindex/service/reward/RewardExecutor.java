package com.jeongns.mindex.service.reward;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RewardExecutor {
    @NonNull
    private final JavaPlugin plugin;

    public RewardExecutor(@NonNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void execute(@NonNull Player player, String rewardCommand) {
        if (rewardCommand == null || rewardCommand.isBlank()) {
            return;
        }

        String command = rewardCommand.replace("%player%", player.getName());
        boolean executed = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        if (!executed) {
            plugin.getLogger().warning("[Reward] command failed: " + command);
        }
    }
}
