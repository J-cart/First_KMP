package com.tutorials.firstkmp.core

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.tutorials.firstkmp.database.NoteDatabase

actual class DatabaseDriverFactory() {
   actual fun createDriver():SqlDriver = NativeSqliteDriver(
        NoteDatabase.Schema,
        "note.db"
    )
}