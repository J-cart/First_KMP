package com.tutorials.firstkmp.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.tutorials.firstkmp.database.NoteDatabase
import com.tutorials.firstkmp.domain.Note
import com.tutorials.firstkmp.domain.NoteDataSource
import com.tutorials.firstkmp.domain.toNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SqlDelightNoteDataSourceImpl(db: NoteDatabase) : NoteDataSource {
    private val query = db.noteQueries
    override fun getAllNotes(): Flow<List<Note>> {
        val noteFlow = query.getAllNotes().asFlow().mapToList()
        return noteFlow.map {
            it.map { note ->
                Note(
                    id = note.id,
                    title = note.title,
                    desc = note.desc,
                    dateCreated = note.dateCreated
                )
            }
        }
    }

    override suspend fun deleteAllNotes() {
        query.deleteAllNote()
    }

    override suspend fun getNoteById(id: Long): Note? {
        val queryNote = query.getNoteById(id).executeAsOneOrNull()
        val note = queryNote?.let {
            Note(
                id = it.id,
                title = it.title,
                desc = it.desc,
                dateCreated = it.dateCreated
            )
        }
        return queryNote?.toNote()//note
    }


    override suspend fun insertNote(note: Note) {
        query.insertNote(
            id = note.id,
            title = note.title,
            desc = note.desc,
            dateCreated = note.dateCreated
        )
    }

    override suspend fun deleteNoteById(id: Long) {
        query.deleteNoteById(id)
    }
}