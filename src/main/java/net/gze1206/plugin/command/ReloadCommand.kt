package net.gze1206.plugin.command

import net.gze1206.plugin.core.ConfigManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

object ReloadCommand : CommandExecutor, TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?) : Boolean {
        ConfigManager.reloadAll()
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?) : MutableList<String> {
        return mutableListOf()
    }
}