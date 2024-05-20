package net.gze1206.noruNexus.core

import net.gze1206.noruNexus.Main
import net.gze1206.noruNexus.command.*
import org.bukkit.command.CommandExecutor

object CommandManager {
    fun register() {
        Main.getInstance().run {
            server.run {
                fun setPluginExecutor(name: String, executor: CommandExecutor) {
                    getPluginCommand(name)!!.setExecutor(executor)
                }

                setPluginExecutor("set-money", UserMoneyCommand)
                setPluginExecutor("nnreload", ReloadCommand)
                setPluginExecutor("칭호", TitleCommand)
                setPluginExecutor("출금", GetMoneyCommand)
                setPluginExecutor("상점", GlobalShopCommand)
                setPluginExecutor("룬", RuneCommand)
            }
        }
    }
}