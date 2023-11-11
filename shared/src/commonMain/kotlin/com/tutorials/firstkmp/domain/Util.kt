package com.tutorials.firstkmp.domain

import database.NoteEntity

fun NoteEntity.toNote(): com.tutorials.firstkmp.domain.Note {
    return com.tutorials.firstkmp.domain.Note(
        id = id,
        title = title,
        desc = desc,
        dateCreated = dateCreated
    )
}