package com.tutorials.firstkmp.presentation.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.tutorials.firstkmp.domain.NoteDataSource
import com.tutorials.firstkmp.presentation.ui.theme.NoteTheme

@Composable
fun MainView(noteDataSource: NoteDataSource) {
    NoteTheme {
        Navigator(HomeScreen(noteDataSource))
    }
}