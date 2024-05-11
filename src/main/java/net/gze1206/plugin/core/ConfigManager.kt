package net.gze1206.plugin.core

import net.gze1206.plugin.Main
import net.gze1206.plugin.config.ConfigFile
import net.gze1206.plugin.event.UserMoneyUpdateEvent
import net.gze1206.plugin.model.Title
import org.bukkit.configuration.file.FileConfiguration

object ConfigManager {
    private val PATH = Main.instance!!.dataFolder.absolutePath

    val config : FileConfiguration = Main.instance!!.config
    val title : ConfigFile = ConfigFile(PATH, "title.yml")

    fun init() {
        config.options().copyDefaults(true)
        config.addDefault("title-trigger.${UserMoneyUpdateEvent.RICH}", 100)
        config.addDefault("title-trigger.${UserMoneyUpdateEvent.RIICH}", 10000)
        config.addDefault("title-trigger.${UserMoneyUpdateEvent.RIIICH}", 1000000)

        Title.initConfig()
    }

    fun reloadAll() {
        Main.instance!!.reloadConfig()
        title.reload()
    }

    fun saveAll() {
        Main.instance!!.saveConfig()
        title.save()
    }
}