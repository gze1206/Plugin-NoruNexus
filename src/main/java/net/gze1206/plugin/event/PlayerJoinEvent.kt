package net.gze1206.plugin.event

import net.gze1206.plugin.core.UserManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinEvent : Listener {

    @EventHandler
    fun onPlayerJoinEvent(e: PlayerJoinEvent) {
        UserManager.addPlayer(e.player)
    }

}