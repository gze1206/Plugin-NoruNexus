package net.gze1206.noruNexus.core

import net.gze1206.noruNexus.Main
import net.gze1206.noruNexus.config.ConfigFile
import net.gze1206.noruNexus.config.RuneConfig
import net.gze1206.noruNexus.model.Shop
import net.gze1206.noruNexus.model.Title

object ConfigManager {
    private val PATH = Main.getInstance().dataFolder.absolutePath

    val config = Main.getInstance().config
    val title = ConfigFile(PATH, "title.yml")
    val rune = RuneConfig(PATH, "rune.yml")
    val shop = ConfigFile(PATH, "shop.yml")

    fun init() {
        config.run {
            options().copyDefaults(true)

            addDefault("title-trigger.${Title.TitleId.RICH}", 100)
            addDefault("title-trigger.${Title.TitleId.RIICH}", 10000)
            addDefault("title-trigger.${Title.TitleId.RIIICH}", 1000000)
        }

        Title.initConfig()
        rune.init()
        Shop.initConfig()
    }

    fun reloadAll() {
        Main.getInstance().reloadConfig()
        title.reload()
        rune.reload()
        shop.reload()
    }

    fun saveAll() {
        Main.getInstance().saveConfig()
        title.save()
        rune.save()
        shop.save()
    }
}