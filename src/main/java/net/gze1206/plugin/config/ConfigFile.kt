package net.gze1206.plugin.config

import net.gze1206.plugin.Main
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import kotlin.io.path.Path

class ConfigFile(path: String, fileName: String) {
    private val file : File
    private var config : FileConfiguration

    init {
        file = File(Path(path, fileName).toString())
        config = YamlConfiguration.loadConfiguration(file)
    }

    fun save() {
        try {
            config.save(file)
        } catch (e : Exception) {
            Main.log!!.severe("${e.message}\n${e.stackTraceToString()}")
        }
    }

    fun reload() {
        if (!file.exists()) return
        config = YamlConfiguration.loadConfiguration(file)
    }

    fun getConfig() = config
}