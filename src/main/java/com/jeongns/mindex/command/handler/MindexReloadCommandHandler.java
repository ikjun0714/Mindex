package com.jeongns.mindex.command.handler;

import com.jeongns.mindex.MindexPlugin;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

@AllArgsConstructor
public final class MindexReloadCommandHandler implements CommandHandler {
    @NonNull
    private final JavaPlugin plugin;

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("reload")
                .requires(this::canUse)
                .executes(ctx -> {
                    execute(ctx.getSource(), new String[0]);
                    return Command.SINGLE_SUCCESS;
                });
    }

    @Override
    public String permission() {
        return "mindex.command.reload";
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        if (!(plugin instanceof MindexPlugin mindexPlugin)) {
            source.getSender().sendPlainMessage("플러그인 인스턴스를 찾을 수 없습니다.");
            return;
        }

        try {
            mindexPlugin.reloadPlugin();
            source.getSender().sendPlainMessage("Mindex 설정과 데이터를 다시 불러왔습니다.");
            plugin.getLogger().info("[Command] /mindex reload executed by " + source.getSender().getName());
        } catch (Exception e) {
            source.getSender().sendPlainMessage("Mindex 리로드에 실패했습니다. 콘솔 로그를 확인하세요.");
            plugin.getLogger().severe("[Command] /mindex reload failed: " + e.getMessage());
            throw e;
        }
    }
}
