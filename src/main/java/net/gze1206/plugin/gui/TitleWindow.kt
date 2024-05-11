package net.gze1206.plugin.gui

import net.gze1206.plugin.core.Constants
import net.gze1206.plugin.utils.not
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class TitleWindow(private val player: Player) : InventoryWindow {
    companion object {
        private const val COL_COUNT = 9
        private const val ROW_COUNT = 6
        private const val TOTAL_SIZE = COL_COUNT * ROW_COUNT

        private const val BUTTON_FROM = COL_COUNT * (ROW_COUNT - 1)
        private const val PREV_BUTTON_AT = BUTTON_FROM + 3
        private const val PAGE_AT = BUTTON_FROM + 4
        private const val NEXT_BUTTON_AT = BUTTON_FROM + 5

        private const val PREV_BUTTON_UID = "PREV"
        private const val NEXT_BUTTON_UID = "NEXT"

        private val windowTitle = Component.text("칭호", NamedTextColor.DARK_AQUA)
    }

    private val inventory : Inventory = Bukkit.createInventory(null, TOTAL_SIZE, windowTitle)
    override fun inventory() = inventory

    private var page : Int = 1

    fun open() {
        InventoryWindow.inventoryGuiMap[player.uniqueId] = this
        player.openInventory(inventory)
    }

    fun update(page: Int = 1) {
        this.page = page
        val maxPage = 3

        if (page < 1 || maxPage < page)
            throw IndexOutOfBoundsException("올바르지 않은 칭호 페이지에 접근했습니다. [${page}]")

        repeat(TOTAL_SIZE) {
            inventory.setItem(it, null)
        }

        if (1 < page) {
            val prev = ItemStack(Material.ARROW, 1)
            val prevMeta = prev.itemMeta
            prevMeta.displayName(!"이전 페이지")
            prevMeta.persistentDataContainer.set(Constants.GUI_UID_KEY, PersistentDataType.INTEGER, GuiType.TITLE.ordinal)
            prevMeta.persistentDataContainer.set(Constants.BUTTON_UID_KEY, PersistentDataType.STRING, PREV_BUTTON_UID)
            prev.itemMeta = prevMeta
            inventory.setItem(PREV_BUTTON_AT, prev)
        }

        val pageIndicator = ItemStack(Material.PAPER, 1)
        val pageMeta = pageIndicator.itemMeta
        pageMeta.displayName(!"페이지 $page / $maxPage")
        pageIndicator.itemMeta = pageMeta
        inventory.setItem(PAGE_AT, pageIndicator)

        if (page < maxPage) {
            val next = ItemStack(Material.ARROW, 1)
            val nextMeta = next.itemMeta
            nextMeta.displayName(!"다음 페이지")
            nextMeta.persistentDataContainer.set(Constants.GUI_UID_KEY, PersistentDataType.INTEGER, GuiType.TITLE.ordinal)
            nextMeta.persistentDataContainer.set(Constants.BUTTON_UID_KEY, PersistentDataType.STRING, NEXT_BUTTON_UID)
            next.itemMeta = nextMeta
            inventory.setItem(NEXT_BUTTON_AT, next)
        }
    }

    override fun onClick(buttonUid: String, item: ItemStack) {
        when (buttonUid) {
            PREV_BUTTON_UID -> update(page - 1)
            NEXT_BUTTON_UID -> update(page + 1)
            else -> {

            }
        }
    }
}