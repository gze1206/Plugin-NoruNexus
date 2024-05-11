package net.gze1206.noruNexus.core

import net.gze1206.noruNexus.Main
import net.gze1206.noruNexus.model.Title
import net.gze1206.noruNexus.model.User
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import kotlin.io.path.Path

object Database {
    private val PATH = Main.getInstance().dataFolder.absolutePath
    private val DB_PATH = Path(PATH, "database.db").toAbsolutePath()

    fun init() {
        Class.forName("org.sqlite.JDBC")
        val dataDir = File(PATH)
        if (!dataDir.exists()) dataDir.mkdirs()

        createTables()
    }

    fun query(sql: String, body: PreparedStatement.() -> Boolean) : Boolean {
        val conn = getConnection(false)
        val statement = conn.prepareStatement(sql)

        try {
            return if (body(statement)) {
                conn.commit()
                true
            } else {
                conn.rollback()
                false
            }
        } catch (e: Exception) {
            Main.getLog().severe("SQL FAILED : ${e.message}")
            conn.rollback()
            return false
        } finally {
            statement.close()
            conn.close()
        }
    }

    private fun createTables() {
        getConnection().run {
            User.createTable(this)
            Title.createTable(this)
            close()
        }
    }

    private fun getConnection(autoCommit: Boolean = true) : Connection {
        val conn = DriverManager.getConnection("jdbc:sqlite:$DB_PATH")
        conn.autoCommit = autoCommit
        return conn
    }
}