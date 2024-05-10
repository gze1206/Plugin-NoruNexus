package net.gze1206.plugin.core

import net.gze1206.plugin.Main
import net.gze1206.plugin.model.User
import net.kyori.adventure.text.TextComponent
import org.bukkit.entity.Player
import java.util.UUID

class UserManager {
    companion object {
        private var activeUsers = HashMap<UUID, User>()

        fun addPlayer(player: Player) : User {
            val displayName = player.displayName() as TextComponent
            val user = User(player.uniqueId, displayName.content(), null, 0UL)
            activeUsers[player.uniqueId] = user

            Main.instance!!.logger.info("플레이어 ${player.name} 입장")
            return user
        }

        fun removePlayer(player: Player) {
            activeUsers.remove(player.uniqueId)

            Main.instance!!.logger.info("플레이어 ${player.name} 퇴장")
        }

        fun getUser(player: Player) : User? {
            return activeUsers[player.uniqueId]
        }
    }
}