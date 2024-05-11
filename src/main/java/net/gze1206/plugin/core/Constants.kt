package net.gze1206.plugin.core

import net.gze1206.plugin.Main
import org.bukkit.NamespacedKey

object Constants {
    const val DATA_DIR = "plugins/Plugin"
    val GUI_UID_KEY = NamespacedKey(Main.instance!!, "gui-uid")
    val BUTTON_UID_KEY = NamespacedKey(Main.instance!!, "gui-button-uid")
}