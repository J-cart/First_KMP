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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tutorials.firstkmp.PlatformUtil
import com.tutorials.firstkmp.domain.NoteGroup
import com.tutorials.firstkmp.domain.time.DateTimeUtil
import com.tutorials.firstkmp.presentation.SharedViewModel
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteGroup(
    noteGroupUuid: Long? = null,
    sharedViewModel: SharedViewModel,
    onNavigateUp: () -> Unit,
    onNavigate: (Long) -> Unit,
    onEditNavigate: () -> Unit,
    onDeleteNavigate: () -> Unit,
    platformUtil: PlatformUtil
) {
    var noteGroupTitle by remember {
        mutableStateOf("")
    }

    var appBarTitle by remember { mutableStateOf("New note group") }

    var noteGroup by remember { mutableStateOf(NoteGroup()) }

    val noteGroupUiState by sharedViewModel.noteGroupState.collectAsState()

    LaunchedEffect(Unit) {
        noteGroupUuid?.let {
            sharedViewModel.getNoteGroupByUuid(it)
        }
    }

    val imageUtil = platformUtil.createImagePicker()
    imageUtil.initUtil()

    noteGroupUuid?.let {
        noteGroupUiState.noteGroup?.let { nGroup ->
            noteGroup = nGroup
            noteGroupTitle = nGroup.title
            appBarTitle = "Edit note group"
        }
    }


    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = appBarTitle, modifier = Modifier.padding(start = 10.dp)) },
            navigationIcon = {
                Icon(
                    modifier = Modifier.clickable { onNavigateUp() },
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back"
                )
            },
            actions = {

                noteGroup.id?.let {
                    IconButton(onClick = {
                        /*TODO: delete note group*/
                        sharedViewModel.deleteNoteGroup(id=it,imageUtil = imageUtil)
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
                noteGroupUuid?.let {
                    sharedViewModel.updateNoteGroup(
                        noteGroup.copy(
                            title = noteGroupTitle,
                            dateUpdated = DateTimeUtil.formatDateTimeToday()
                        )
                    )
                    onEditNavigate()
                } ?: addGroup(noteGroupTitle, sharedViewModel) {
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
                        Text(
                            text = "Note - ${DateTimeUtil.formatDateTimeTodayForTitle()}",
                            color = Color.LightGray
                        )
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

fun addGroup(noteGroupTitle: String, sharedViewModel: SharedViewModel, onNavigate: (Long) -> Unit) {
    val groupTitle =
        noteGroupTitle.ifEmpty { "Note - ${DateTimeUtil.formatDateTimeTodayForTitle()}" }
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