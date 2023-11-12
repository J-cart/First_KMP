package com.tutorials.firstkmp.presentation

import com.tutorials.firstkmp.domain.Note
import com.tutorials.firstkmp.domain.NoteDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SharedViewModel(private val noteDataSource: NoteDataSource) : ViewModel() {

    var allNotesState = MutableStateFlow<NoteUiState>(NoteUiState())
        private set

    var noteState = MutableStateFlow<NoteUiState>(NoteUiState())
        private set


    fun loadAllNotes() {
        viewModelScope.launch {
            noteDataSource.getAllNotes().collect { note ->
                allNotesState.update { it.copy(noteList = note) }
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

    fun addNote(note: Note){
        viewModelScope.launch {
            noteDataSource.insertNote(note)
        }
    }

    fun deleteNote(id: Long){
        viewModelScope.launch {
            noteDataSource.deleteNoteById(id)
        }
    }


}