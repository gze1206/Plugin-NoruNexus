package net.gze1206.noruNexus.event

import net.gze1206.noruNexus.core.*
import net.gze1206.noruNexus.core.Constants.MONEY_AMOUNT_KEY
import net.gze1206.noruNexus.utils.*
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import kotlin.random.Random

object PlayerInteractEvent : Listener {

    @EventHandler
    fun onEvent(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!e.hasItem()) return

        val item = e.item!!
        val itemMeta = item.itemMeta

        e.isCancelled = when (item.getCustomType()) {
            ItemType.MONEY -> useMoney(e.player, item, itemMeta)
            ItemType.RECALL_SCROLL -> useRecallScroll(e.player, item)
            ItemType.RUNE -> useRune(e.player, item)

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

    private fun useRune(player: Player, item: ItemStack) : Boolean {
        if (item.getRuneProgress() < 1.0) {
            return false
        }

        val runeType = item.getRuneType()
        val params = ConfigManager.rune.getParameters(runeType)

        fun give(type: Material, min: Int = params[0], max: Int = params[1]) {
            val itemStack = ItemStack(type, Random.nextInt(min, max + 1))
            player.inventory.addItem(itemStack)
        }

        repeat(item.amount) {
            when (runeType) {
                RuneType.EMPTY -> return false

                RuneType.BONE -> give(Material.BONE)
                RuneType.GUNPOWDER -> give(Material.GUNPOWDER)
                RuneType.IRON -> give(Material.IRON_INGOT)
                RuneType.STRING -> give(Material.STRING)

                RuneType.ENDER -> {
                    give(Material.ENDER_PEARL)
                    if (Random.nextInt(101) <= params[2]) {
                        give(Material.ENDER_EYE, 1, 1)
                    }
                }
                RuneType.MONEY -> {
                    val money = ItemManager.createMoney(Random.nextLong(params[0].toLong(), params[1].toLong() + 1))
                    player.inventory.addItem(money)
                }

                else -> throw NotImplementedError()
            }
        }

        item.addRuneProgress(-1.0)
        return true
    }

}