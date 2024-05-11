package net.gze1206.noruNexus.model

import net.gze1206.noruNexus.Main
import net.gze1206.noruNexus.core.ConfigManager
import net.gze1206.noruNexus.core.Constants.TEST_TITLE_ID
import net.gze1206.noruNexus.core.Database
import net.gze1206.noruNexus.event.UserGetTitleEvent
import net.gze1206.noruNexus.event.UserMoneyUpdateEvent
import org.bukkit.configuration.file.FileConfiguration
import java.sql.Connection

data class Title(
    val id : String?,
    val displayName : String,
    val rarity: Rarity,
    val lore: String,
    val globalBroadcast : Boolean
) {
    enum class Rarity(val customModelId: Int, val color: String) {
        Normal(1, "#ffffff"),
        Rare(2, "#16f0f1"),
        Epic(3, "#da0bee"),
        Legendary(4, "#eec00b")
    }

    companion object {
        fun initConfig() {
            fun FileConfiguration.addTitle(id: String, displayName: String, lore: String, rarity: Rarity, globalBroadcast: Boolean = false) {
                addDefault("titles.${id}.displayName", displayName)
                addDefault("titles.${id}.lore", lore)
                addDefault("titles.${id}.rarity", rarity.name)
                addDefault("titles.${id}.globalBroadcast", globalBroadcast)
            }

            ConfigManager.title.getConfig().run {
                options().copyDefaults(true)

                addTitle(TEST_TITLE_ID, "테스트", "테스트를 위한 칭호입니다.", Rarity.Normal)
                addTitle(UserMoneyUpdateEvent.RICH, "부자", "꽤 많은 부를 축적한 사람에게 주어지는 칭호입니다.", Rarity.Rare, true)
                addTitle(UserMoneyUpdateEvent.RIICH, "부우자", "상당히 많은 부를 축적한 사람에게 주어지는 칭호입니다.", Rarity.Epic, true)
                addTitle(UserMoneyUpdateEvent.RIIICH, "부우우자", "굉장히 많은 부를 축적한 사람에게 주어지는 칭호입니다.", Rarity.Legendary, true)
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
                Rarity.valueOf(config.getString("titles.${id}.rarity")!!),
                config.getString("titles.${id}.lore")!!,
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

        fun getOwnTitles(user: User) : MutableList<Title> {
            val titles = mutableListOf<Title>()
            titles.add(Title(null, "(없음)", Rarity.Normal, "칭호를 해제합니다.", false))

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

            titles.sortBy { it.id }
            return titles
        }
    }
}