package net.gze1206.noruNexus.event

import net.gze1206.noruNexus.core.Constants.ITEM_TYPE_KEY
import net.gze1206.noruNexus.core.Constants.MONEY_UNIT_KEY
import net.gze1206.noruNexus.core.ItemType
import net.gze1206.noruNexus.core.UserManager
import net.gze1206.noruNexus.utils.updateScoreboard
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

object PlayerInteractEvent : Listener {

    @EventHandler
    fun onEvent(e: PlayerInteractEvent) {
        if (!e.hasItem()) return

        val item = e.item!!
        val itemMeta = item.itemMeta

        when (itemMeta.persistentDataContainer.get(ITEM_TYPE_KEY, PersistentDataType.STRING)) {
            ItemType.MONEY.name -> useMoney(e.player, item, itemMeta)

            null -> return
            else -> throw NotImplementedError()
        }

    }

    private fun useMoney(player: Player, item: ItemStack, itemMeta: ItemMeta) {
        val unit = itemMeta.persistentDataContainer.get(MONEY_UNIT_KEY, PersistentDataType.INTEGER)!!
        val amount = item.amount * unit

        val succeed = UserManager.getUser(player).transaction {
            money += amount
        }

        if (!succeed) {
            player.sendMessage("입금에 실패했습니다.")
            return
        }

        UserManager.getUser(player).run {
            player.updateScoreboard(this)
            player.inventory.removeItem(item)
        }
    }

}