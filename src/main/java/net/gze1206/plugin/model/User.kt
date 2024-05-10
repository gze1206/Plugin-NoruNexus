package net.gze1206.plugin.model

import net.gze1206.plugin.Main
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import java.util.UUID

data class User (
    val uuid : UUID,
    var nickname: String,
    var prefix : String?,
    var money: Long,
) {
    companion object {
        fun createTable() {
            val conn = Main.db.getConnection()!!
            val statement = conn.createStatement()
            statement.execute("CREATE TABLE IF NOT EXISTS Users (Id TEXT PRIMARY KEY, Nickname TEXT, Prefix TEXT, Money INTEGER)")
            statement.close()
        }

        fun create(player: Player) : User? {
            val tempPrefix = "테스트"
            val displayName = (player.displayName() as TextComponent).content()
            val user = User(player.uniqueId, displayName, tempPrefix, 0L)

            val conn = Main.db.getConnection()!!
            val statement = conn.prepareStatement("INSERT INTO Users (Id, Nickname, Prefix, Money) VALUES (?, ?, ?, ?)")
            statement.setString(1, user.uuid.toString())
            statement.setString(2, user.nickname)
            statement.setString(3, user.prefix)
            statement.setLong(4, user.money)

            val effected = statement.executeUpdate()

            if (effected <= 0) {
                Main.instance!!.logger.severe("행이 추가되지 않았습니다. [${user.uuid},${user.nickname},${user.prefix},${user.money}]")
                return null
            }

            statement.close()
            return user
        }

        fun get(player: Player) : User? {
            val conn = Main.db.getConnection()!!
            val statement = conn.prepareStatement("SELECT * FROM Users WHERE Id = ?")
            statement.setString(1, player.uniqueId.toString())

            val result = statement.executeQuery()
            if (!result.next())
                return null

            val user = User(
                UUID.fromString(result.getString(1)),
                result.getString(2),
                result.getString(3),
                result.getLong(4))

            statement.close()
            return user
        }
    }

    fun getDisplayName() : Component {
        val nicknameComponent = Component.text(nickname, NamedTextColor.WHITE)
        if (prefix == null) return nicknameComponent
        return Component.text("[$prefix] ").color(TextColor.color(0x13f832))
            .append(nicknameComponent)
    }

    fun update() : Boolean {
        val conn = Main.db.getConnection()!!
        val statement = conn.prepareStatement("UPDATE Users SET Nickname = ?, Prefix = ?, Money = ? WHERE Id = ?")
        statement.setString(4, uuid.toString())
        statement.setString(1, nickname)
        statement.setString(2, prefix)
        statement.setLong(3, money)

        return 0 < statement.executeUpdate()
    }
}