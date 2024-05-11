package net.gze1206.noruNexus.core

import net.gze1206.noruNexus.Main
import net.gze1206.noruNexus.event.*
import org.bukkit.event.Listener

object EventManager {
    fun register() {
        Main.getInstance().run {
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