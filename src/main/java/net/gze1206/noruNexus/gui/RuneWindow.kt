package net.gze1206.noruNexus.gui

import net.gze1206.noruNexus.core.ConfigManager
import net.gze1206.noruNexus.core.ItemManager
import net.gze1206.noruNexus.core.RuneType
import net.gze1206.noruNexus.utils.component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.MerchantRecipe

class RuneWindow(private val player: Player) {
    companion object {
        private val windowTitle = "룬 각인".component(NamedTextColor.DARK_PURPLE)
    }

    private val merchant = Bukkit.createMerchant(windowTitle)

    fun open() {
        initMerchant()
        player.openMerchant(merchant, true)
    }

    private fun initMerchant() {
        val recipes = mutableListOf<MerchantRecipe>()
        ConfigManager.rune.getRecipeKeys().forEach {
            val ingredients = ConfigManager.rune.getRecipe(it)
            if (ingredients.isEmpty()) return@forEach

            val runeType = RuneType.valueOf(it)
            val recipe = MerchantRecipe(ItemManager.createRune(runeType), 10000)
            recipe.addIngredient(ItemManager.createRune(RuneType.EMPTY))
            ingredients.forEach { ingredient -> recipe.addIngredient(ingredient) }
            recipes.add(recipe)
        }

        merchant.recipes = recipes
    }
}