package net.gze1206.noruNexus.model

import net.gze1206.noruNexus.Main
import net.gze1206.noruNexus.core.ConfigManager
import net.gze1206.noruNexus.core.Database
import net.gze1206.noruNexus.event.UserGetTitleEvent
import java.sql.Connection

data class Title(
    val id : String?,
    val displayName : String,
    val rarity: Rarity,
    val lore: String,
    val globalBroadcast : Boolean
) {
    enum class Rarity(@Suppress("unused") val customModelId: Int, val color: String) {
        Normal(1, "#ffffff"),
        Rare(2, "#16f0f1"),
        Epic(3, "#da0bee"),
        Legendary(4, "#eec00b")
    }

    enum class TitleId(val key: String, val displayName: String, val lore: String, val rarity: Rarity, val globalBroadcast: Boolean) {
        RICH("000-rich", "부자", "꽤 많은 부를 축적한 사람에게 주어지는 칭호입니다.", Rarity.Rare, true),
        RIICH("001-riich", "부우자", "상당히 많은 부를 축적한 사람에게 주어지는 칭호입니다.", Rarity.Epic, true),
        RIIICH("002-riiich", "부우우자", "굉장히 많은 부를 축적한 사람에게 주어지는 칭호입니다.", Rarity.Legendary, true),

        HELLDIVER("003-helldiver", "헬다이버", "네더에 진입한 사람에게 주어지는 칭호입니다.", Rarity.Epic, true),

        TEST("999-test", "테스트", "테스트를 위한 칭호입니다.", Rarity.Normal, false);

        override fun toString() : String = this.key
    }

    companion object {
        fun initConfig() {
            ConfigManager.title.getConfig().run {
                options().copyDefaults(true)

                TitleId.entries.sortedBy { it.key }.forEach {
                    addDefault("titles.${it}.displayName", it.displayName)
                    addDefault("titles.${it}.lore", it.lore)
                    addDefault("titles.${it}.rarity", it.rarity.name)
                    addDefault("titles.${it}.globalBroadcast", it.globalBroadcast)
                }
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

        fun give(user: User, titleId: TitleId) : Boolean {
            val title = get(titleId.key) ?: return false

            val succeed = Database.query("SELECT * FROM UserTitles WHERE UserId = ? and TitleId = ?") {
                setString(1, user.uuid.toString())
                setString(2, titleId.key)

                val result = executeQuery()
                if (result.next()) return@query false

                connection.prepareStatement("INSERT INTO UserTitles (UserId, TitleId) VALUES (?, ?)").run {
                    setString(1, user.uuid.toString())
                    setString(2, titleId.key)

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