package net.gze1206.noruNexus

import net.gze1206.noruNexus.core.CommandManager
import net.gze1206.noruNexus.core.ConfigManager
import net.gze1206.noruNexus.core.Database
import net.gze1206.noruNexus.core.EventManager
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class Main : JavaPlugin() {
    companion object {
        private var instance : Main? = null
        fun getInstance() = instance!!

        private var log : Logger? = null
        fun getLog() = log!!
    }

    override fun onEnable() {
        instance = this
        log = this.logger
        EventManager.register()
        CommandManager.register()
        ConfigManager.init()
        ConfigManager.saveAll()
        Database.init()
        _2025_NewYear.schedule()
        logger.info("플러그인 활성화!")
    }

    override fun onDisable() {
        logger.info("플러그인 비활성화")
    }
}