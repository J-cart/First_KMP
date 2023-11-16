package com.tutorials.firstkmp.presentation

import com.tutorials.firstkmp.domain.NoteGroup

data class NoteGroupUiState(
     val groupList: List<NoteGroup> = emptyList(),
     val noteGroup: NoteGroup? = null
)