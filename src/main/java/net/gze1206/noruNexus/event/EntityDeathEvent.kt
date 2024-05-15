package net.gze1206.noruNexus.event

import net.gze1206.noruNexus.core.ConfigManager
import net.gze1206.noruNexus.core.Constants.RUNE_TYPE_KEY
import net.gze1206.noruNexus.core.ItemManager
import net.gze1206.noruNexus.core.ItemType
import net.gze1206.noruNexus.core.RuneType
import net.gze1206.noruNexus.utils.addRuneProgress
import net.gze1206.noruNexus.utils.isTypeOf
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.persistence.PersistentDataType
import kotlin.random.Random

object EntityDeathEvent : Listener {

    @EventHandler
    fun onEntityDeathEvent(e: EntityDeathEvent) {
        if (e.entity !is Monster) return

        if (e.entity.killer != null) {
            progressRune(e.entityType.name, e.entity.killer!!)
        }

        dropEmptyRune(e)
    }

    private fun dropEmptyRune(e: EntityDeathEvent) {
        val dropInfo = ConfigManager.rune.getDropInfo(e.entityType.name)
        val random = Random.nextInt(101)

        if (random <= dropInfo.rate) {
            val rune = ItemManager.createRune(RuneType.EMPTY, amount = dropInfo.amount)
            e.drops.add(rune)
        }
    }

    private fun progressRune(monsterName: String, player: Player) {
        val progression = ConfigManager.rune.getProgressOnKillMonster(monsterName)

        player.inventory.forEach {
            if (it == null || !it.isTypeOf(ItemType.RUNE)) {
                return@forEach
            }

            val runeType = it.itemMeta.persistentDataContainer.get(RUNE_TYPE_KEY, PersistentDataType.STRING)
            if (runeType == null || runeType == RuneType.EMPTY.name) {
                return@forEach
            }

            it.addRuneProgress(progression)
        }
    }

}