package com.jeongns.mindex.command;

import com.jeongns.mindex.command.handler.CommandHandler;
import com.jeongns.mindex.command.handler.TestCommandHandler;
import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;


public final class CommandManager {
    private final JavaPlugin plugin;


    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadCommands() {
        var handlers = List.of(
                new TestCommandHandler(plugin)
        );

        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            var root = Commands.literal("mindex")
                    .executes(ctx -> {
                        plugin.getLogger().info("[Command] /mindex executed by " + ctx.getSource().getSender().getName());
                        ctx.getSource().getSender().sendPlainMessage("mindex root command executed.");
                        return Command.SINGLE_SUCCESS;
                    });

            for (CommandHandler handler : handlers) {
                root.then(handler.createSubcommand());
            }

            commands.registrar().register(root.build(), "Mindex root command");
        });


    }
}
