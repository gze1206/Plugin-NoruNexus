package net.gze1206.plugin.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object PlayerQuitEvent : Listener {

    @EventHandler
    fun onPlayerQuitEvent(e: PlayerQuitEvent) {
    }

}