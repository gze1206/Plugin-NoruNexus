package net.gze1206.plugin

import net.gze1206.plugin.core.CommandManager
import net.gze1206.plugin.core.Database
import net.gze1206.plugin.core.EventManager
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class Main : JavaPlugin() {
    companion object {
        val db = Database()
        var instance : Main? = null
        var log : Logger? = null
    }

    override fun onEnable() {
        instance = this
        log = this.logger
        EventManager.register()
        CommandManager.register()
        db.connect()
        logger.info("플러그인 활성화!")
    }

    override fun onDisable() {
        db.close()
        logger.info("플러그인 비활성화")
    }
}