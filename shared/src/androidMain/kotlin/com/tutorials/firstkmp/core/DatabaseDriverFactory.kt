package com.tutorials.firstkmp.core

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.tutorials.firstkmp.database.NoteDatabase

actual class DatabaseDriverFactory(private val context: Context) {
   actual fun createDriver():SqlDriver = AndroidSqliteDriver(
        NoteDatabase.Schema,
        context,
        "note.db"
    )
}