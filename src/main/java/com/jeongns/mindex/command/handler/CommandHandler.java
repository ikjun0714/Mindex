package com.jeongns.mindex.command.handler;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.Nullable;

public interface CommandHandler {
    LiteralArgumentBuilder<CommandSourceStack> createCommand();

    void execute(CommandSourceStack var1, String[] var2);

    default @Nullable String permission() {
        return null;
    }

    default boolean canUse(CommandSourceStack source) {
        String permission = permission();
        return permission == null || source.getSender().hasPermission(permission);
    }
}
