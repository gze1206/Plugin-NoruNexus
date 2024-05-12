package net.gze1206.noruNexus.core

import net.gze1206.noruNexus.Main
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.NamespacedKey

object Constants {
    val SYSTEM_MESSAGE_COMPONENT = Component.text("[시스템] ", NamedTextColor.YELLOW)
    val GUI_UID_KEY = NamespacedKey(Main.getInstance(), "gui-uid")
    val BUTTON_UID_KEY = NamespacedKey(Main.getInstance(), "gui-button-uid")
    val ITEM_TYPE_KEY = NamespacedKey(Main.getInstance(), "item-type")
    val MONEY_UNIT_KEY = NamespacedKey(Main.getInstance(), "money-unit")
    const val TEST_TITLE_ID = "999-test"
}