package com.example.composesubmission

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidintermedieatesubmission.data.response.Rumah
import com.example.composesubmission.ui.components.BottomBar
import com.example.composesubmission.ui.navigation.Screen
import com.example.composesubmission.ui.screen.detail.DetailScreen
import com.example.composesubmission.ui.screen.edit.EditScreen
import com.example.composesubmission.ui.screen.home.ListRumahScreen
import com.example.composesubmission.ui.screen.home.RumahListItem
import com.example.composesubmission.ui.screen.profile.ProfileScreen
import com.example.composesubmission.ui.theme.ComposeSubmissionTheme


@Composable
fun JetRumahApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Screen.Home.route,
                arguments = listOf(navArgument("isSuccess") { type = NavType.BoolType }),) {
                val isSuccess = it.arguments?.getBoolean("isSuccess")
                ListRumahScreen(navigateToDetail = { rumahId ->
                    navController.navigate(Screen.DetailRumah.createRoute(rumahId))
                }, navigateToTambah = {
                    navController.navigate(Screen.TambahRumah.route)
                }, navigateBackResult = isSuccess ?: false

                )
            }

            composable(Screen.TambahRumah.route) {
                EditScreen( navigateBack = { isSuccesful ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("isSuccesful", isSuccesful)
                    navController.popBackStack()
                }, navigateSuccess = {
                    navController.navigate(Screen.Home.isSuccess(true)) {
                        popUpTo(navController.graph.findStartDestination().id) {
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }

            composable(Screen.Profile.route) {
                ProfileScreen()
            }

            composable(
                Screen.EditRumah.route,
                arguments = listOf(navArgument("rumahId") { type = NavType.StringType }),
            ) {
                val id = it.arguments?.getString("rumahId")
                EditScreen(
                    rumahId = id,
                    navigateBack = {
                        navController.navigateUp()
                    }, navigateSuccess = {
                        navController.navigate(Screen.Home.isSuccess(true)) {
                            popUpTo(navController.graph.findStartDestination().id) {

                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(
                route = Screen.DetailRumah.route,
                arguments = listOf(navArgument("rumahId") { type = NavType.StringType }),
            ) {
                val id = it.arguments?.getString("rumahId") ?: ""
                DetailScreen(
                    rumahId = id,
                    navigateBack = {
                        navController.navigateUp()
                    },
                    navigateToEdit = { rumahId ->
                        navController.navigate(Screen.EditRumah.createRoute(rumahId))
                    }
                )
            }

        }

    }

}


@Preview(showBackground = true)
@Composable
fun JetHeroesAppPreview() {
    ComposeSubmissionTheme {
        RumahListItem(
            dataRumah = Rumah(
                id = "0",
                name = "Rumah Ideaman",
                photoUrl = "",
                description = "Rummah Terbaik sejagat raya dah bang asli ini bang no bohonh ",
                author = "Miya",
            )
        )
    }
}