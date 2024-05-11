package net.gze1206.noruNexus.event

import net.gze1206.noruNexus.core.UserManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinEvent : Listener {

    @EventHandler
    fun onPlayerJoinEvent(e: PlayerJoinEvent) {
        UserManager.addPlayer(e.player)
    }

}