package net.gze1206.noruNexus.command

import net.gze1206.noruNexus.gui.ShopWindow
import net.gze1206.noruNexus.model.Shop
import net.gze1206.noruNexus.model.Shop.Companion.GLOBAL_ID
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object GlobalShopCommand : TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) : Boolean {
        if (sender !is Player) {
            return false
        }

        if (!Shop.has(GLOBAL_ID)) return false

        val window = ShopWindow(sender.player!!, Shop.get(GLOBAL_ID))
        window.update(1)
        window.open()
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>) : MutableList<String> {
        return mutableListOf()
    }
}