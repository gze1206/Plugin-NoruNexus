package net.gze1206.plugin.core

import net.gze1206.plugin.Main
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.NamespacedKey

object Constants {
    const val DATA_DIR = "plugins/Plugin"
    val SYSTEM_MESSAGE_COMPONENT = Component.text("[시스템] ", NamedTextColor.YELLOW)
    val GUI_UID_KEY = NamespacedKey(Main.instance!!, "gui-uid")
    val BUTTON_UID_KEY = NamespacedKey(Main.instance!!, "gui-button-uid")
}