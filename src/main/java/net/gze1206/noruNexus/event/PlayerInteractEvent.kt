package net.gze1206.noruNexus.event

import net.gze1206.noruNexus.core.Constants.ITEM_TYPE_KEY
import net.gze1206.noruNexus.core.Constants.MONEY_AMOUNT_KEY
import net.gze1206.noruNexus.core.ItemType
import net.gze1206.noruNexus.core.UserManager
import net.gze1206.noruNexus.utils.removeItem
import net.gze1206.noruNexus.utils.updateScoreboard
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

object PlayerInteractEvent : Listener {

    @EventHandler
    fun onEvent(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (!e.hasItem()) return

        val item = e.item!!
        val itemMeta = item.itemMeta

        e.isCancelled = when (itemMeta.persistentDataContainer.get(ITEM_TYPE_KEY, PersistentDataType.STRING)) {
            ItemType.MONEY.name -> useMoney(e.player, item, itemMeta)
            ItemType.RECALL_SCROLL.name -> useRecallScroll(e.player, item)

            null -> false
            else -> throw NotImplementedError()
        }

    }

    private fun useMoney(player: Player, item: ItemStack, itemMeta: ItemMeta) : Boolean {
        val amount = itemMeta.persistentDataContainer.get(MONEY_AMOUNT_KEY, PersistentDataType.LONG)!!

        val succeed = UserManager.getUser(player).transaction {
            money += amount
        }

        if (!succeed) {
            player.sendMessage("입금에 실패했습니다.")
            return false
        }

        UserManager.getUser(player).run {
            player.updateScoreboard(this)
            player.inventory.removeItem(item)
            player.sendMessage("${amount}원을 입금했습니다.")
        }

        return true
    }

    private fun useRecallScroll(player: Player, item: ItemStack) : Boolean {
        player.teleport(player.respawnLocation ?: player.world.spawnLocation.add(0.5, 0.5, 0.5))
        player.inventory.removeItem(item, 1)
        player.sendMessage("귀환 스크롤을 사용해 리스폰 지점으로 텔레포트했습니다.")
        return true
    }

}