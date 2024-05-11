package net.gze1206.plugin.core

import net.gze1206.plugin.model.Title
import net.gze1206.plugin.model.User
import org.bukkit.entity.Player

object UserManager {
    fun addPlayer(player: Player) : User {
        var user = User.get(player)
        if (user == null) {
            user = User.create(player)
            Title.give(user!!, "test")
        }

        val displayName = user.getDisplayName()
        player.displayName(displayName)
        player.playerListName(displayName)
        user.updateScoreboard(player)

        return user
    }

    fun getUser(player: Player) : User? {
        var user = User.get(player)
        if (user == null) user = User.create(player)

        return user
    }
}