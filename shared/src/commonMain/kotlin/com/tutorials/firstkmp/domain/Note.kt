package com.tutorials.firstkmp.domain

data class Note(
    val id:Long = 0L,
    val groupId:Long = 0L,
    val groupUuid:Long = 0L,
    val text:String= "",
    val isSelected:Long = 0L,
    val noteType:Int = NoteType.TEXT,
    val dateCreated:Long = 0L
)
