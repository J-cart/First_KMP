package com.tutorials.firstkmp.presentation.screen

import androidx.compose.runtime.Composable
import com.tutorials.firstkmp.domain.NoteDataSource
import com.tutorials.firstkmp.presentation.PreComposeNavHost
import com.tutorials.firstkmp.presentation.SharedViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import moe.tlaster.precompose.PreComposeApp

@Composable
fun MainView(noteDataSource: NoteDataSource) {
    val sharedViewModel =
        getViewModel(key = Unit, viewModelFactory { SharedViewModel(noteDataSource) })

    PreComposeApp {
        PreComposeNavHost(sharedViewModel)
    }

}
