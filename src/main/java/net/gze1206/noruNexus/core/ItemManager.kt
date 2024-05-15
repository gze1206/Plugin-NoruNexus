package net.gze1206.noruNexus.core

import net.gze1206.noruNexus.core.Constants.ITEM_TYPE_KEY
import net.gze1206.noruNexus.core.Constants.MONEY_AMOUNT_KEY
import net.gze1206.noruNexus.model.Title
import net.gze1206.noruNexus.utils.component
import net.gze1206.noruNexus.utils.not
import net.gze1206.noruNexus.utils.setRune
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemManager {
    fun createMoney(amount: Long) : ItemStack {
        val item = ItemStack(Material.CLOCK, 1)
        val itemMeta = item.itemMeta
        itemMeta.setCustomModelData(1)
        itemMeta.displayName(!"${amount}원")
        itemMeta.persistentDataContainer.set(ITEM_TYPE_KEY, PersistentDataType.STRING, ItemType.MONEY.name)
        itemMeta.persistentDataContainer.set(MONEY_AMOUNT_KEY, PersistentDataType.LONG, amount)
        item.itemMeta = itemMeta

        return item
    }

    fun createRecallScroll(amount: Int) : ItemStack {
        val item = ItemStack(Material.CLOCK, amount)
        val itemMeta = item.itemMeta
        itemMeta.setCustomModelData(2)
        itemMeta.displayName("귀환 스크롤".component(Title.Rarity.Rare.color))
        itemMeta.persistentDataContainer.set(ITEM_TYPE_KEY, PersistentDataType.STRING, ItemType.RECALL_SCROLL.name)
        item.itemMeta = itemMeta

        return item
    }

    fun createRune(runeType: RuneType, amount: Int = 1, progress: Double = 0.0) : ItemStack {
        val item = ItemStack(Material.CLOCK, amount)
        item.setRune(runeType, progress)

        return item
    }
}

