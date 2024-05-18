package net.gze1206.noruNexus.event

import net.gze1206.noruNexus.core.Constants.BUTTON_UID_KEY
import net.gze1206.noruNexus.core.Constants.GUI_UID_KEY
import net.gze1206.noruNexus.gui.InventoryWindow
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.persistence.PersistentDataType

object InventoryClickEvent : Listener {

    @EventHandler
    fun onInventoryClickEvent(e: InventoryClickEvent) {
        val inventoryWindow = InventoryWindow.inventoryGuiMap[e.whoClicked.uniqueId]
        if (inventoryWindow?.inventory() != e.inventory) return

        e.isCancelled = true
        if (e.currentItem == null) return
        val item = e.currentItem!!

        val itemMeta = item.itemMeta
        val guiType = itemMeta.persistentDataContainer.get(GUI_UID_KEY, PersistentDataType.INTEGER) ?: return
        if (guiType < 0) return

        val buttonUid = itemMeta.persistentDataContainer.get(BUTTON_UID_KEY, PersistentDataType.STRING)!!
        e.isCancelled = inventoryWindow.onClick(buttonUid, item, e.click)
    }

}