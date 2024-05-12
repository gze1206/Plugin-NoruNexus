package net.gze1206.noruNexus.utils

import net.gze1206.noruNexus.model.Title
import net.gze1206.noruNexus.model.User
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

fun Player.updateScoreboard(user: User) {
    val title = if (user.title == null) null else Title.get(user.title!!)
    val displayPrefix = title?.displayName ?: user.title
    val color = (title?.rarity ?: Title.Rarity.Normal).color

    User.scoreboardBuilder.let {
        it[1] = !"칭호 : " + if (user.title == null) !"(없음)" else "[$displayPrefix]".component(color)
        it[2] = !"소지금 : ${user.money}원"
        it[3] = !" "

        scoreboard = it.scoreboard()
    }
}

fun PlayerInventory.removeItem(item: ItemStack, amount: Int) {
    if (item.amount < amount) throw IndexOutOfBoundsException()

    val newAmount = item.amount - amount
    if (newAmount == 0) {
        this.removeItem(item)
    } else {
        item.amount = newAmount
    }
}