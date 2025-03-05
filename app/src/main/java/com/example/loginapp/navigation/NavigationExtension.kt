package com.example.loginapp.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

fun NavHostController.navigateSingleTopTo(
    route: String,
) {
    this.navigate(route) {
        launchSingleTop = true
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        restoreState = true
    }
}