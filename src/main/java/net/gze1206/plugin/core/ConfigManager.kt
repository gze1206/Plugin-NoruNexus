package net.gze1206.plugin.core

import net.gze1206.plugin.Main
import net.gze1206.plugin.config.ConfigFile
import net.gze1206.plugin.model.Title

object ConfigManager {
    private val PATH = Main.instance!!.dataFolder.absolutePath

    val title : ConfigFile = ConfigFile(PATH, "title.yml")

    fun init() {
        Title.initConfig()
    }

    fun reloadAll() {
        title.reload()
    }

    fun saveAll() {
        title.save()
    }
}