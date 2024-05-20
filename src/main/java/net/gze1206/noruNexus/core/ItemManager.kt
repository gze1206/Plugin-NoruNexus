package net.gze1206.noruNexus.core

import net.gze1206.noruNexus.core.Constants.ITEM_TYPE_KEY
import net.gze1206.noruNexus.core.Constants.MONEY_AMOUNT_KEY
import net.gze1206.noruNexus.core.Constants.SHOP_CUSTOM_ITEM_KEY_PREFIX
import net.gze1206.noruNexus.model.Title
import net.gze1206.noruNexus.utils.component
import net.gze1206.noruNexus.utils.not
import net.gze1206.noruNexus.utils.setRune
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemManager {
    fun fromKey(key: String, amount: Int) : ItemStack {
        if (key.startsWith(SHOP_CUSTOM_ITEM_KEY_PREFIX)) {
            return when (key.substring(SHOP_CUSTOM_ITEM_KEY_PREFIX.length)) {
                "recall_scroll" -> createRecallScroll(amount)
                "rune" -> createRune(RuneType.EMPTY, amount)
                "money" -> createMoney(amount.toLong())

                else -> throw NotImplementedError()
            }
        }

        return ItemStack(Material.matchMaterial(key)!!, amount)
    }

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

