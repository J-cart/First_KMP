package com.tutorials.firstkmp.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.touchlab.kermit.Logger
import com.tutorials.firstkmp.domain.NoteGroup
import com.tutorials.firstkmp.domain.time.DateTimeUtil
import com.tutorials.firstkmp.presentation.SharedViewModel
import kotlinx.datetime.Clock

data class AddEditNoteGroupScreen(private val sharedViewModel: SharedViewModel,private val noteGroup: NoteGroup? = null) :Screen{
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        AddEditNoteGroup(onNavigateUp = {
            navigator.pop()
        }, onNavigate = {
            navigator.replace(GroupNotesScreen(groupUuid = it, sharedViewModel))
        }, sharedViewModel = sharedViewModel,
            noteGroup = noteGroup, onEditNavigate = {
                navigator.pop()
            }, onDeleteNavigate = {
                /*
               --also works--
                navigator.popUntil {
                    it is HomeScreen
                }*/
                navigator.popUntilRoot()
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteGroup(noteGroup: NoteGroup? = null,sharedViewModel: SharedViewModel,onNavigateUp:()->Unit, onNavigate:(Long)->Unit, onEditNavigate:()->Unit, onDeleteNavigate:()->Unit) {
    var noteGroupTitle by remember {
        mutableStateOf("")
    }


    LaunchedEffect(Unit){
        noteGroup?.let {
            noteGroupTitle = it.title
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "New note group", modifier = Modifier.padding(start = 10.dp)) },
            navigationIcon = {
                Icon(modifier = Modifier.clickable { onNavigateUp() },
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back"
                )
            },
            actions = {

                noteGroup?.id?.let {
                    IconButton(onClick = {
                        /*TODO: delete note group*/
                        sharedViewModel.deleteNoteGroup(it)
                        onDeleteNavigate()
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }

            }
        )
    }, floatingActionButton = {
        FloatingActionButton(
            shape = CircleShape,
            onClick = { /*TODO: navigate to note group items*/
                noteGroup?.let {
                    sharedViewModel.updateNoteGroup(
                        it.copy(
                            title = noteGroupTitle,
                            dateUpdated = DateTimeUtil.formatDateTimeToday()
                        )
                    )
                    onEditNavigate()
                }?: addGroup(noteGroupTitle, sharedViewModel){
                    onNavigate(it)
                }

            }) {
            Icon(imageVector = Icons.Default.Check, contentDescription = "Continue")
        }
    }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    modifier = Modifier
                        .size(56.dp)
                        .background(color = Color.LightGray, shape = CircleShape)
                        .clickable {
                            // TODO: select group image
                        }
                        .padding(4.dp)
                        .align(Alignment.Bottom),
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Clear"
                )

                Spacer(modifier = Modifier.width(6.dp))
                TextField(
                    value = noteGroupTitle,
                    onValueChange = {
                        noteGroupTitle = it
                    },
                    placeholder = {
                        Text(text = "Note - ${DateTimeUtil.formatDateTimeTodayForTitle() }", color = Color.LightGray)
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .border(width = 1.dp, shape = CircleShape, color = Color.LightGray)
                        .clickable { noteGroupTitle = "" }
                        .padding(4.dp),
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear"
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}

fun addGroup(noteGroupTitle:String,sharedViewModel: SharedViewModel,onNavigate: (Long) -> Unit){
    val groupTitle = noteGroupTitle.ifEmpty { "Note - ${DateTimeUtil.formatDateTimeTodayForTitle() }"}
    val groupUuid = Clock.System.now().toEpochMilliseconds()
    sharedViewModel.addNoteGroup(
        NoteGroup(
            title = groupTitle,
            uuid = groupUuid,
            dateCreated = DateTimeUtil.formatDateTimeToday(),
            dateUpdated = DateTimeUtil.formatDateTimeToday()
        )
    )
    onNavigate(groupUuid)
}