package net.gze1206.noruNexus.gui

import net.gze1206.noruNexus.core.Constants
import net.gze1206.noruNexus.utils.not
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*
import kotlin.collections.HashMap

interface InventoryWindow {
    companion object {
        val inventoryGuiMap = HashMap<UUID, InventoryWindow>()

        const val PREV_BUTTON_UID = "PREV"
        const val NEXT_BUTTON_UID = "NEXT"
        val prevPageComponent = !"이전 페이지"
        val nextPageComponent = !"다음 페이지"
    }

    fun inventory() : Inventory
    fun page() : Int

    fun onClick(buttonUid: String, item: ItemStack, clickType: ClickType) : Boolean

    fun drawPagination(guiType: GuiType, prevButtonAt: Int, pageAt: Int, nextButtonAt: Int, maxPage: Int) {
        val page = page()
        val inventory = inventory()

        if (1 < page) {
            val prev = ItemStack(Material.ARROW, 1)
            val prevMeta = prev.itemMeta
            prevMeta.displayName(prevPageComponent)
            prevMeta.setCustomModelData(1)
            prevMeta.persistentDataContainer.set(Constants.GUI_UID_KEY, PersistentDataType.INTEGER, guiType.ordinal)
            prevMeta.persistentDataContainer.set(
                Constants.BUTTON_UID_KEY, PersistentDataType.STRING,
                PREV_BUTTON_UID
            )
            prev.itemMeta = prevMeta
            inventory.setItem(prevButtonAt, prev)
        }

        val pageIndicator = ItemStack(Material.PAPER, 1)
        val pageMeta = pageIndicator.itemMeta
        pageMeta.displayName(!"페이지 $page / $maxPage")
        pageIndicator.itemMeta = pageMeta
        inventory.setItem(pageAt, pageIndicator)

        if (page < maxPage) {
            val next = ItemStack(Material.ARROW, 1)
            val nextMeta = next.itemMeta
            nextMeta.displayName(nextPageComponent)
            nextMeta.setCustomModelData(2)
            nextMeta.persistentDataContainer.set(Constants.GUI_UID_KEY, PersistentDataType.INTEGER, guiType.ordinal)
            nextMeta.persistentDataContainer.set(
                Constants.BUTTON_UID_KEY, PersistentDataType.STRING,
                NEXT_BUTTON_UID
            )
            next.itemMeta = nextMeta
            inventory.setItem(nextButtonAt, next)
        }
    }
}