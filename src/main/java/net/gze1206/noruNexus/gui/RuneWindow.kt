package net.gze1206.noruNexus.gui

import net.gze1206.noruNexus.core.ItemManager
import net.gze1206.noruNexus.core.RuneType
import net.gze1206.noruNexus.utils.component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.InventoryType.SlotType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe

class RuneWindow(private val player: Player) : InventoryActionWindow {
    companion object {
        private val windowTitle = "룬 각인".component(NamedTextColor.DARK_PURPLE)
    }

    private val inventory = Bukkit.createInventory(null, InventoryType.GRINDSTONE, windowTitle)
    override fun inventory() = inventory

    override fun page() = 1

    private val merchant = Bukkit.createMerchant(windowTitle)

    fun open() {
        InventoryWindow.inventoryGuiMap[player.uniqueId] = this
        update()
        player.openMerchant(merchant, true)
    }

    private fun update() {
        val recipes = mutableListOf<MerchantRecipe>()
        val recipe = MerchantRecipe(ItemManager.createRune(RuneType.IRON), 10000)
        recipe.addIngredient(ItemManager.createRune(RuneType.EMPTY))
        recipe.addIngredient(ItemStack(Material.IRON_INGOT))
        recipes.add(recipe)

        merchant.recipes = recipes
    }

    override fun onAction(current: ItemStack?, cursor: ItemStack, rawSlot: Int, slotType: SlotType, action: InventoryAction) : Boolean {
        return false
    }
}