package com.example.composesubmission.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home/{isSuccess}") {
        fun isSuccess(isSuccess: Boolean) = "home/${isSuccess}"
    }
    object DetailRumah : Screen("detail/{rumahId}"){
        fun createRoute(rumahId: String) = "detail/$rumahId"
    }
    object EditRumah : Screen("edit/{rumahId}"){
        fun createRoute(rumahId: String) = "edit/$rumahId"
    }
    object TambahRumah : Screen("add")
    object Profile : Screen("profile")
}