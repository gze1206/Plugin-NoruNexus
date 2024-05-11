package net.gze1206.plugin.core

import net.gze1206.plugin.model.Title
import net.gze1206.plugin.model.User
import net.gze1206.plugin.utils.updateScoreboard
import org.bukkit.entity.Player

object UserManager {
    fun addPlayer(player: Player) : User {
        val user = getUser(player)
        val displayName = user.getDisplayName()

        player.run {
            displayName(displayName)
            playerListName(displayName)
            updateScoreboard(user)
        }

        return user
    }

    fun getUser(player: Player) : User {
        var user = User.get(player)
        if (user == null) {
            user = User.create(player)!!
            Title.give(user, "test")
        }

        return user
    }
}