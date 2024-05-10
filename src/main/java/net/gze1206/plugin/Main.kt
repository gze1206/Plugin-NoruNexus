package net.gze1206.plugin

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    override fun onEnable() {
        logger.info("플러그인 활성화!")
    }

    override fun onDisable() {
        logger.info("플러그인 비활성화")
    }
}