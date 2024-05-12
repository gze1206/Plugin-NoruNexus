package net.gze1206.noruNexus.event

import net.gze1206.noruNexus.core.Constants.MONEY_UNIT_KEY
import net.gze1206.noruNexus.core.UserManager
import net.gze1206.noruNexus.utils.updateScoreboard
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

object PlayerInteractEvent : Listener {

    @EventHandler
    fun onEvent(e: PlayerInteractEvent) {
        if (!e.hasItem()) return

        val item = e.item!!
        val itemMeta = item.itemMeta
        if (item.type != Material.GOLD_NUGGET || itemMeta.customModelData != 1) {
            return
        }

        val unit = itemMeta.persistentDataContainer.get(MONEY_UNIT_KEY, PersistentDataType.INTEGER)!!
        val amount = e.item!!.amount * unit

        val succeed = UserManager.getUser(e.player).transaction {
            money += amount
        }

        if (!succeed) {
            e.player.sendMessage("입금에 실패했습니다.")
            return
        }

        UserManager.getUser(e.player).run {
            e.player.updateScoreboard(this)
            e.player.inventory.removeItem(item)
        }
    }

}