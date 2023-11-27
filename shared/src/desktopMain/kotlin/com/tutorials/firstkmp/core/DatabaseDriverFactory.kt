package com.tutorials.firstkmp.core

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.tutorials.firstkmp.database.NoteDatabase
import java.io.File


actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val fileDir = File("${System.getProperty("user.dir")}","FirstKMP.db")
        val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${fileDir.absolutePath}")
        try {
            NoteDatabase.Schema.create(driver)
        }catch (e:Exception){
            NoteDatabase(driver)
        }
        return driver
    }
}