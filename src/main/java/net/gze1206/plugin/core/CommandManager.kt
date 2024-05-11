package net.gze1206.plugin.core

import net.gze1206.plugin.Main
import net.gze1206.plugin.command.ReloadCommand
import net.gze1206.plugin.command.TitleCommand
import net.gze1206.plugin.command.UserMoneyCommand
import org.bukkit.command.CommandExecutor

object CommandManager {
    fun register() {
        Main.instance?.run {
            server.run {
                fun setPluginExecutor(name: String, executor: CommandExecutor) {
                    getPluginCommand(name)!!.setExecutor(executor)
                }

                setPluginExecutor("money", UserMoneyCommand)
                setPluginExecutor("reload-config", ReloadCommand)
                setPluginExecutor("title", TitleCommand)
            }
        }
    }
}