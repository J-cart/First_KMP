package com.tutorials.firstkmp.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.tutorials.firstkmp.domain.NoteDataSource
import com.tutorials.firstkmp.presentation.SharedViewModel
import com.tutorials.firstkmp.presentation.ui.theme.NoteTheme
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun MainView(noteDataSource: NoteDataSource) {
    val sharedViewModel =
        getViewModel(key = Unit, viewModelFactory { SharedViewModel(noteDataSource) })
    NoteTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Navigator(HomeScreen(noteDataSource,sharedViewModel))
        }

    }
}