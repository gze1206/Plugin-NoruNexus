package net.gze1206.plugin.core

import net.gze1206.plugin.Main
import net.gze1206.plugin.event.PlayerJoinEvent
import net.gze1206.plugin.event.PlayerKickEvent
import net.gze1206.plugin.event.PlayerQuitEvent

object EventManager {
    fun register() {
        Main.instance?.let {
            it.server.pluginManager.apply {
                registerEvents(PlayerJoinEvent, it)
                registerEvents(PlayerKickEvent, it)
                registerEvents(PlayerQuitEvent, it)
            }
        }
    }
}