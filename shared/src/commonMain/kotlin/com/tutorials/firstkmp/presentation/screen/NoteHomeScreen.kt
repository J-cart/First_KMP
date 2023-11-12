package com.tutorials.firstkmp.presentation.screen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tutorials.firstkmp.domain.Note
import com.tutorials.firstkmp.domain.NoteDataSource
import com.tutorials.firstkmp.presentation.SharedViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalResourceApi::class)
@Composable
fun NoteHomeScreen(noteDataSource: NoteDataSource) {

    val sharedViewModel =
        getViewModel(key = Unit, viewModelFactory { SharedViewModel(noteDataSource) })

    val lazyListState = rememberLazyListState()
    var query by remember {
        mutableStateOf("")
    }

    var deleteDialogText by remember {
        mutableStateOf("")
    }

    var noteToDelete by remember {
        mutableStateOf(Note())
    }

    var dialogState by remember {
        mutableStateOf(false)
    }
    var singleDialogState by remember {
        mutableStateOf(false)
    }
    var addNoteDialogState by remember {
        mutableStateOf(false)
    }

    val uiState by sharedViewModel.allNotesState.collectAsState()

    LaunchedEffect(key1 = true) {
        sharedViewModel.loadAllNotes()
    }

    Scaffold(
        topBar = {
            GenericAppBar(
                title = "Notes",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete All"
                    )
                },
                onIconClick = {
                    dialogState = uiState.noteList.isNotEmpty()
                    deleteDialogText = "Are you sure you want to delete all items?"
                },
                iconState = remember {
                    mutableStateOf(true)
                }
            )
        }, floatingActionButton = {
            GenericFab(
                contentDescription = "Add Note",
                action = {
                    // TODO: Navigate to add note screen
                    addNoteDialogState = true
                },
                icon = Icons.Default.Add
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            CustomSearchBar(onValueChange = {
                query = it
            }, query = query)

            when {

                uiState.noteList.isEmpty() -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(0.55f),
                            painter = painterResource("compose-multiplatform.xml"),
                            contentDescription = "Empty"
                        )

                    }


                }

                uiState.noteList.isNotEmpty() -> {
                    NoteList(
                        noteItems = uiState.noteList,
                        state = lazyListState,
                        query = query,
                        onClick = {
                            // TODO: Navigate to view note screen
                        },
                        onLongClick = {
                            singleDialogState = true
                            deleteDialogText = "Are you sure you want to delete this item?"
                            noteToDelete = it
                        })

                }

            }

            DeleteDialog(
                updateDialogState = {
                    dialogState = it
                },
                dialogState = dialogState,
                text = deleteDialogText
            ) { sharedViewModel.deleteAllNotes() }

            DeleteDialog(
                updateDialogState = {
                    singleDialogState = it
                },
                dialogState = singleDialogState,
                text = deleteDialogText
            ) { sharedViewModel.deleteNote(noteToDelete.id) }

            AddNoteDialog(
                dialogState = addNoteDialogState,
                showDialog = {
                    addNoteDialogState = it
                },
                addNoteAction = {
                    sharedViewModel.addNote(it)
                }
            )
        }
    }


}

@Composable
fun CustomSearchBar(onValueChange: (String) -> Unit, query: String) {

    val icon by remember(query) {
        mutableStateOf(
            if (query.isEmpty()) {
                Icons.Default.Search
            } else {
                Icons.Default.Clear
            }
        )
    }
    TextField(
        value = query,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(text = "Search ...") },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black
        ), trailingIcon = {
            IconButton(onClick = {
                if (query.isNotEmpty()) {
                    onValueChange("")
                } else {
                    // TODO: search item(apparently, since `query` is a state,
                    //  there might be no need for this `else` block)
                }
            }) {
                Icon(imageVector = icon, contentDescription = "action")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 5.dp, bottom = 12.dp)
    )
}


@Composable
fun NoteList(
    noteItems: List<Note>,
    state: LazyListState,
    query: String,
    onClick: (noteItem: Note) -> Unit,
    onLongClick: (noteItem: Note) -> Unit
) {
    val items = if (query.isEmpty()) {
        noteItems
    } else {
        noteItems.filter {
            it.desc.contains(query, ignoreCase = true) || it.title.contains(
                query,
                ignoreCase = true
            )
        }
    }
    LazyColumn(state = state) {
        itemsIndexed(items) { index, item ->
            NoteListItem(noteItem = item, onLongClick = {
                onLongClick(item)
            }, onClick = {
                onClick(item)
            })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(
    noteItem: Note,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 3.dp)
            .height(120.dp)
            .clip(RoundedCornerShape(18.dp))
            .combinedClickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = rememberRipple(bounded = false), onClick = {
                onClick()
            }, onLongClick = {
                onLongClick()

            }), color = MaterialTheme.colorScheme.primary
    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp)
            ) {
                if (noteItem.title.isNotEmpty()) {
                    Text(
                        text = noteItem.title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (noteItem.desc.isNotEmpty()) {
                    Text(text = noteItem.desc, maxLines = 1, overflow = TextOverflow.Ellipsis)

                }

                Text(text = noteItem.dateCreated.toString(), fontStyle = FontStyle.Italic)
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericAppBar(
    title: String,
    icon: @Composable (() -> Unit)?,
    onIconClick: (() -> Unit)?,
    iconState: MutableState<Boolean>
) {
    TopAppBar(
        title = { Text(text = title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        actions = {
            IconButton(onClick = { onIconClick?.invoke() }, content = {
                if (iconState.value) {
                    icon?.invoke()
                }
            })
        })
}


@Composable
fun GenericFab(
    contentDescription: String,
    action: () -> Unit,
    icon: ImageVector
) {

    return FloatingActionButton(shape = CircleShape,onClick = { action() }) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }

}


@Composable
fun DeleteDialog(
    updateDialogState: (Boolean) -> Unit,
    dialogState: Boolean,
    text: String,
    action: () -> Unit,
) {
    if (dialogState) {
        AlertDialog(
            onDismissRequest = { updateDialogState(false) },
            title = { Text(text = "Delete Note") }, text = {
                Column {
                    Text(text = text)
                }
            }, confirmButton = {
                ElevatedButton(modifier = Modifier.padding(horizontal = 3.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.surface
                    ),
                    onClick = {
                        action()
                        updateDialogState(
                            false
                        )
                    }) {
                    Text(text = "OK")

                }
            }, dismissButton = {
                ElevatedButton(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.surface
                    ),
                    onClick = {
                        updateDialogState(false)
                    }) {
                    Text(text = "CANCEL")

                }
            })

    }


}

@Composable
fun AddNoteDialog(
    dialogState: Boolean,
    showDialog: (Boolean) -> Unit,
    addNoteAction: (Note) -> Unit
) {

    var noteTitle by remember {
        mutableStateOf("")
    }
    var noteDesc by remember {
        mutableStateOf("")
    }
    if (dialogState) {
        Dialog(onDismissRequest = { showDialog(false) }) {
            Box(
                modifier = Modifier.background(
                    color = Color.White, shape = RoundedCornerShape(
                        CornerSize(16.dp)
                    )
                )
            ) {
                Column(modifier = Modifier.wrapContentSize().padding(12.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Add New Note", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }



                    OutlinedTextField(
                        value = noteTitle,
                        onValueChange = { noteTitle = it },
                        placeholder = { Text(text = "Enter title") },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        isError = noteTitle.isEmpty(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, top = 5.dp, bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = noteDesc,
                        onValueChange = { noteDesc = it },
                        placeholder = { Text(text = "Enter description") },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, top = 5.dp, bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp), horizontalArrangement = Arrangement.End
                    ) {
                        ElevatedButton(
                            modifier = Modifier.padding(horizontal = 3.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            onClick = {
                                showDialog(false)
                            }) {
                            Text(text = "CANCEL")

                        }

                        ElevatedButton(modifier = Modifier.padding(horizontal = 3.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            onClick = {
                                if (noteTitle.isNotEmpty()) {
                                    addNoteAction(
                                        Note(
                                            id = Clock.System.now().toEpochMilliseconds(),
                                            title = noteTitle,
                                            desc = noteDesc.ifEmpty { "" },
                                            dateCreated = Clock.System.now().toEpochMilliseconds()
                                        )
                                    )
                                    noteTitle = ""
                                    noteDesc = ""
                                    showDialog(false)
                                }
                            }) {
                            Text(text = "ADD")

                        }
                    }


                }
            }

        }
    }

}