package com.tutorials.firstkmp.presentation.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import co.touchlab.kermit.Logger
import com.tutorials.firstkmp.domain.NoteDataSource
import com.tutorials.firstkmp.presentation.NoteNavigationRoute
import com.tutorials.firstkmp.presentation.PreComposeNavHost
import com.tutorials.firstkmp.presentation.SharedViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
fun MainView(noteDataSource: NoteDataSource) {
    val sharedViewModel =
        getViewModel(key = Unit, viewModelFactory { SharedViewModel(noteDataSource) })

NewView(sharedViewModel)


}

@Composable
fun NewView(sharedViewModel: SharedViewModel) {
    val navigator = rememberNavigator()
    Scaffold (
        bottomBar = {
            BottomBarNavigation(navigator)
        }
    ){
        PreComposeNavHost(navigator,sharedViewModel)

    }
}

@Composable
fun BottomBarNavigation(navigator: Navigator) {
    val scope = rememberCoroutineScope()
    var selectedItemIndex by remember { mutableStateOf(0) }
    var currentRoute by remember { mutableStateOf("") }
    scope.launch {
        navigator.currentEntry.collect { backStackEntry ->
            backStackEntry?.let {
                currentRoute = it.route.route
                Logger.d("JOENOTETAG") { "route: ${currentRoute}:->Index $selectedItemIndex" }
            }

        }
    }

    val screens by remember {
        mutableStateOf(
            listOf(
                NoteNavigationRoute.NoteHomeScreen,
                NoteNavigationRoute.SharedHomeScreen

            )
        )
    }

    val topLevelDest by remember {
        mutableStateOf(
            listOf(
                NoteNavigationRoute.NoteHomeScreen.route,
                NoteNavigationRoute.SharedHomeScreen.route

            )
        )
    }

    if (topLevelDest.contains(currentRoute)) {

        NavigationBar {
            screens.forEachIndexed { index, it ->
                NavigationBarItem(
                    selected = selectedItemIndex == index,
                    onClick = {
                        selectedItemIndex = index
                        val navOptions = NavOptions(
                            launchSingleTop = true,
                            popUpTo = PopUpTo(
                                route = screens[index].route,
                                inclusive = true
                            )
                        )
                        navigator.navigate(
                            route = screens[index].route,
                            options = navOptions
                        )
                    },
                    icon = {
                        Icon(imageVector = getIcon(it.route), contentDescription = it.route)
                    },
                    label = {
                        Text(it.route)
                    }
                )
            }
        }
    }
}

fun getIcon(route:String):ImageVector{
  return  when(route){
        NoteNavigationRoute.NoteHomeScreen.route->{
            Icons.Default.Home
        }
        else-> Icons.Default.Search
    }
}