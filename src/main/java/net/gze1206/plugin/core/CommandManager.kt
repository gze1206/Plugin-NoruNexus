package net.gze1206.plugin.core

import net.gze1206.plugin.Main
import net.gze1206.plugin.command.TitleCommand
import net.gze1206.plugin.command.UserMoneyCommand

object CommandManager {
    fun register() {
        Main.instance?.let {
            it.server.run {
                getPluginCommand("money")!!.setExecutor(UserMoneyCommand)
                getPluginCommand("title")!!.setExecutor(TitleCommand)
            }
        }
    }
}