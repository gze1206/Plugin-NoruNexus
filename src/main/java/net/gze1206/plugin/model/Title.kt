package net.gze1206.plugin.model

import net.gze1206.plugin.Main
import net.gze1206.plugin.core.ConfigManager
import net.gze1206.plugin.event.UserGetTitleEvent
import net.gze1206.plugin.event.UserMoneyUpdateEvent
import org.bukkit.configuration.file.FileConfiguration
import java.sql.Connection

data class Title(
    val id : String?,
    val displayName : String,
    val lore: String,
    val color : String,
    val globalBroadcast : Boolean
) {
    companion object {
        fun initConfig() {

            fun FileConfiguration.addTitle(id: String, displayName: String, lore: String, color: String, globalBroadcast: Boolean = false) {
                addDefault("titles.${id}.displayName", displayName)
                addDefault("titles.${id}.lore", lore)
                addDefault("titles.${id}.color", color)
                addDefault("titles.${id}.globalBroadcast", globalBroadcast)
            }

            ConfigManager.title.getConfig().let {
                it.options().copyDefaults(true)

                it.addTitle("test", "테스트", "테스트를 위한 칭호입니다.", "#16f0f1")
                it.addTitle(UserMoneyUpdateEvent.RICH, "부자", "꽤 많은 부를 축적한 사람에게 주어지는 칭호입니다.", "#16f0f1", true)
                it.addTitle(UserMoneyUpdateEvent.RIICH, "부우자", "상당히 많은 부를 축적한 사람에게 주어지는 칭호입니다.", "#da0bee", true)
                it.addTitle(UserMoneyUpdateEvent.RIIICH, "부우우자", "굉장히 많은 부를 축적한 사람에게 주어지는 칭호입니다.", "#eec00b", true)
            }
        }

        fun createTable(conn: Connection) {
            val statement = conn.createStatement()
            statement.execute("""CREATE TABLE IF NOT EXISTS UserTitles (
                |    UserId TEXT,
                |    TitleId TEXT
                |)""".trimMargin())
            statement.execute("CREATE UNIQUE INDEX IF NOT EXISTS UserTitleIdx ON UserTitles (UserId, TitleId)")
            statement.close()
        }

        fun get(id: String) : Title? {
            val config = ConfigManager.title.getConfig()
            if (!config.contains("titles.$id")) return null

            return Title(
                id,
                config.getString("titles.${id}.displayName")!!,
                config.getString("titles.${id}.lore")!!,
                config.getString("titles.${id}.color")!!,
                config.getBoolean("titles.${id}.globalBroadcast")
            )
        }

        fun give(user: User, titleId: String) : Boolean {
            val title = get(titleId) ?: return false

            var succeed = false
            Main.db.query("SELECT * FROM UserTitles WHERE UserId = ? and TitleId = ?") {
                it.setString(1, user.uuid.toString())
                it.setString(2, titleId)

                val result = it.executeQuery()
                if (result.next()) return@query false

                val query = it.connection!!.prepareStatement("INSERT INTO UserTitles (UserId, TitleId) VALUES (?, ?)")
                query.setString(1, user.uuid.toString())
                query.setString(2, titleId)

                val effected = query.executeUpdate()
                if (effected < 1) {
                    Main.log!!.severe("행이 추가되지 않았습니다. [${user.uuid},$titleId]")
                    return@query false
                }
                succeed = true

                return@query true
            }

            if (succeed) {
                UserGetTitleEvent(user, title, false).callEvent()
            }

            return succeed
        }

        fun getOwnTitles(user: User?) : ArrayList<Title> {
            val titles = arrayListOf<Title>()
            titles.add(Title(null, "(없음)", "칭호를 해제합니다.", "#ffffff", false))

            if (user != null) {
                Main.db.query("SELECT * FROM UserTitles WHERE UserId = ?") {
                    it.setString(1, user.uuid.toString())

                    val result = it.executeQuery()
                    while (result.next()) {
                        val titleId = result.getString(2)
                        val title = get(titleId) ?: continue
                        titles.add(title)
                    }
                    return@query true
                }
            }

            return titles
        }
    }
}