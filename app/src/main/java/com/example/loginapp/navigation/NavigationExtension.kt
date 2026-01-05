package com.example.loginapp.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

inline fun <reified T : Any> NavHostController.navigateSingleTopTo(
    route: T,
) {
    this.navigate(route) {
        launchSingleTop = true
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        restoreState = true
    }
}
