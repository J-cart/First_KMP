package com.tutorials.firstkmp.domain

import kotlinx.coroutines.flow.Flow

interface NoteDataSource {

    fun getAllNotes(): Flow<List<Note>>
    fun getAllNotesByGroup(uuid:Long): Flow<List<Note>>

    suspend fun deleteAllNotes()

    suspend fun getNoteById(id: Long): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNoteById(id: Long)

    suspend fun deleteAllNoteById(id: Long)

    suspend fun unSelectNotes(setStateValue: Long, queryStateValue: Long)



    fun getAllNoteGroup():Flow<List<NoteGroup>>

    fun getNoteGroupByUuid(uuid: Long):NoteGroup?

    suspend fun deleteAllNoteGroup()

    suspend fun insertNoteGroup(noteGroup: NoteGroup)

    suspend fun updateNoteGroup(noteGroup: NoteGroup)

    suspend fun deleteNoteGroupById(id: Long)
}