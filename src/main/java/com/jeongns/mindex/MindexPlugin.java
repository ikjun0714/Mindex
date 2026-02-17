package com.jeongns.mindex;

import org.bukkit.plugin.java.JavaPlugin;

public final class MindexPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Mindex 플러그인이 시작되었습니다.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Mindex 플러그인이 종료되었습니다.");
    }
}
