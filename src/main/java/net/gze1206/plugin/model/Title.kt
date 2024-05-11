package net.gze1206.plugin.model

import net.gze1206.plugin.Main
import net.gze1206.plugin.core.ConfigManager
import java.sql.Connection

data class Title(
    val id : String?,
    val displayName : String,
    val lore: String,
    val color : String
) {
    companion object {
        fun initConfig() {
            ConfigManager.title.getConfig().let {
                it.options().copyDefaults(true)

                it.addDefault("titles.test.displayName", "테스트")
                it.addDefault("titles.test.lore", "테스트를 위한 칭호입니다.")
                it.addDefault("titles.test.color", "#16f0f1")
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
                config.getString("titles.${id}.color")!!
            )
        }

        fun give(user: User, titleId: String) {
            get(titleId) ?: return

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

                return@query true
            }
        }

        fun getOwnTitles(user: User?) : ArrayList<Title> {
            val titles = arrayListOf<Title>()
            titles.add(Title(null, "(없음)", "칭호를 해제합니다.", "#ffffff"))

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