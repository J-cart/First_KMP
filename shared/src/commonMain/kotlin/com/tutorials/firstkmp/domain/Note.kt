package com.tutorials.firstkmp.domain

data class Note(
    val id:Long = 0L,
    val groupId:Long = 0L,
    val groupUuid:Long = 0L,
    val title:String= "",
    val desc:String = "",
    val dateCreated:Long = 0L
)
