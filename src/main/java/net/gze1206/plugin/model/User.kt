package net.gze1206.plugin.model

import net.gze1206.plugin.Main
import net.gze1206.plugin.event.UserMoneyUpdateEvent
import net.gze1206.plugin.gui.ScoreboardBuilder
import net.gze1206.plugin.utils.not
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
        fun createTable(conn: Connection) {
            val statement = conn.createStatement()
            statement.execute("""CREATE TABLE IF NOT EXISTS Users (
                |    Id TEXT PRIMARY KEY,
                |    Nickname TEXT,
                |    Title TEXT,
                |    Money INTEGER
                |)""".trimMargin())
            statement.close()
        }

        fun create(player: Player) : User? {
            var user : User? = User(player.uniqueId, player.name, null, 0L)

            Main.db.query("INSERT INTO Users (Id, Nickname, Title, Money) VALUES (?, ?, ?, ?)") {
                val uuid = user!!.uuid
                val nickname = user!!.nickname
                val title = user!!.title
                val money = user!!.money

                it.setString(1, uuid.toString())
                it.setString(2, nickname)
                it.setString(3, title)
                it.setLong(4, money)

                val effected = it.executeUpdate()

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
                it.setString(1, player.uniqueId.toString())

                val result = it.executeQuery()
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

    private val scoreboardBuilder = ScoreboardBuilder()

    private fun update() : Boolean {
        var succeed = false
        Main.db.query("UPDATE Users SET Nickname = ?, Title = ?, Money = ? WHERE Id = ?") {
            it.setString(4, uuid.toString())
            it.setString(1, nickname)
            it.setString(2, title)
            it.setLong(3, money)

            succeed = 0 < it.executeUpdate()
            Main.log!!.info("유저 정보 갱신 ${if (succeed) "성공" else "실패"} [$uuid,$nickname,$title,$money]")

            return@query true
        }

        return succeed
    }

    fun transaction(block: (User) -> Unit) : Boolean {
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

    fun updateScoreboard(player: Player) {
        val title = if (title == null) null else Title.get(title!!)
        val displayPrefix = title?.displayName ?: this.title
        val color = title?.color ?: "#ffffff"

        scoreboardBuilder.let {
            it[1] = !"칭호 : " + if (this.title == null) !"(없음)" else Component.text("[$displayPrefix]", TextColor.fromHexString(color))
            it[2] = !"소지금 : ${money}원"

            player.scoreboard = it.scoreboard()
        }
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