package net.gze1206.plugin.core

import net.gze1206.plugin.Main
import net.gze1206.plugin.model.Title
import net.gze1206.plugin.model.User
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class Database {
    companion object {
        const val DB_PATH = "${Constants.DATA_DIR}/database.db"
    }

    fun init() {
        Class.forName("org.sqlite.JDBC")
        val dataDir = File(Constants.DATA_DIR)
        if (!dataDir.exists()) dataDir.mkdirs()

        createTables()
    }

    fun query(sql: String, body: PreparedStatement.() -> Boolean) : Boolean {
        val conn = Main.db.getConnection(false)
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
            Main.log!!.severe("SQL FAILED : ${e.message}")
            conn.rollback()
            return false
        } finally {
            statement.close()
            conn.close()
        }
    }

    private fun createTables() {
        val conn = getConnection()
        User.createTable(conn)
        Title.createTable(conn)
        conn.close()
    }

    private fun getConnection(autoCommit: Boolean = true) : Connection {
        val conn = DriverManager.getConnection("jdbc:sqlite:$DB_PATH")
        conn.autoCommit = autoCommit
        return conn
    }
}