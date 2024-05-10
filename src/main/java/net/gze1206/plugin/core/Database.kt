package net.gze1206.plugin.core

import net.gze1206.plugin.model.User
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class Database {
    companion object {
        val DB_PATH = "${Constants.DATA_DIR}/database.db"
    }

    private var connection : Connection? = null

    fun connect() {
        Class.forName("org.sqlite.JDBC")
        val dataDir = File(Constants.DATA_DIR)
        if (!dataDir.exists()) dataDir.mkdirs()

        val conn = DriverManager.getConnection("jdbc:sqlite:$DB_PATH")
        connection = conn
        createTables()
    }

    fun close() {
        connection?.close()
    }

    fun getConnection() : Connection? {
        return connection
    }

    private fun createTables() {
        User.createTable()
    }
}