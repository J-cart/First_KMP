package com.tutorials.firstkmp.domain

import database.NoteEntity
import database.NoteGroupEntity

fun NoteEntity.toNote(): com.tutorials.firstkmp.domain.Note {
    return com.tutorials.firstkmp.domain.Note(
        id = id,
        title = title,
        desc = desc,
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