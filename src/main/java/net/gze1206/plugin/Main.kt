package net.gze1206.plugin

import net.gze1206.plugin.core.Database
import net.gze1206.plugin.core.EventManager
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        val db = Database()
        var instance : Main? = null
    }

    override fun onEnable() {
        instance = this
        EventManager.register()
        db.connect()
        logger.info("플러그인 활성화!")
    }

    override fun onDisable() {
        db.close()
        logger.info("플러그인 비활성화")
    }
}