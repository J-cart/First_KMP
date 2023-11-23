package com.tutorials.firstkmp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tutorials.firstkmp.PlatformUtil
import com.tutorials.firstkmp.core.DatabaseDriverFactory
import com.tutorials.firstkmp.data.SqlDelightNoteDataSourceImpl
import com.tutorials.firstkmp.database.NoteDatabase
import com.tutorials.firstkmp.presentation.screen.MainView
import com.tutorials.firstkmp.presentation.ui.theme.NoteTheme
import moe.tlaster.precompose.PreComposeApp

class MainActivity : ComponentActivity() {
    private val db = NoteDatabase(driver = DatabaseDriverFactory(this).createDriver())
    private val platformUtil = PlatformUtil(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PreComposeApp {
                NoteTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {

                        MainView(
                            platformUtil,
                            SqlDelightNoteDataSourceImpl(db)
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
