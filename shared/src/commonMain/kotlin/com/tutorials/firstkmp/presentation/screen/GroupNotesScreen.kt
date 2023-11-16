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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.tutorials.firstkmp.domain.Note
import com.tutorials.firstkmp.domain.NoteGroup
import com.tutorials.firstkmp.presentation.SharedViewModel
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

data class GroupNotesScreen(private val groupUuid:Long,private val sharedViewModel: SharedViewModel):Screen{

     @Composable
     override fun Content() {
         val navigator = LocalNavigator.currentOrThrow
         NoteGroupItemScreen(sharedViewModel, onNavigateUp = {navigator.pop()}, groupUuid = groupUuid)

     }
 }
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun NoteGroupItemScreen(
    sharedViewModel:SharedViewModel,
    groupUuid: Long,
    onNavigateUp:()->Unit
) {
    val lazyListState = rememberLazyListState()

    var noteText by remember {
        mutableStateOf("")
    }

    var noteGroup by remember {
        mutableStateOf(NoteGroup())
    }

    val uiState by sharedViewModel.allNotesState.collectAsState()
    val uiGroupState by sharedViewModel.noteGroupState.collectAsState()

    LaunchedEffect(Unit){
        sharedViewModel.getNoteGroupByUuid(groupUuid)
        sharedViewModel.loadAllNotesByGroup(groupUuid)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = noteGroup.title, modifier = Modifier.padding(start = 10.dp)) },
            navigationIcon = {
                Icon(modifier = Modifier.clickable { onNavigateUp() },
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back"
                )
            },
            actions = {
                IconButton(onClick = { /*TODO: edit note group*/ }) {
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
            Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(color = Color.LightGray).padding(top = 10.dp))

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
                            onClick = {
                                // TODO: Navigate to view note screen
                            },
                            onLongClick = {
                                // TODO: delete note
                            })

                    }

                    uiGroupState.noteGroup != null ->{
                        noteGroup = uiGroupState.noteGroup!!
                    }

                }

            }


            Column {
                Box(modifier = Modifier.fillMaxWidth().height(0.8.dp).background(color = Color.LightGray))
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
                                noteText = it
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
                                }
                                .padding(end = 8.dp),
                            imageVector = Icons.Default.MoreVert, contentDescription = "Attachment"
                        )
                        FilledIconButton(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .align(Alignment.Bottom),
                            shape = CircleShape, onClick = {
                                /*TODO: save note*/
                                if (noteText.isNotEmpty()){
                                    val note = Note(
                                        id = Clock.System.now().toEpochMilliseconds(),
                                        title = noteText,
                                        groupUuid =groupUuid,
                                        groupId =noteGroup.id!!,
                                        dateCreated = Clock.System.now().toEpochMilliseconds()
                                    )
                                    sharedViewModel.addNote(note)
                                    noteText = ""
                                }

                            }) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = "Add note")
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun NoteItem(text:String) {
    Surface(
        modifier = Modifier.padding(
            top = 5.dp,
            bottom = 5.dp,
            end = 10.dp,
            start = 10.dp
        )
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = Color.Gray, shape = RoundedCornerShape(
                        CornerSize(8.dp)
                    )
                )
                .padding(8.dp)
        ) {
            Column {
                Text(text = text)
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
    LazyColumn(modifier = Modifier.fillMaxWidth(), state = state, horizontalAlignment = Alignment.End) {
        itemsIndexed(noteItems) { index, item ->
            NoteItem(item.title)
        }
    }
}
