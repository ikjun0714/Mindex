package com.jeongns.mindex.command.handler;

import com.jeongns.mindex.MindexPlugin;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

@AllArgsConstructor
public final class MindexResetCommandHandler implements CommandHandler {
    @NonNull
    private final JavaPlugin plugin;

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("reset")
                .requires(this::canUse)
                .then(Commands.argument("username", StringArgumentType.word())
                        .executes(ctx -> {
                            execute(ctx.getSource(), new String[]{
                                    StringArgumentType.getString(ctx, "username")
                            });
                            return Command.SINGLE_SUCCESS;
                        }));
    }

    @Override
    public String permission() {
        return "mindex.command.reset";
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        if (!(plugin instanceof MindexPlugin mindexPlugin) || mindexPlugin.getPlayerStateManager() == null) {
            source.getSender().sendPlainMessage("플레이어 상태 매니저가 초기화되지 않았습니다.");
            plugin.getLogger().warning("[Command] /mindex reset failed: PlayerStateManager not initialized");
            return;
        }

        if (args.length == 0 || args[0].isBlank()) {
            source.getSender().sendPlainMessage("사용법: /mindex reset <username>");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);
        if (target == null || target.getUniqueId() == null) {
            source.getSender().sendPlainMessage("해당 유저를 찾을 수 없습니다: " + args[0]);
            return;
        }

        mindexPlugin.getPlayerStateManager().reset(target.getUniqueId());
        source.getSender().sendPlainMessage(target.getName() + "의 도감 기록을 초기화했습니다.");
        plugin.getLogger().info("[Command] /mindex reset executed for " + target.getName());
    }
}
