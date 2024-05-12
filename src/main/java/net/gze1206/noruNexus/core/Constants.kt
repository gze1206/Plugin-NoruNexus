package net.gze1206.noruNexus.core

import net.gze1206.noruNexus.Main
import net.gze1206.noruNexus.utils.component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.NamespacedKey

object Constants {
    const val RESET_CHARACTER = "§f"
    val SYSTEM_MESSAGE_COMPONENT = "[시스템] ".component(NamedTextColor.YELLOW)
    val GUI_UID_KEY = NamespacedKey(Main.getInstance(), "gui-uid")
    val BUTTON_UID_KEY = NamespacedKey(Main.getInstance(), "gui-button-uid")
    val ITEM_TYPE_KEY = NamespacedKey(Main.getInstance(), "item-type")
    val MONEY_AMOUNT_KEY = NamespacedKey(Main.getInstance(), "money-amount")
}