package net.gze1206.plugin.core

import net.gze1206.plugin.Main
import net.gze1206.plugin.model.User
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import java.util.UUID

class UserManager {
    companion object {
        private var activeUsers = HashMap<UUID, User>()

        fun addPlayer(player: Player) : User {
            val displayName = (player.displayName() as TextComponent).content()
            player.displayName(
                Component.text("[테스트] ").color(TextColor.color(0x13f832))
                .append(Component.text(displayName, NamedTextColor.WHITE)))
            val user = User(player.uniqueId, displayName, null, 0UL)
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