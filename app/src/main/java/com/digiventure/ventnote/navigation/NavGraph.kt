package com.digiventure.ventnote.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.digiventure.ventnote.screen.note_creation.NoteCreationPage
import com.digiventure.ventnote.screen.notes.NotesPage

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Route.NotesPage.routeName
    ) {
        composable(Route.NotesPage.routeName) {
            NotesPage(navHostController = navHostController)
        }
        composable(Route.NoteCreationPage.routeName) {
            NoteCreationPage(navHostController = navHostController)
        }
//        composable(
//            route = "${Route.NoteDetailPage.routeName}/{noteId}",
//            arguments = listOf(navArgument("noteId") {
//                type = NavType.StringType
//                defaultValue = ""
//            })
//        ) {
//            NoteDetailPage(navHostController = navHostController,
//                id = it.arguments?.getString("noteId") ?: "0")
//        }

//        composable(
//            route = "${Route.SharePreviewPage.routeName}/{noteData}",
//            arguments = listOf(navArgument("noteData") {
//                type = NoteModelParamType()
//            })
//        ) {
//            val note = it.arguments?.getParcelable<NoteModel>("noteData")
//            SharePreviewPage(navHostController = navHostController, note = note)
//        }
    }
}