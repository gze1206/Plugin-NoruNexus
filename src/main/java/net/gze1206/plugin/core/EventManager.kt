package net.gze1206.plugin.core

import net.gze1206.plugin.Main
import net.gze1206.plugin.event.*
import org.bukkit.event.Listener

object EventManager {
    fun register() {
        Main.instance?.run {
            server.pluginManager.let {
                fun registerEvents(listener: Listener) {
                    it.registerEvents(listener, this)
                }

                registerEvents(PlayerJoinEvent)
                registerEvents(InventoryClickEvent)
                registerEvents(UserMoneyUpdateEvent)
                registerEvents(UserGetTitleEvent)
            }
        }
    }
}