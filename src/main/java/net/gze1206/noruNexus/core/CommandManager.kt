package net.gze1206.noruNexus.core

import net.gze1206.noruNexus.Main
import net.gze1206.noruNexus.command.ReloadCommand
import net.gze1206.noruNexus.command.TitleCommand
import net.gze1206.noruNexus.command.UserMoneyCommand
import org.bukkit.command.CommandExecutor

object CommandManager {
    fun register() {
        Main.getInstance().run {
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