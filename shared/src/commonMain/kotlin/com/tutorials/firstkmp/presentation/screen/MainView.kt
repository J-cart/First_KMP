package com.tutorials.firstkmp.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.tutorials.firstkmp.domain.NoteDataSource
import com.tutorials.firstkmp.presentation.MyChildStack
import com.tutorials.firstkmp.presentation.Screen
import com.tutorials.firstkmp.presentation.SharedViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun MainView(noteDataSource: NoteDataSource) {
    val sharedViewModel =
        getViewModel(key = Unit, viewModelFactory { SharedViewModel(noteDataSource) })

    val navigator = remember { StackNavigation<Screen>() }

    MyChildStack(
        source = navigator,
        initialStack = { listOf(Screen.HomeScreen) },
        handleBackButton = true,
        animation = stackAnimation(fade() + scale())
    ) { screen ->
        when (screen) {
            Screen.HomeScreen -> {
                NoteHomeScreen(
                    onViewGroupNavigate = {
                        navigator.push(Screen.GroupNotesScreen(it))
                    },
                    onAddGroupNavigate = {
                        navigator.push(Screen.AddEditNoteScreen())
                    },
                    sharedViewModel = sharedViewModel
                )
            }

            is Screen.AddEditNoteScreen -> {
                AddEditNoteGroup(
                    noteGroup = screen.noteGroup,
                    sharedViewModel = sharedViewModel,
                    onNavigate = {
                        navigator.replaceCurrent(Screen.GroupNotesScreen(it))
                    },
                    onDeleteNavigate = {
                        navigator.replaceAll(Screen.HomeScreen)
                    },
                    onEditNavigate = {
                        navigator.pop()
                    },
                    onNavigateUp = {
                        navigator.pop()
                    }
                )
            }

            is Screen.GroupNotesScreen -> {
                NoteGroupItemScreen(
                    sharedViewModel = sharedViewModel,
                    groupUuid = screen.groupUuid,
                    onNavigateUp = {
                        navigator.pop()
                    },
                    onEditNavigate = {
                        navigator.push(Screen.AddEditNoteScreen(it))
                    }
                )
            }
        }
    }

//            Navigator(HomeScreen(noteDataSource, sharedViewModel))
}
