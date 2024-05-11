package net.gze1206.noruNexus.gui

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.HashMap

interface InventoryWindow {
    companion object {
        val inventoryGuiMap = HashMap<UUID, InventoryWindow>()
    }

    fun inventory() : Inventory
    fun onClick(buttonUid: String, item: ItemStack)
}