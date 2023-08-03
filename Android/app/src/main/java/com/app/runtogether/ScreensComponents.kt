package com.app.runtogether

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowHomeScreen( locationDetails: LocationDetails, navController : NavHostController = rememberNavController()){
    Scaffold(
        topBar = { TopAndNavigationBarHandler(navController) },
        bottomBar = {BottomAppBar()}

    ) {
        NavigationGraph(navController, it, locationDetails)
    }

}

@Composable
fun TopAndNavigationBarHandler(navController: NavHostController){
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route?: Screens.RunScreen.name
    GenerateTopBar(currentScreen)
    if(currentScreen!=Screens.SignUp.name) {
        CreateNavigationBar(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateTopBar(currentScreen : String){
    //missing hamburger and "go back" arrows
    CenterAlignedTopAppBar(
        title = { Text(text = currentScreen) }
    )
}

@Composable
fun NavigationGraph(navController: NavHostController, paddingValues: PaddingValues, locationDetails: LocationDetails){
    NavHost(navController = navController,
        startDestination = Screens.RunScreen.name,
        modifier = Modifier.padding(top = 155.dp)) {
        composable(route = Screens.TodaysRun.name){
            CardRun(index = 10, navController = navController )
        }
        composable(route = Screens.RunScreen.name){
            ShowRunScreen(locationDetails)
        }
        composable(route = Screens.Settings.name){
            //todo
        }
        composable(route = Screens.Challenges.name){
            //index andra sostituito con il numero di challenge nel database
            CreateGrid(index = 10, navController = navController)
        }
        composable(route = Screens.SignUp.name) {
            ShowSignUpPage(navController)
        }
    }
}

@Composable
fun CreateNavigationBar(navController: NavHostController){
    var selectedItem by remember { mutableStateOf(0) }
    val map = mapOf(Screens.RunScreen.name to R.drawable.baseline_run_circle_24,
        Screens.TodaysRun.name to R.drawable.baseline_today_24,
        Screens.Challenges.name to R.drawable.round_stars_24)

    NavigationBar (modifier = Modifier.padding(vertical = 64.dp)) {
        map.entries.forEachIndexed { k, v ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = v.value),
                        contentDescription = v.key
                    )
                },
                label = { Text(v.key) },
                selected = selectedItem == k,
                onClick = { selectedItem = k
                            navController.navigate(v.key)
                }
            )
        }
    }
}

@Composable
fun BottomAppBar(){
    var selectedItem by remember { mutableStateOf(0) }
    val map = mapOf(Screens.Running.name to Icon(painter= painterResource(id = R.drawable.baseline_run_circle_24), contentDescription = "Menu"),
        Screens.Friends.name to Icons.Filled.Person,
        Screens.Notify.name to Icons.Filled.Notifications)
    BottomAppBar(modifier = Modifier.fillMaxWidth().height(80.dp)){
        /*IconButton(onClick = { /* doSomething() */ }) {
            Icon(painter= painterResource(id = R.drawable.baseline_run_circle_24), contentDescription = "Localized description")

        }*/

            map.entries.forEachIndexed { k, v ->
                NavigationBarItem(
                    icon = {v.value},
                    label = { Text(v.key) },
                    selected = selectedItem == k,
                    onClick = { selectedItem = k
                    }
                )
            }
    }
}
