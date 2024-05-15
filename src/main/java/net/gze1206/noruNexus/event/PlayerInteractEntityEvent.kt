package net.gze1206.noruNexus.event

import io.lumine.mythic.bukkit.MythicBukkit
import net.gze1206.noruNexus.Main
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import kotlin.jvm.optionals.getOrNull

object PlayerInteractEntityEvent : Listener {

    @EventHandler
    fun onPlayerInteractEvent(e: PlayerInteractEntityEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        val mob = MythicBukkit.inst().mobManager.getActiveMob(e.rightClicked.uniqueId)?.getOrNull() ?: return

        e.isCancelled = true
        Main.getLog().info("${e.player.name} tried to interact with MythicMobs ${mob.name}")
    }
}