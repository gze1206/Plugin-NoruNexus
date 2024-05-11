package net.gze1206.noruNexus.model

import net.gze1206.noruNexus.Main
import net.gze1206.noruNexus.core.ConfigManager
import net.gze1206.noruNexus.core.Database
import net.gze1206.noruNexus.event.UserGetTitleEvent
import net.gze1206.noruNexus.event.UserMoneyUpdateEvent
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

            ConfigManager.title.getConfig().run {
                options().copyDefaults(true)

                addTitle("test", "테스트", "테스트를 위한 칭호입니다.", "#16f0f1")
                addTitle(UserMoneyUpdateEvent.RICH, "부자", "꽤 많은 부를 축적한 사람에게 주어지는 칭호입니다.", "#16f0f1", true)
                addTitle(UserMoneyUpdateEvent.RIICH, "부우자", "상당히 많은 부를 축적한 사람에게 주어지는 칭호입니다.", "#da0bee", true)
                addTitle(UserMoneyUpdateEvent.RIIICH, "부우우자", "굉장히 많은 부를 축적한 사람에게 주어지는 칭호입니다.", "#eec00b", true)
            }
        }

        fun createTable(conn: Connection) {
            conn.createStatement().run {
                execute("""CREATE TABLE IF NOT EXISTS UserTitles (
                    |    UserId TEXT,
                    |    TitleId TEXT
                    |)""".trimMargin())
                execute("CREATE UNIQUE INDEX IF NOT EXISTS UserTitleIdx ON UserTitles (UserId, TitleId)")
                close()
            }
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

            val succeed = Database.query("SELECT * FROM UserTitles WHERE UserId = ? and TitleId = ?") {
                setString(1, user.uuid.toString())
                setString(2, titleId)

                val result = executeQuery()
                if (result.next()) return@query false

                connection.prepareStatement("INSERT INTO UserTitles (UserId, TitleId) VALUES (?, ?)").run {
                    setString(1, user.uuid.toString())
                    setString(2, titleId)

                    val effected = executeUpdate()
                    if (effected < 1) {
                        Main.getLog().severe("행이 추가되지 않았습니다. [${user.uuid},$titleId]")
                        return@query false
                    }
                    return@query true
                }
            }

            if (succeed) {
                UserGetTitleEvent(user, title, false).callEvent()
            }

            return succeed
        }

        fun getOwnTitles(user: User) : ArrayList<Title> {
            val titles = arrayListOf<Title>()
            titles.add(Title(null, "(없음)", "칭호를 해제합니다.", "#ffffff", false))

            Database.query("SELECT * FROM UserTitles WHERE UserId = ?") {
                setString(1, user.uuid.toString())

                val result = executeQuery()
                while (result.next()) {
                    val titleId = result.getString(2)
                    val title = get(titleId) ?: continue
                    titles.add(title)
                }
                return@query true
            }

            return titles
        }
    }
}