package net.gze1206.plugin.core

import net.gze1206.plugin.model.User
import org.bukkit.entity.Player

class UserManager {
    companion object {
        fun addPlayer(player: Player) : User {
            var user = User.get(player)
            if (user == null) user = User.create(player)

            player.displayName(user!!.getDisplayName())
            user.updateScoreboard(player)

            return user
        }

        fun getUser(player: Player) : User? {
            var user = User.get(player)
            if (user == null) user = User.create(player)

            return user
        }
    }
}