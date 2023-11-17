package com.tutorials.firstkmp.domain

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
data class NoteGroup(
    val id:Long?=0L,
    val uuid:Long = 0L,
    val title:String = "",
    val dateCreated:String ="",
    val dateUpdated:String = ""
):Parcelable