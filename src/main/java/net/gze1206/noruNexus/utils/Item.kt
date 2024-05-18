package net.gze1206.noruNexus.utils

import net.gze1206.noruNexus.core.ConfigManager
import net.gze1206.noruNexus.core.Constants
import net.gze1206.noruNexus.core.Constants.ITEM_TYPE_KEY
import net.gze1206.noruNexus.core.ItemType
import net.gze1206.noruNexus.core.RuneType
import net.gze1206.noruNexus.model.Title
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import kotlin.math.floor

fun ItemStack.getCustomType() : ItemType? {
    val value = itemMeta.persistentDataContainer.get(ITEM_TYPE_KEY, PersistentDataType.STRING) ?: return null

    return ItemType.valueOf(value)
}

fun ItemStack.isTypeOf(type: ItemType) : Boolean {
    return type == getCustomType()
}

fun ItemStack.setRune(runeType: RuneType, progress: Double) {
    val itemMeta = this.itemMeta
    val isActive = 1.0 <= progress
    val color = if (isActive) Title.Rarity.Epic.color else Title.Rarity.Rare.color

    val lore = mutableListOf<Component>()
    if (runeType != RuneType.EMPTY) {
        var loreText = "각인 진행 ${"%.2f".format(progress * 100)}"
        if (isActive) {
            loreText += " (${floor(progress)}회 충전됨)"
        }

        lore.add(!loreText)
    }
    var loreText = runeType.lore
    ConfigManager.rune.getParameters(runeType).forEachIndexed { idx: Int, it: Int ->
        loreText = loreText.replace("%${idx+1}", it.toString())
    }
    lore.add(!loreText)

    itemMeta.setCustomModelData(if (isActive) runeType.activeModelId else runeType.inactiveModelId)
    itemMeta.displayName(runeType.displayName.component(color))
    itemMeta.lore(lore)
    itemMeta.persistentDataContainer.set(ITEM_TYPE_KEY, PersistentDataType.STRING, ItemType.RUNE.name)
    itemMeta.persistentDataContainer.set(Constants.RUNE_TYPE_KEY, PersistentDataType.STRING, runeType.name)
    itemMeta.persistentDataContainer.set(Constants.RUNE_PROGRESS_KEY, PersistentDataType.DOUBLE, progress)
    this.itemMeta = itemMeta
}

fun ItemStack.getRuneType() : RuneType {
    val text = itemMeta.persistentDataContainer.get(Constants.RUNE_TYPE_KEY, PersistentDataType.STRING) ?: return RuneType.EMPTY
    return RuneType.valueOf(text)
}

fun ItemStack.getRuneProgress() : Double {
    return itemMeta.persistentDataContainer.get(Constants.RUNE_PROGRESS_KEY, PersistentDataType.DOUBLE) ?: throw Exception("올바른 룬 아이템이 아닙니다.")
}

fun ItemStack.isChargedRune() : Boolean {
    val progress = itemMeta.persistentDataContainer.get(Constants.RUNE_PROGRESS_KEY, PersistentDataType.DOUBLE) ?: return false
    return 1.0 <= progress
}

fun ItemStack.addRuneProgress(value: Double) {
    val runeType = getRuneType()
    if (runeType == RuneType.EMPTY) {
        throw Exception("${RuneType.EMPTY.displayName}의 각인은 진행할 수 없습니다.")
    }

    val itemMeta = this.itemMeta
    val progress = itemMeta.persistentDataContainer.get(Constants.RUNE_PROGRESS_KEY, PersistentDataType.DOUBLE) ?: throw Exception("올바른 룬 아이템이 아닙니다.")
    setRune(runeType, progress + value)
}