package com.tutorials.firstkmp.domain

import kotlinx.coroutines.flow.Flow

interface NoteDataSource {

    fun getAllNotes(): Flow<List<Note>>

    suspend fun deleteAllNotes()

    suspend fun getNoteById(id: Long): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNoteById(id: Long)
}