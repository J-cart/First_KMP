package com.tutorials.firstkmp.presentation.screen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tutorials.firstkmp.domain.Note
import com.tutorials.firstkmp.domain.NoteDataSource
import com.tutorials.firstkmp.presentation.SharedViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


data class HomeScreen(private val noteDataSource: NoteDataSource):Screen{


    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        NoteHomeScreen(noteDataSource, onNavigate = {
            navigator.push(AddEditNoteGroupScreen())
        })
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun NoteHomeScreen(noteDataSource: NoteDataSource,onNavigate:()->Unit) {

    val sharedViewModel =
        getViewModel(key = Unit, viewModelFactory { SharedViewModel(noteDataSource) })

    val lazyListState = rememberLazyListState()


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
                         onNavigate()
//                    addNoteDialogState = true
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

            Column(modifier = Modifier.weight(1f)) {

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
                        NoteGroupList(
                            noteItems = uiState.noteList,
                            state = lazyListState,
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
            }

            HomeBottomMenu(modifier = Modifier.height(60.dp))

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

            AddNoteGroupDialog(
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
fun NoteGroupList(
    noteItems: List<Note>,
    state: LazyListState,
    onClick: (noteItem: Note) -> Unit,
    onLongClick: (noteItem: Note) -> Unit
) {
    LazyColumn(state = state) {
        itemsIndexed(noteItems) { index, item ->
            NoteGroupItem(noteItem = item, onLongClick = {
                onLongClick(item)
            }, onClick = {
                onClick(item)
            })
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
fun AddNoteGroupDialog(
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteGroupItem(
    noteItem: Note,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val dotColor by remember {
        mutableStateOf(
            listOf(
                Color.Cyan,
                Color.Black,
                Color.Red,
                Color.Green,
                Color.Yellow
            )
        )
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .height(50.dp)
            .clip(RectangleShape)
            .combinedClickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = rememberRipple(bounded = false), onClick = {
                onClick()
            }, onLongClick = {
                onLongClick()

            })
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row() {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(color = dotColor.random(), shape = CircleShape)
                        .align(Alignment.CenterVertically)
                        .padding(start = 16.dp)
                )
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp),
                            text = noteItem.title,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "Nov 9, Thu", color = Color.Gray, fontSize = 12.sp)
                    }
                    if(noteItem.desc.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(top = 4.dp),
                            text = "Latest group note",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeBottomMenu(modifier: Modifier) {
    Surface {
        Box {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        // TODO: open menu
                    }) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "menu",
                            tint = Color.Gray
                        )
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = "Menu",
                            color = Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        // TODO: open search
                    }) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "search",
                            tint = Color.Gray
                        )
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = "Search",
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
