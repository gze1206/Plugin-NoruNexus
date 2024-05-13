package net.gze1206.noruNexus.core

import net.gze1206.noruNexus.core.Constants.ITEM_TYPE_KEY
import net.gze1206.noruNexus.core.Constants.MONEY_AMOUNT_KEY
import net.gze1206.noruNexus.core.Constants.RUNE_PROGRESS_KEY
import net.gze1206.noruNexus.core.Constants.RUNE_TYPE_KEY
import net.gze1206.noruNexus.model.Title
import net.gze1206.noruNexus.utils.component
import net.gze1206.noruNexus.utils.not
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import kotlin.math.floor

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

    fun createRune(runeType: RuneType, progress: Double = 1.0) : ItemStack {
        val item = ItemStack(Material.CLOCK, 1)
        item.setRune(runeType, progress)

        return item
    }
}

fun ItemStack.setRune(runeType: RuneType, progress: Double) {
    val itemMeta = this.itemMeta
    val isActive = 1.0 <= progress
    val color = if (isActive) Title.Rarity.Epic.color else Title.Rarity.Rare.color
    var loreText = "각인 진행 ${"%.2f".format(progress * 100)}"
    if (isActive) {
        loreText += " (${floor(progress)}회 충전됨)"
    }
    itemMeta.setCustomModelData(if (isActive) runeType.activeModelId else runeType.inactiveModelId)
    itemMeta.displayName(runeType.displayName.component(color))
    itemMeta.lore(mutableListOf(
        !loreText
    ))
    itemMeta.persistentDataContainer.set(ITEM_TYPE_KEY, PersistentDataType.STRING, ItemType.RUNE.name)
    itemMeta.persistentDataContainer.set(RUNE_TYPE_KEY, PersistentDataType.STRING, runeType.name)
    itemMeta.persistentDataContainer.set(RUNE_PROGRESS_KEY, PersistentDataType.DOUBLE, progress)
    this.itemMeta = itemMeta
}

fun ItemStack.getRuneType() : RuneType {
    val text = itemMeta.persistentDataContainer.get(RUNE_TYPE_KEY, PersistentDataType.STRING) ?: return RuneType.NONE
    return RuneType.valueOf(text)
}

fun ItemStack.addRuneProgress(value: Double) {
    val runeType = getRuneType()
    if (runeType == RuneType.NONE) {
        throw Exception("${RuneType.NONE.displayName}의 각인은 진행할 수 없습니다.")
    }

    val itemMeta = this.itemMeta
    val progress = itemMeta.persistentDataContainer.get(RUNE_PROGRESS_KEY, PersistentDataType.DOUBLE) ?: throw Exception("올바른 룬 아이템이 아닙니다.")
    setRune(runeType, progress + value)
}