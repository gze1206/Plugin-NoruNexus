package net.gze1206.plugin

import net.gze1206.plugin.core.EventManager
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        var instance : Main? = null
    }

    override fun onEnable() {
        instance = this
        EventManager.register()
        logger.info("플러그인 활성화!")
    }

    override fun onDisable() {
        logger.info("플러그인 비활성화")
    }
}