package com.tutorials.firstkmp.domain

import database.NoteEntity
import database.NoteGroupEntity

fun NoteEntity.toNote(): com.tutorials.firstkmp.domain.Note {
    return com.tutorials.firstkmp.domain.Note(
        id = id,
        text = text,
        media = media,
        noteType = noteType.toInt(),
        groupId = groupId,
        groupUuid = groupUuid,
        isSelected = isSelected,
        dateCreated = dateCreated
    )
}
fun NoteGroupEntity.toNoteGroup(): NoteGroup {
    return NoteGroup(
        id = id,
        title = title,
        uuid = uuid,
        dateCreated = dateCreated,
        dateUpdated = dateUpdated
    )
}


object NoteType {
    const val DATE = 0
    const val TEXT = 1
    const val IMAGE = 2
    const val AUDIO = 3
    const val VIDEO = 4
    const val FILE = 5
    const val LOCATION = 6
    const val LINK = 7

    fun canBeCopied(type: Int): Boolean {
        return (type == TEXT || type == LOCATION || type == LINK)
    }
}