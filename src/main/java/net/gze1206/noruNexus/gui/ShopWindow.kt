package net.gze1206.noruNexus.gui

import net.gze1206.noruNexus.core.Constants
import net.gze1206.noruNexus.core.ItemManager
import net.gze1206.noruNexus.gui.InventoryWindow.Companion.NEXT_BUTTON_UID
import net.gze1206.noruNexus.gui.InventoryWindow.Companion.PREV_BUTTON_UID
import net.gze1206.noruNexus.utils.component
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import kotlin.math.ceil

class ShopWindow(private val player: Player) : InventoryWindow {
    companion object {
        private const val COL_COUNT = 9
        private const val ROW_COUNT = 6
        private const val TOTAL_SIZE = COL_COUNT * ROW_COUNT
        private const val ITEMS_PER_PAGE = COL_COUNT * (ROW_COUNT - 1)

        private const val BUTTON_FROM = COL_COUNT * (ROW_COUNT - 1)
        private const val PREV_BUTTON_AT = BUTTON_FROM + 3
        private const val PAGE_AT = BUTTON_FROM + 4
        private const val NEXT_BUTTON_AT = BUTTON_FROM + 5

        private const val CUSTOM_ITEM_KEY = "custom:"

        private val windowTitle = SHOP_INFO_TEMP.shopName.component(NamedTextColor.DARK_AQUA)
    }

    private data class ShopProduct(
        val key: String,
        val buyPricePerOne: Long?,
        val sellPricePerOne: Long?,
        val singleAmount: Int,
        val bulkAmount: Int? = null,
    ) {
        fun isCustomItem() : Boolean {
            return key.startsWith(CUSTOM_ITEM_KEY)
        }

        fun toItemStack() : ItemStack {
            if (isCustomItem()) {
                return when (key.substring(CUSTOM_ITEM_KEY.length)) {
                    "recall_scroll" -> ItemManager.createRecallScroll(1)

                    else -> throw NotImplementedError()
                }
            }

            return ItemStack(Material.matchMaterial(key)!!, 1)
        }
    }

    private object SHOP_INFO_TEMP {
        const val shopName = "상점"
        val products = arrayOf(
            ShopProduct("custom:recall_scroll", 1000, null, 1, 10),
            ShopProduct("minecraft:white_bed", 5000, null, 1, 1)
        )
    }

    private val inventory = Bukkit.createInventory(null, TOTAL_SIZE, windowTitle)
    override fun inventory() = inventory

    private var page = 1
    override fun page() = page

    fun open() {
        InventoryWindow.inventoryGuiMap[player.uniqueId] = this
        player.openInventory(inventory)
    }

    fun update(page: Int = 1) {
        val maxPage = ceil(SHOP_INFO_TEMP.products.size / ITEMS_PER_PAGE.toDouble()).toInt()

        if (page < 1 || maxPage < page) {
            throw IndexOutOfBoundsException("올바르지 않은 상점 페이지에 접근했습니다.")
        }
        this.page = page
        val items = SHOP_INFO_TEMP.products.drop(ITEMS_PER_PAGE * (page - 1)).take(ITEMS_PER_PAGE)

        repeat(TOTAL_SIZE) {
            if (items.size <= it) {
                inventory.setItem(it, null)
                return@repeat
            }

            val product = items[it]
            val item = product.toItemStack()
            val meta = item.itemMeta
            meta.lore(productToLore(meta.lore(), item.maxStackSize, product))
            meta.persistentDataContainer.set(Constants.GUI_UID_KEY, PersistentDataType.INTEGER, GuiType.SHOP.ordinal)
            meta.persistentDataContainer.set(Constants.BUTTON_UID_KEY, PersistentDataType.STRING, product.key)
            item.itemMeta = meta
            inventory.setItem(it, item)
        }

        drawPagination(maxPage)
    }

    private fun productToLore(originalLore: List<Component>?, maxStackSize: Int, product: ShopProduct) : MutableList<Component> {
        val lore = originalLore?.toMutableList() ?: mutableListOf()
        val bulkAmount = product.bulkAmount ?: maxStackSize

        if (product.sellPricePerOne != null) {
            if (1 < bulkAmount) lore.add(0, "${bulkAmount}개 일괄 판매 [Shift+오른쪽 클릭] : ${bulkAmount * product.sellPricePerOne}원".component(NamedTextColor.YELLOW))
            lore.add(0, "판매 [오른쪽 클릭] : ${product.sellPricePerOne}원".component(NamedTextColor.YELLOW))
        }
        else lore.add(0, "판매 불가".component(NamedTextColor.RED).style(Style.style(TextDecoration.ITALIC)))

        if (product.buyPricePerOne != null) {
            if (1 < bulkAmount) lore.add(0, "${bulkAmount}개 일괄 구매 [Shift+왼쪽 클릭] : ${bulkAmount * product.buyPricePerOne}원".component(NamedTextColor.YELLOW))
            lore.add(0, "구매 [왼쪽 클릭] : ${product.buyPricePerOne}원".component(NamedTextColor.YELLOW))
        }
        else lore.add(0, "구매 불가".component(NamedTextColor.RED).style(Style.style(TextDecoration.ITALIC)))

        return lore
    }

    private fun drawPagination(maxPage: Int) {
        drawPagination(GuiType.SHOP, PREV_BUTTON_AT, PAGE_AT, NEXT_BUTTON_AT, maxPage)
    }

    override fun onClick(buttonUid: String, item: ItemStack, clickType: ClickType) : Boolean {
        when (buttonUid) {
            PREV_BUTTON_UID -> {
                update(page - 1)
            }
            NEXT_BUTTON_UID -> {
                update(page + 1)
            }
            else -> {
                val product = SHOP_INFO_TEMP.products.find { it.key == buttonUid }!!
                when (clickType) {
                    // 왼쪽 클릭 : 구매
                    ClickType.LEFT -> {
                        buyItem(ItemStack(item.type, product.singleAmount), product)
                    }
                    ClickType.SHIFT_LEFT -> {
                        buyItem(ItemStack(item.type, product.bulkAmount ?: item.maxStackSize), product)
                    }

                    // 오른쪽 클릭 : 판매
                    ClickType.RIGHT -> {
                        sellItem(ItemStack(item.type, product.singleAmount), product)
                    }
                    ClickType.SHIFT_RIGHT -> {
                        sellItem(ItemStack(item.type, product.bulkAmount ?: item.maxStackSize), product)
                    }

                    else -> {}
                }
            }
        }

        return true
    }

    private fun buyItem(item: ItemStack, product: ShopProduct) {

    }

    private fun sellItem(item: ItemStack, product: ShopProduct) {

    }
}