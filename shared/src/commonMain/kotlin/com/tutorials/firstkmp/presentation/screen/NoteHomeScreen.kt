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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.tutorials.firstkmp.PlatformUtil
import com.tutorials.firstkmp.domain.NoteGroup
import com.tutorials.firstkmp.presentation.SharedViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NoteHomeScreen(
    onViewGroupNavigate: (Long) -> Unit,
    onAddGroupNavigate: () -> Unit,
    sharedViewModel: SharedViewModel,
    platformUtil: PlatformUtil
) {

    val imageUtil = platformUtil.createImagePicker()
    imageUtil.initUtil()
    val lazyListState = rememberLazyListState()


    var deleteDialogText by remember {
        mutableStateOf("")
    }

    var noteToDelete by remember {
        mutableStateOf(NoteGroup())
    }

    var dialogState by remember {
        mutableStateOf(false)
    }
    var singleDialogState by remember {
        mutableStateOf(false)
    }

    val uiState by sharedViewModel.allNoteGroupState.collectAsState()

    LaunchedEffect(key1 = true) {
        sharedViewModel.loadAllNoteGroup()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Notes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ))
        }, floatingActionButton = {
            GenericFab(
                contentDescription = "Add Group",
                action = {
                    // TODO: Navigate to add note screen
                         onAddGroupNavigate()
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

                    uiState.groupList.isEmpty() -> {

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

                    uiState.groupList.isNotEmpty() -> {
                        NoteGroupList(
                            noteGroupItems = uiState.groupList,
                            state = lazyListState,
                            onClick = {
                                // TODO: Navigate to view note screen
                                onViewGroupNavigate(it.uuid)
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
            ) { sharedViewModel.deleteAllNoteGroup() }

            DeleteDialog(
                updateDialogState = {
                    singleDialogState = it
                },
                dialogState = singleDialogState,
                text = deleteDialogText
            ) { noteToDelete.id?.let { sharedViewModel.deleteNoteGroup(id=it, imageUtil = imageUtil) } }

        }
    }


}



@Composable
fun NoteGroupList(
    noteGroupItems: List<NoteGroup>,
    state: LazyListState,
    onClick: (noteGroupItem: NoteGroup) -> Unit,
    onLongClick: (noteGroupItem: NoteGroup) -> Unit
) {
    LazyColumn(state = state) {
        itemsIndexed(noteGroupItems) { index, item ->
            NoteGroupItem(noteGroupItem = item, onLongClick = {
                onLongClick(item)
            }, onClick = {
                onClick(item)
            })
        }
    }
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



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteGroupItem(
    noteGroupItem: NoteGroup,
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
                            text = noteGroupItem.title,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = noteGroupItem.dateUpdated, color = Color.Gray, fontSize = 12.sp)
                    }
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
