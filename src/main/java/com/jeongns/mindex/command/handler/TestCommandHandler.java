package com.jeongns.mindex.command.handler;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.AllArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

@AllArgsConstructor
public final class TestCommandHandler implements CommandHandler {
    private final JavaPlugin plugin;

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> createSubcommand() {
        return Commands.literal("test")
                .executes(ctx -> {
                    execute(ctx.getSource(), new String[0]);
                    return Command.SINGLE_SUCCESS;
                });
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        plugin.getLogger().info("[Command] /mindex test executed by " + source.getSender().getName());
        source.getSender().sendPlainMessage("mindex test command executed.");
    }
}
