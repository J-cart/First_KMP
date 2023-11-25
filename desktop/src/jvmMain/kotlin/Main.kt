import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.tutorials.firstkmp.PlatformUtil
import com.tutorials.firstkmp.core.DatabaseDriverFactory
import com.tutorials.firstkmp.data.SqlDelightNoteDataSourceImpl
import com.tutorials.firstkmp.database.NoteDatabase
import com.tutorials.firstkmp.presentation.screen.MainView
import moe.tlaster.precompose.PreComposeApp


fun main() {
    val db = NoteDatabase(driver = DatabaseDriverFactory().createDriver())
    val platformUtil = PlatformUtil()

    application {

        val windowState = rememberWindowState()
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "First KMP"
        ) {

            PreComposeApp {
                MaterialTheme {
                    MainView(
                        platformUtil,
                        SqlDelightNoteDataSourceImpl(db)
                    )
                }
            }

        }
    }
}
