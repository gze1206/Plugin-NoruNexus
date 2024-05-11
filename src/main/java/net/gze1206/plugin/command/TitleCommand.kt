package net.gze1206.plugin.command

import net.gze1206.plugin.gui.TitleWindow
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object TitleCommand : CommandExecutor, TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?) : Boolean {
        if (sender !is Player) {
            return false
        }
        val window = TitleWindow(sender.player!!)
        window.update(1)
        window.open()
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?) : MutableList<String> {
        return mutableListOf()
    }
}