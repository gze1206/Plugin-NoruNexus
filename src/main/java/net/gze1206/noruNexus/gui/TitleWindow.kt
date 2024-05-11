package net.gze1206.noruNexus.gui

import net.gze1206.noruNexus.core.Constants
import net.gze1206.noruNexus.core.UserManager
import net.gze1206.noruNexus.model.Title
import net.gze1206.noruNexus.utils.not
import net.gze1206.noruNexus.utils.updateScoreboard
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import kotlin.math.ceil

class TitleWindow(private val player: Player) : InventoryWindow {
    companion object {
        private const val COL_COUNT = 9
        private const val ROW_COUNT = 6
        private const val TOTAL_SIZE = COL_COUNT * ROW_COUNT
        private const val ITEMS_PER_PAGE = COL_COUNT * (ROW_COUNT - 1)

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
        val user = UserManager.getUser(player)
        var titles : Collection<Title> = Title.getOwnTitles(user)
        //NOTE: 칭호 수가 그렇게까지 많지는 않을 거라 다소 방만하게 작성합니다. 본래는 SQL 쿼리 자체에서 페이징한 뒤 가져오는 게 맞습니다.
        val maxPage = ceil(titles.size / ITEMS_PER_PAGE.toDouble()).toInt()
        titles = titles.drop(ITEMS_PER_PAGE * (page - 1)).take(ITEMS_PER_PAGE)

        if (page < 1 || maxPage < page)
            throw IndexOutOfBoundsException("올바르지 않은 칭호 페이지에 접근했습니다. [${page}]")

        repeat(TOTAL_SIZE) {
            if (titles.size <= it) {
                inventory.setItem(it, null)
                return@repeat
            }

            val title = titles[it]
            val item = ItemStack(if (title.id == null) Material.BARRIER else Material.NAME_TAG, 1)
            val meta = item.itemMeta
            meta.displayName(Component.text(title.displayName, TextColor.fromHexString(title.rarity.color)))
            meta.lore(listOf(!title.lore))
            meta.setCustomModelData(title.rarity.customModelId)
            meta.persistentDataContainer.set(Constants.GUI_UID_KEY, PersistentDataType.INTEGER, GuiType.TITLE.ordinal)
            meta.persistentDataContainer.set(Constants.BUTTON_UID_KEY, PersistentDataType.STRING, title.id ?: "")
            item.itemMeta = meta
            inventory.setItem(it, item)
        }

        drawPagination(maxPage)
    }

    private fun drawPagination(maxPage: Int) {
        if (1 < page) {
            val prev = ItemStack(Material.ARROW, 1)
            val prevMeta = prev.itemMeta
            prevMeta.displayName(!"이전 페이지")
            prevMeta.setCustomModelData(1)
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
            nextMeta.setCustomModelData(2)
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
            else -> setTitle(buttonUid.ifBlank { null })
        }
    }

    private fun setTitle(titleId: String?) {
        val succeed = UserManager.getUser(player).transaction {
            title = titleId
        }

        if (!succeed) return

        UserManager.getUser(player).let {
            val displayName = it.getDisplayName()
            player.displayName(displayName)
            player.playerListName(displayName)
            player.updateScoreboard(it)
        }
    }
}