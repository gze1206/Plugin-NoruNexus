package net.gze1206.plugin.model

import net.gze1206.plugin.Main
import net.gze1206.plugin.event.UserMoneyUpdateEvent
import net.gze1206.plugin.gui.ScoreboardBuilder
import net.gze1206.plugin.utils.plus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import java.sql.Connection
import java.util.UUID

data class User (
    val uuid : UUID,
    var nickname: String,
    var title : String?,
    var money: Long,
) {
    companion object {
        val scoreboardBuilder = ScoreboardBuilder()

        fun createTable(conn: Connection) {
            conn.createStatement().run {
                execute("""CREATE TABLE IF NOT EXISTS Users (
                    |    Id TEXT PRIMARY KEY,
                    |    Nickname TEXT,
                    |    Title TEXT,
                    |    Money INTEGER
                    |)""".trimMargin())
                close()
            }
        }

        fun create(player: Player) : User? {
            var user : User? = User(player.uniqueId, player.name, null, 0L)

            Main.db.query("INSERT INTO Users (Id, Nickname, Title, Money) VALUES (?, ?, ?, ?)") {
                val uuid = user!!.uuid
                val nickname = user!!.nickname
                val title = user!!.title
                val money = user!!.money

                setString(1, uuid.toString())
                setString(2, nickname)
                setString(3, title)
                setLong(4, money)

                val effected = executeUpdate()

                if (effected <= 0) {
                    Main.log!!.severe("행이 추가되지 않았습니다. [$uuid,$nickname,$title,$money]")
                    user = null
                    return@query false
                }

                return@query true
            }

            return user
        }

        fun get(player: Player) : User? {
            var user : User? = null
            Main.db.query("SELECT * FROM Users WHERE Id = ?") {
                setString(1, player.uniqueId.toString())

                val result = executeQuery()
                if (!result.next())
                    return@query true

                user = User(
                    UUID.fromString(result.getString(1)),
                    result.getString(2),
                    result.getString(3),
                    result.getLong(4))

                return@query true
            }

            return user
        }
    }

    private fun update() : Boolean {
         return Main.db.query("UPDATE Users SET Nickname = ?, Title = ?, Money = ? WHERE Id = ?") {
            setString(4, uuid.toString())
            setString(1, nickname)
            setString(2, title)
            setLong(3, money)

            val succeed = 0 < executeUpdate()
            Main.log!!.info("유저 정보 갱신 ${if (succeed) "성공" else "실패"} [$uuid,$nickname,$title,$money]")

            return@query succeed
        }
    }

    fun transaction(block: User.() -> Unit) : Boolean {
        val nickname = nickname
        val title = title
        val money = money

        block(this)

        if (nickname == this.nickname && title == this.title && money == this.money)
            return false

        if (update()) {
            if (money != this.money) {
                UserMoneyUpdateEvent(this, this.money, false).callEvent()
            }
            return true
        }

        this.nickname = nickname
        this.title = title
        this.money = money
        return false
    }

    fun getDisplayName() : Component {
        val nicknameComponent = Component.text(nickname, NamedTextColor.WHITE)
        if (title == null) return nicknameComponent

        val title = Title.get(title!!)
        val displayPrefix = title?.displayName ?: this.title
        val color = title?.color ?: "#ffffff"
        return Component.text("[$displayPrefix] ", TextColor.fromHexString(color)) + nicknameComponent
    }
}