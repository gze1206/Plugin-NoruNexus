package net.gze1206.noruNexus.event

import net.gze1206.noruNexus.core.ConfigManager
import net.gze1206.noruNexus.core.Constants
import net.gze1206.noruNexus.core.ItemType
import net.gze1206.noruNexus.core.RuneType
import net.gze1206.noruNexus.utils.addRuneProgress
import net.gze1206.noruNexus.utils.isTypeOf
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.persistence.PersistentDataType

object BlockBreakEvent : Listener {

    @EventHandler
    fun onBlockBreakEvent(e: BlockBreakEvent) {
        progressRune(e.block.type.name, e.player)
    }

    private fun progressRune(blockName: String, player: Player) {
        val progression = ConfigManager.rune.getProgressOnMining(blockName)

        player.inventory.forEach {
            if (it == null || !it.isTypeOf(ItemType.RUNE)) {
                return@forEach
            }

            val runeType = it.itemMeta.persistentDataContainer.get(Constants.RUNE_TYPE_KEY, PersistentDataType.STRING)
            if (runeType == null || runeType == RuneType.EMPTY.name) {
                return@forEach
            }

            it.addRuneProgress(progression)
        }
    }

}