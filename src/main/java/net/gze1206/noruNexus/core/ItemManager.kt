package net.gze1206.noruNexus.core

import net.gze1206.noruNexus.model.Title
import net.gze1206.noruNexus.utils.component
import net.gze1206.noruNexus.utils.not
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemManager {
    fun createMoney(amount: Long) : ItemStack {
        val item = ItemStack(Material.CLOCK, 1)
        val itemMeta = item.itemMeta
        itemMeta.setCustomModelData(1)
        itemMeta.displayName(!"${amount}원")
        itemMeta.persistentDataContainer.set(Constants.ITEM_TYPE_KEY, PersistentDataType.STRING, ItemType.MONEY.name)
        itemMeta.persistentDataContainer.set(Constants.MONEY_AMOUNT_KEY, PersistentDataType.LONG, amount)
        item.itemMeta = itemMeta

        return item
    }

    fun createRecallScroll(amount: Int) : ItemStack {
        val item = ItemStack(Material.CLOCK, amount)
        val itemMeta = item.itemMeta
        itemMeta.setCustomModelData(2)
        itemMeta.displayName("귀환 스크롤".component(Title.Rarity.Rare.color))
        itemMeta.persistentDataContainer.set(Constants.ITEM_TYPE_KEY, PersistentDataType.STRING, ItemType.RECALL_SCROLL.name)
        item.itemMeta = itemMeta

        return item
    }
}