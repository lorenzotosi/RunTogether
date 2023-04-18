package com.app.runtogether

sealed class Screens(val name: String) {
    object Home : Screens("Run")
    object Settings : Screens("Settings")
    object Challenges : Screens("Challenges")
    object TodaysRun : Screens("Today's Run")
}