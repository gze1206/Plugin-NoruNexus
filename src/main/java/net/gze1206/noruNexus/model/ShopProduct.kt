package net.gze1206.noruNexus.model

import net.gze1206.noruNexus.core.Constants.SHOP_CUSTOM_ITEM_KEY_PREFIX
import net.gze1206.noruNexus.core.ItemManager
import net.gze1206.noruNexus.core.RuneType
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

data class ShopProduct(
    val key: String,
    val buyPricePerOne: Long?,
    val sellPricePerOne: Long?,
    val singleAmount: Int,
    val bulkAmount: Int? = null,
) {
    companion object {
        private const val KEY_KEY = "key"
        private const val BUY_PRICE_KEY = "buy_price_per_one"
        private const val SELL_PRICE_KEY = "sell_price_per_one"
        private const val SINGLE_AMOUNT_KEY = "single_amount"
        private const val BULK_AMOUNT_KEY = "bulk_amount"

        fun from(section: ConfigurationSection) : ShopProduct {
            return ShopProduct(
                section.getString(KEY_KEY)!!,
                if (section.contains(BUY_PRICE_KEY)) section.getLong(BUY_PRICE_KEY) else null,
                if (section.contains(SELL_PRICE_KEY)) section.getLong(SELL_PRICE_KEY) else null,
                section.getInt(SINGLE_AMOUNT_KEY),
                if (section.contains(BULK_AMOUNT_KEY)) section.getInt(BULK_AMOUNT_KEY) else null
            )
        }
    }

    fun isCustomItem() : Boolean {
        return key.startsWith(SHOP_CUSTOM_ITEM_KEY_PREFIX)
    }

    fun toItemStack(amount: Int = 1) : ItemStack {
        if (isCustomItem()) {
            return when (key.substring(SHOP_CUSTOM_ITEM_KEY_PREFIX.length)) {
                "recall_scroll" -> ItemManager.createRecallScroll(amount)
                "rune" -> ItemManager.createRune(RuneType.EMPTY)

                else -> throw NotImplementedError()
            }
        }

        return ItemStack(Material.matchMaterial(key)!!, amount)
    }
}
