package com.tutorials.firstkmp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tutorials.firstkmp.core.DatabaseDriverFactory
import com.tutorials.firstkmp.data.SqlDelightNoteDataSourceImpl
import com.tutorials.firstkmp.database.NoteDatabase
import com.tutorials.firstkmp.presentation.screen.MainView

class MainActivity : ComponentActivity() {
    private val db = NoteDatabase(driver = DatabaseDriverFactory(this).createDriver())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView(
                SqlDelightNoteDataSourceImpl(db)
            )
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
