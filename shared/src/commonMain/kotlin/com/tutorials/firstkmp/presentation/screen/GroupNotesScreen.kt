package com.tutorials.firstkmp.presentation.screen

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class GroupNotesScreen():Screen{

     @Composable
     override fun Content() {
         val navigator = LocalNavigator.currentOrThrow
         NoteGroupItemScreen(groupTitle = "Some Note", onNavigateUp = {navigator.pop()})

     }
 }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteGroupItemScreen(
    groupTitle: String,
    onNavigateUp:()->Unit
) {
    var noteText by remember {
        mutableStateOf("")
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = groupTitle, modifier = Modifier.padding(start = 10.dp)) },
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
                LazyColumn(
                    horizontalAlignment = Alignment.End
                ) {
                    item {
                        NoteItem(text = "Some text")
                    }
                    item {
                        NoteItem(text = "SSha as far say e dey work and I no dey reason time-space complexity then whatever works is fine by me")
                    }
                    item {
                        NoteItem(text = "SSha as far say e dey work and I no dey reason time-space ct")
                    }
                    item {
                        NoteItem(text = "Sha as far say e dey work and I no dey reason time-space complexity then whatever works is fine by meSha as far say e dey work and I no dey reason time-space complexity then whatever works is fine by me")
                    }
                    item {
                        NoteItem(text = "Sha as far say e dey work and I ")
                    }
                    item {
                        NoteItem(text = "Some text")
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
