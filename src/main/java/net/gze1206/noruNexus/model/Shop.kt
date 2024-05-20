package net.gze1206.noruNexus.model

import net.gze1206.noruNexus.core.ConfigManager

data class Shop(
    val displayName : String,
    val products : List<ShopProduct>
) {
    companion object {
        const val GLOBAL_ID = "global"

        private const val NAME_KEY = "displayName"
        private const val PRODUCTS_KEY = "products"

        fun initConfig() {
            ConfigManager.shop.getConfig().run {
                options().copyDefaults(true)

                addDefault("${GLOBAL_ID}.$NAME_KEY", "상점")
            }
        }

        fun has(id: String) : Boolean {
            return ConfigManager.shop.getConfig().contains(id)
        }

        fun get(id: String) : Shop {
            val config = ConfigManager.shop.getConfig().getConfigurationSection(id)!!
            val products = mutableListOf<ShopProduct>()

            val productSection = config.getConfigurationSection(PRODUCTS_KEY)
            productSection?.getKeys(false)?.forEach {
                val section = productSection.getConfigurationSection(it)!!
                products.add(ShopProduct.from(section))
            }

            return Shop(
                config.getString(NAME_KEY)!!,
                products
            )
        }
    }
}