package com.tutorials.firstkmp.presentation

import com.tutorials.firstkmp.domain.Note
import com.tutorials.firstkmp.domain.NoteDataSource
import com.tutorials.firstkmp.domain.NoteGroup
import com.tutorials.firstkmp.domain.NoteType
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SharedViewModel(private val noteDataSource: NoteDataSource) : ViewModel() {

    var allNotesState = MutableStateFlow<NoteUiState>(NoteUiState())
        private set

    var noteState = MutableStateFlow<NoteUiState>(NoteUiState())
        private set

    var noteImageState = MutableStateFlow<NoteUiState>(NoteUiState())
        private set

    var allNoteGroupState = MutableStateFlow<NoteGroupUiState>(NoteGroupUiState())
        private set

    var noteGroupState = MutableStateFlow<NoteGroupUiState>(NoteGroupUiState())
        private set


    fun loadAllNotes() {
        viewModelScope.launch {
            noteDataSource.getAllNotes().collect { note ->
                allNotesState.update { it.copy(noteList = note) }
            }
        }
    }

    fun loadAllNotesByGroup(uuid: Long) {
        viewModelScope.launch {
            noteDataSource.getAllNotesByGroup(uuid).collect { note ->
                allNotesState.update { it.copy(noteList = note) }
                noteImageState.update { it.copy(noteList = note.filter { mNote -> mNote.noteType == NoteType.IMAGE }) }
            }
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch { noteDataSource.deleteAllNotes() }
    }

    fun getNoteById(id: Long) {
        viewModelScope.launch {
            noteState.update {
                it.copy(note = noteDataSource.getNoteById(id))
            }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteDataSource.insertNote(note)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            noteDataSource.deleteNoteById(id)
        }
    }

    private fun deleteAllNoteById(id: Long) {
        viewModelScope.launch {
            noteDataSource.deleteAllNoteById(id)
        }
    }

    fun deleteNotesInIdList(idList: List<Long>) {
        viewModelScope.launch {
            noteDataSource.deleteNoteInIdList(idList)
        }
    }

    suspend fun getNotesById(idList: List<Long>) = noteDataSource.getNotesById(idList)


    fun clearSelection() {
        viewModelScope.launch {
            noteDataSource.unSelectNotes(setStateValue = 0L, queryStateValue = 1L)
        }
    }


    fun loadAllNoteGroup() {
        viewModelScope.launch {
            noteDataSource.getAllNoteGroup().collect { noteGroup ->
                allNoteGroupState.update { it.copy(groupList = noteGroup) }
            }
        }
    }

    fun deleteAllNoteGroup() {
        viewModelScope.launch { noteDataSource.deleteAllNoteGroup() }
    }


    fun addNoteGroup(noteGroup: NoteGroup) {
        viewModelScope.launch {
            noteDataSource.insertNoteGroup(noteGroup)
        }
    }

    fun updateNoteGroup(noteGroup: NoteGroup) {
        viewModelScope.launch {
            noteDataSource.updateNoteGroup(noteGroup)
        }
    }

    fun deleteNoteGroup(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDataSource.deleteNoteGroupById(id)
            deleteAllNoteById(id)
        }
    }

    fun getNoteGroupByUuid(uuid: Long) {
        viewModelScope.launch {
            noteGroupState.update {
                it.copy(noteGroup = noteDataSource.getNoteGroupByUuid(uuid))
            }
        }
    }


}