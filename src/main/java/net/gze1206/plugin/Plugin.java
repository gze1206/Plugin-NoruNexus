package net.gze1206.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("플러그인 활성화!");
    }

    @Override
    public void onDisable() {
        getLogger().info("플러그인 비활성화");
    }
}
