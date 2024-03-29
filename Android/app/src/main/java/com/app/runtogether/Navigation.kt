package com.app.runtogether

sealed class Screens(val name: String) {
    object RunScreen : Screens("Start a Run")
    object Settings : Screens("Settings")
    object Challenges : Screens("Challenges")
    object TodaysRun : Screens("Today's Run")
    object SignUp : Screens("Sign Up")
    object Login : Screens("Login")
    object Profile : Screens("Profile")
    object Notify : Screens("Notify")
    object Running : Screens("Running")
    object AddNewRun : Screens("New Run")
    object EndRun : Screens("Results")
    object RunInfo : Screens("Run Information")

    object TrophyInfo : Screens("Trophy Information")

}