package com.tutorials.firstkmp.presentation

import com.tutorials.firstkmp.domain.Note

data class NoteUiState(
     val noteList: List<Note> = emptyList(),
     val note:Note? = null
)