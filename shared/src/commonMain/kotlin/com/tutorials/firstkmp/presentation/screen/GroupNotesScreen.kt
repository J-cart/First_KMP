package com.tutorials.firstkmp.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tutorials.firstkmp.PlatformUtil
import com.tutorials.firstkmp.domain.Note
import com.tutorials.firstkmp.domain.NoteGroup
import com.tutorials.firstkmp.domain.NoteType
import com.tutorials.firstkmp.presentation.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun NoteGroupItemScreen(
    platformUtil: PlatformUtil,
    sharedViewModel: SharedViewModel,
    groupUuid: Long,
    onNavigateUp: () -> Unit,
    onEditNavigate: (NoteGroup) -> Unit
) {

    val copyNoteScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    var noteGroup by remember {
        mutableStateOf(NoteGroup())
    }

    var selectedNote by remember {
        mutableStateOf<Set<Long>>(emptySet())
    }

    var noteText by remember {
        mutableStateOf("")
    }

    var editableNote by remember { mutableStateOf<Note?>(null) }
    var isEditMode by remember { mutableStateOf(false) }

    val uiState by sharedViewModel.allNotesState.collectAsState()
    val uiGroupState by sharedViewModel.noteGroupState.collectAsState()

    LaunchedEffect(Unit) {
        sharedViewModel.getNoteGroupByUuid(groupUuid)
        sharedViewModel.loadAllNotesByGroup(groupUuid)
    }

    LaunchedEffect(selectedNote) {
        if (selectedNote.isNotEmpty()) {
            editableNote = uiState.noteList.find { it.id == selectedNote.first() }
        }
    }
    DisposableEffect(copyNoteScope) {
        onDispose {
            copyNoteScope.cancel("On Dispose Screen")
        }
    }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val imagePicker = platformUtil.createImagePicker()
    imagePicker.registerPicker {
        imageBitmap = imagePicker.rememberImageBitmapFromByteArray(it)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = noteGroup.title, modifier = Modifier.padding(start = 10.dp)) },
            navigationIcon = {
                Icon(
                    modifier = Modifier.clickable { onNavigateUp() },
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back"
                )
            },
            actions = {
                IconButton(onClick = {
                    /*TODO: edit note group*/
                    onEditNavigate(noteGroup)

                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        )
    }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(0.8.dp)
                    .background(color = Color.LightGray).padding(top = 10.dp)
            )

            Box(modifier = Modifier.weight(1f)) {

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
                        NoteItemList(
                            noteItems = uiState.noteList,
                            state = lazyListState,
                            onClick = { note ->
                                // TODO: toggle selection
                                toggleSelection(
                                    sharedViewModel = sharedViewModel,
                                    selectedNote,
                                    noteItem = note,
                                    onToggle = {
                                        selectedNote = it
                                    }
                                )
                            },
                            onLongClick = {
                                // TODO: toggle selection
                            })

                    }

                }
                uiGroupState.noteGroup?.let {
                    noteGroup = it
                }

            }


            Column {
                Box(
                    modifier = Modifier.fillMaxWidth().height(0.8.dp)
                        .background(color = Color.LightGray)
                )
                if (selectedNote.isEmpty()) {
                    AddNoteView(
                        noteText = noteText,
                        sharedViewModel = sharedViewModel,
                        groupUuid = groupUuid,
                        noteGroup = noteGroup,
                        onValueChange = {
                            noteText = it
                        }, onEditNote = {
                            editableNote?.copy(text = noteText, isSelected = 0L)?.let { editNote ->
                                sharedViewModel.addNote(editNote)
                                isEditMode = it
                            }
                        },
                        onSelectAttachment = {
                            imagePicker.pickImage()
                        },
                        isEditMode = isEditMode
                    )
                } else {
                    editableNote?.let {
                        NoteEditVew(
                            selectedNote.size,
                            firstNote = it,
                            onClearSelection = {
                                sharedViewModel.clearSelection()
                                selectedNote = emptySet()
                            },
                            onEditSelection = {
                                selectedNote = emptySet()
                                editableNote?.let { note ->
                                    isEditMode = true
                                    noteText = note.text
                                }
                            },
                            onShareSelection = {
                                shareSelection(
                                    shareNoteScope = copyNoteScope,
                                    sharedViewModel = sharedViewModel,
                                    selectedNote = selectedNote,
                                    platformUtil = platformUtil,
                                    onShareComplete = {
                                        sharedViewModel.clearSelection()
                                        selectedNote = emptySet()
                                    }
                                )
                            },
                            onCopySelection = {
                                copySelection(
                                    copyNoteScope = copyNoteScope,
                                    sharedViewModel = sharedViewModel,
                                    selectedNote = selectedNote,
                                    platformUtil = platformUtil,
                                    onCopyComplete = {
                                        sharedViewModel.clearSelection()
                                        selectedNote = emptySet()
                                    }
                                )
                            },
                            onDeleteSelection = {
                                sharedViewModel.deleteNotesInIdList(selectedNote.toList())
                                selectedNote = emptySet()
                            }
                        )
                    }
                }
            }
            imageBitmap?.let {
                ShowImageDialog(
                    imageBitmap = it,
                    onCloseAction = {
                        imageBitmap = null
                    }
                )
            }

        }
    }
}

@Composable
fun NoteItem(note: Note, onClick: (noteItem: Note) -> Unit) {
    Surface {
        Box(
            modifier = Modifier.fillMaxWidth().background(
                color = toggleBackGroundColor(note)
            ).padding(
                top = (2.5).dp,
                bottom = (2.5).dp,
                end = 10.dp,
                start = 10.dp
            ), contentAlignment = Alignment.CenterEnd

        ) {
            Column(modifier = Modifier
                .wrapContentSize()
                .background(
                    color = Color.Gray, shape = RoundedCornerShape(
                        CornerSize(8.dp)
                    )
                ).clickable {
                    onClick(note)
                }
                .padding(8.dp)) {
                Text(text = note.text)
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = "7:34pm",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun NoteItemList(
    noteItems: List<Note>,
    state: LazyListState,
    onClick: (noteItem: Note) -> Unit,
    onLongClick: (noteItem: Note) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        horizontalAlignment = Alignment.End
    ) {
        itemsIndexed(noteItems) { index, item ->
            NoteItem(item, onClick = {
                onClick(it)

            })
        }
    }
}

@Composable
fun AddNoteView(
    isEditMode: Boolean,
    noteText: String,
    onValueChange: (String) -> Unit,
    onEditNote: (Boolean) -> Unit,
    sharedViewModel: SharedViewModel,
    groupUuid: Long,
    onSelectAttachment:()->Unit,
    noteGroup: NoteGroup
) {


    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(end = 8.dp),
                value = noteText,
                placeholder = {
                    Text(text = "What's on your mind?", color = Color.LightGray)
                },
                onValueChange = {
                    onValueChange(it)
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                )
            )
            Icon(
                modifier = Modifier
                    .clickable {
                        // TODO: select attachment
                        onSelectAttachment()
                    }
                    .padding(end = 8.dp),
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Attachment"
            )
            FilledIconButton(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.Bottom),
                shape = CircleShape, onClick = {
                    /*TODO: save note*/
                    if (noteText.trim().isNotEmpty()) {
                        if (isEditMode) {
                            onEditNote(false)
                            onValueChange("")

                            return@FilledIconButton
                        }
                        val note = Note(
                            id = Clock.System.now().toEpochMilliseconds(),
                            text = noteText.trim(),
                            groupUuid = groupUuid,
                            groupId = noteGroup.id!!,
                            dateCreated = Clock.System.now().toEpochMilliseconds()
                        )
                        sharedViewModel.addNote(note)
                        onValueChange("")
                    }

                }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Add note"
                )
            }

        }
    }
}

@Composable
fun NoteEditVew(
    selectionCount: Int,
    firstNote: Note,
    onClearSelection: () -> Unit,
    onEditSelection: () -> Unit,
    onShareSelection: () -> Unit,
    onCopySelection: () -> Unit,
    onDeleteSelection: () -> Unit,
) {

    Surface(modifier = Modifier.fillMaxWidth()) {
        Box {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onClearSelection() }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Close")
                }

                Text(text = "$selectionCount")
            }

            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectionCount == 1 && NoteType.canBeCopied(firstNote.noteType)) {
                    IconButton(onClick = { onEditSelection() }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                }

                IconButton(onClick = { onShareSelection() }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                }

                IconButton(onClick = { onCopySelection() }) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = "Copy")
                }

                IconButton(onClick = { onDeleteSelection() }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }


        }
    }
}

private fun toggleSelection(
    sharedViewModel: SharedViewModel,
    selectedNote: Set<Long>,
    noteItem: Note,
    onToggle: (Set<Long>) -> Unit
) {
    val isSelected = if (noteItem.isSelected == 0L) 1L else 0L
    val note = noteItem.copy(isSelected = isSelected)
    val mList = selectedNote.toMutableList()
    if (note.isSelected != 0L) {
        mList.add(note.id)
    } else {
        val noteId = mList.find { id -> id == note.id }
        mList.remove(noteId)
    }
    sharedViewModel.addNote(note)
    onToggle(mList.toSet())
}

private fun toggleBackGroundColor(noteItem: Note) =
    if (noteItem.isSelected != 0L) Color.Green else Color.Transparent

private fun copySelection(
    copyNoteScope: CoroutineScope,
    sharedViewModel: SharedViewModel,
    selectedNote: Set<Long>,
    platformUtil: PlatformUtil,
    onCopyComplete: () -> Unit
) {
    copyNoteScope.launch {
        val selectedNoteList =
            sharedViewModel.getNotesById(selectedNote.toList())

        if (selectedNoteList.size == 1) {
            platformUtil.copyToClipboard(
                selectedNoteList.first().text,
                "Text copied"
            )
            onCopyComplete()
        }
        if (selectedNoteList.size > 1) {
            val stringBuilder = StringBuilder()
            selectedNoteList.forEach { note ->
                stringBuilder.append("${note.text}\n")
            }
            platformUtil.copyToClipboard(
                stringBuilder.toString(),
                "Texts copied"
            )
            onCopyComplete()
        }
    }

}

private fun shareSelection(
    shareNoteScope: CoroutineScope,
    sharedViewModel: SharedViewModel,
    selectedNote: Set<Long>,
    platformUtil: PlatformUtil,
    onShareComplete: () -> Unit
) {
    shareNoteScope.launch {
        val selectedNoteList =
            sharedViewModel.getNotesById(selectedNote.toList())

        if (selectedNoteList.size == 1) {
            platformUtil.shareText(
                selectedNoteList.first().text,
                "Share text"
            )
            onShareComplete()
        }
        if (selectedNoteList.size > 1) {
            val stringBuilder = StringBuilder()
            selectedNoteList.forEach { note ->
                stringBuilder.append("${note.text}\n")
            }
            platformUtil.shareText(
                stringBuilder.toString(),
                "Share text"
            )
            onShareComplete()
        }
    }

}


@Composable
fun ShowImageDialog(
    imageBitmap: ImageBitmap,
    onCloseAction: () -> Unit
) {

    Dialog(onDismissRequest = { }) {
        Box(
            modifier = Modifier.size(300.dp).background(
                color = Color.White, shape = RoundedCornerShape(
                    CornerSize(16.dp)
                )
            ),
            contentAlignment = Alignment.Center
        ) {

            Image(modifier = Modifier.padding(8.dp), bitmap = imageBitmap, contentScale = ContentScale.FillBounds, contentDescription = "Image")

            IconButton(
                modifier = Modifier.background(color = Color.White, shape = CircleShape)
                    .align(Alignment.TopEnd).padding(8.dp), onClick = {
                    onCloseAction()
                }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")

            }

        }

    }


}