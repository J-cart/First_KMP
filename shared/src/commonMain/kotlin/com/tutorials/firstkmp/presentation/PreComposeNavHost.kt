package com.tutorials.firstkmp.presentation

import androidx.compose.runtime.Composable
import com.tutorials.firstkmp.presentation.screen.AddEditNoteGroup
import com.tutorials.firstkmp.presentation.screen.NoteGroupItemScreen
import com.tutorials.firstkmp.presentation.screen.NoteHomeScreen
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun PreComposeNavHost(navigator: Navigator,sharedViewModel: SharedViewModel) {

    NavHost(
        navigator = navigator,
        navTransition = NavTransition(),
        initialRoute = NoteNavigationRoute.NoteHomeScreen.route
    ) {


        scene(
            NoteNavigationRoute.NoteHomeScreen.route
        ) {
            NoteHomeScreen(onViewGroupNavigate = {
                navigator.navigate(NoteNavigationRoute.NoteGroupItemScreen.navigateWithArgs(it))
            }, onAddGroupNavigate = {
                navigator.navigate(NoteNavigationRoute.AddEditNoteGroupScreen.route)
            }, sharedViewModel = sharedViewModel)
        }

        scene(
            NoteNavigationRoute.AddEditNoteGroupScreen.route
        ) { backStackEntry ->
            val noteGroupUuid = backStackEntry.path<Long>("group_uuid")
            AddEditNoteGroup(
                noteGroupUuid = noteGroupUuid,
                sharedViewModel = sharedViewModel,
                onNavigate = {
                    navigator.navigate(NoteNavigationRoute.NoteGroupItemScreen.navigateWithArgs(it))

                },
                onEditNavigate = {
                    navigator.popBackStack()
                },
                onDeleteNavigate = {
                    val navOptions = NavOptions(
                        popUpTo = PopUpTo(
                            route = NoteNavigationRoute.NoteHomeScreen.route,
                            inclusive = true
                        )
                    )
                    navigator.navigate(
                        route = NoteNavigationRoute.NoteHomeScreen.route,
                        options = navOptions
                    )
                },
                onNavigateUp = {
                    navigator.goBack()
                }
            )
        }

        scene(
            NoteNavigationRoute.NoteGroupItemScreen.route
        ) {backStackEntry->
            val noteGroupUuid = backStackEntry.path<Long>("group_uuid")?: 0L
            NoteGroupItemScreen(
                sharedViewModel = sharedViewModel,
                groupUuid = noteGroupUuid ,
                onNavigateUp = {
                    val navOptions = NavOptions(
                        popUpTo = PopUpTo(
                            route = NoteNavigationRoute.NoteHomeScreen.route,
                            inclusive = true
                        )
                    )
                    navigator.navigate(
                        route = NoteNavigationRoute.NoteHomeScreen.route,
                        options = navOptions
                    )
                },
                onEditNavigate = {
                    navigator.navigate(NoteNavigationRoute.AddEditNoteGroupScreen.navigateWithArgs(it.uuid))
                }
            )
        }
    }
}


sealed class NoteNavigationRoute(val route: String) {
    object NoteHomeScreen : NoteNavigationRoute("NoteHomeScreen")

    object AddEditNoteGroupScreen : NoteNavigationRoute("AddEditNoteGroup/{group_uuid}") {
        fun navigateWithArgs(uuid: Long): String {
            return this.route.replace("{group_uuid}", "$uuid")
        }
    }

    object NoteGroupItemScreen : NoteNavigationRoute("NoteGroupItemScreen/{group_uuid}") {
        fun navigateWithArgs(uuid: Long): String {
            return this.route.replace("{group_uuid}", "$uuid")
        }
    }
}

