package net.gze1206.noruNexus.event

import net.gze1206.noruNexus.core.UserManager
import net.gze1206.noruNexus.model.Title
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

object PlayerTeleportEvent : Listener {

    @EventHandler
    fun onPlayerTeleportEvent(e: PlayerTeleportEvent) {
        if (e.to.world.environment != World.Environment.NETHER) {
            return
        }

        UserManager.getUser(e.player).run {
            Title.give(this, Title.TitleId.HELLDIVER)
        }
    }

}