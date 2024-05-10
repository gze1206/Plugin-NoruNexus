package net.gze1206.plugin.core

import net.gze1206.plugin.Main
import net.gze1206.plugin.model.User
import org.bukkit.entity.Player

class UserManager {
    companion object {
        fun addPlayer(player: Player) : User {
            var user = User.get(player)
            if (user == null) user = User.create(player)

            player.displayName(user!!.getDisplayName())

            Main.instance!!.logger.info("플레이어 ${player.name} 입장")
            return user
        }

        fun removePlayer(player: Player) {
            Main.instance!!.logger.info("플레이어 ${player.name} 퇴장")
        }

        fun getUser(player: Player) : User? {
            return User.get(player)
        }
    }
}