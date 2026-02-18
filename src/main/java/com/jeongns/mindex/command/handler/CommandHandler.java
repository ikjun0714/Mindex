package com.jeongns.mindex.command.handler;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface CommandHandler extends BasicCommand {
    LiteralArgumentBuilder<CommandSourceStack> createSubcommand();
}
