package com.tutorials.firstkmp.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.tutorials.firstkmp.database.NoteDatabase
import com.tutorials.firstkmp.domain.Note
import com.tutorials.firstkmp.domain.NoteDataSource
import com.tutorials.firstkmp.domain.NoteGroup
import com.tutorials.firstkmp.domain.toNote
import com.tutorials.firstkmp.domain.toNoteGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SqlDelightNoteDataSourceImpl(db: NoteDatabase) : NoteDataSource {
    private val query = db.noteQueries
    private val groupQuery = db.note_groupQueries

    override fun getAllNotes(): Flow<List<Note>> {
        val noteFlow = query.getAllNotes().asFlow().mapToList()
        return noteFlow.map {
            it.map { note ->
                Note(
                    id = note.id,
                    text = note.text,
                    noteType = note.noteType.toInt(),
                    groupUuid = note.groupUuid,
                    groupId = note.groupId,
                    isSelected = note.isSelected,
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
        return queryNote?.toNote()
    }


    override suspend fun insertNote(note: Note) {
        query.insertNote(
            id = note.id,
            text = note.text,
            noteType = note.noteType.toLong(),
            groupId=note.groupId,
            groupUuid=note.groupUuid,
            isSelected = note.isSelected,
            dateCreated = note.dateCreated
        )
    }

    override suspend fun deleteNoteById(id: Long) {
        query.deleteNoteById(id)
    }

    override suspend fun deleteAllNoteById(id: Long) {
        query.deleteAllNoteById(id)
    }

    override fun getAllNotesByGroup(uuid: Long): Flow<List<Note>> {
        val noteFlow = query.getAllNotesByGroupUuid(uuid).asFlow().mapToList()
        return noteFlow.map {
            it.map { note ->
                note.toNote()
            }
        }
    }

    override suspend fun unSelectNotes(setStateValue: Long, queryStateValue: Long) {
        query.unSelectNotes(setStateValue,queryStateValue)
    }

    override suspend fun deleteNoteInIdList(idList: List<Long>) {
        query.transaction {
            idList.forEach {
                query.deleteNoteById(it)
            }
        }
    }

    override suspend fun getNotesById(idList: List<Long>): List<Note> {
        val list = mutableListOf<Note>()
        /*query.transaction {
            idList.forEach {
                query.getNoteById(it).executeAsOneOrNull()?.toNote()?.let { note ->
                    list.add(note)
                }
            }
        }*/
        idList.forEach {
            getNoteById(it)?.let { note ->
                list.add(note)
            }
        }
        return list
    }

    //region NOTE GROUP
    override fun getAllNoteGroup(): Flow<List<NoteGroup>> {
        val noteGroupFlow = groupQuery.getAllNoteGroup().asFlow().mapToList()
        return noteGroupFlow.map {
            it.map { noteGroup ->
                NoteGroup(
                    id = noteGroup.id,
                    uuid = noteGroup.uuid,
                    title = noteGroup.title,
                    dateUpdated = noteGroup.dateUpdated,
                    dateCreated = noteGroup.dateCreated
                )
            }
        }
    }

    override suspend fun deleteAllNoteGroup() {
        groupQuery.deleteAllNoteGroup()
    }

    override suspend fun insertNoteGroup(noteGroup: NoteGroup) {
        groupQuery.insertNoteGroup(
            id = null,
            uuid = noteGroup.uuid,
            title = noteGroup.title,
            dateUpdated = noteGroup.dateUpdated,
            dateCreated = noteGroup.dateCreated
        )

    }

    override suspend fun deleteNoteGroupById(id: Long) {
        groupQuery.deleteNoteGroupById(id)
    }

    override fun getNoteGroupByUuid(uuid: Long): NoteGroup? {
        val queryNoteGroup = groupQuery.getNoteGroupByUuid(uuid).executeAsOneOrNull()
        return queryNoteGroup?.toNoteGroup()

    }

    override suspend fun updateNoteGroup(noteGroup: NoteGroup) {
        groupQuery.insertNoteGroup(
            id = noteGroup.id,
            uuid = noteGroup.uuid,
            title = noteGroup.title,
            dateUpdated = noteGroup.dateUpdated,
            dateCreated = noteGroup.dateCreated
        )
    }

    //endregion
}