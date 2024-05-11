package net.gze1206.noruNexus.utils

import net.gze1206.noruNexus.model.Title
import net.gze1206.noruNexus.model.User
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player

fun Player.updateScoreboard(user: User) {
    val title = if (user.title == null) null else Title.get(user.title!!)
    val displayPrefix = title?.displayName ?: user.title
    val color = (title?.rarity ?: Title.Rarity.Normal).color

    User.scoreboardBuilder.let {
        it[1] = !"칭호 : " + if (user.title == null) !"(없음)" else Component.text("[$displayPrefix]", TextColor.fromHexString(color))
        it[2] = !"소지금 : ${user.money}원"

        scoreboard = it.scoreboard()
    }
}