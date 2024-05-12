package net.gze1206.noruNexus.core

import net.gze1206.noruNexus.Main
import net.gze1206.noruNexus.config.ConfigFile
import net.gze1206.noruNexus.model.Title
import org.bukkit.configuration.file.FileConfiguration

object ConfigManager {
    private val PATH = Main.getInstance().dataFolder.absolutePath

    val config : FileConfiguration = Main.getInstance().config
    val title : ConfigFile = ConfigFile(PATH, "title.yml")

    fun init() {
        config.run {
            options().copyDefaults(true)

            addDefault("title-trigger.${Title.TitleId.RICH}", 100)
            addDefault("title-trigger.${Title.TitleId.RIICH}", 10000)
            addDefault("title-trigger.${Title.TitleId.RIIICH}", 1000000)
        }

        Title.initConfig()
    }

    fun reloadAll() {
        Main.getInstance().reloadConfig()
        title.reload()
    }

    fun saveAll() {
        Main.getInstance().saveConfig()
        title.save()
    }
}