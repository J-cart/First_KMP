package com.tutorials.firstkmp.domain

data class NoteGroup(
    val id:Long?=0L,
    val uuid:Long = 0L,
    val title:String = "",
    val dateCreated:String ="",
    val dateUpdated:String = ""
)