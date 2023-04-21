package com.app.runtogether

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowHomeScreen(navController : NavHostController = rememberNavController()){
    Scaffold(
        topBar = {GenerateTopBar(navController)}
    ) {pValues ->
        NavigationGraph(navController, pValues)
        CreateNavigationBar(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateTopBar(navController: NavHostController){
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route?: Screens.Home.name
    CenterAlignedTopAppBar(
        title = { Text(text = currentScreen) }
    )
}

@Composable
fun NavigationGraph(navController: NavHostController, padding: PaddingValues){
    NavHost(navController = navController,
        startDestination = Screens.Home.name,
        modifier = Modifier.padding(padding)) {
        composable(route = Screens.TodaysRun.name){
            //todo
        }
        composable(route = Screens.Home.name){
            //todo
        }
        composable(route = Screens.Settings.name){
            //todo
        }
        composable(route = Screens.Challenges.name){
            //todo
        }
    }
}

@Composable
fun CreateNavigationBar(navController: NavHostController){
    var selectedItem by remember { mutableStateOf(0) }
    val map = mapOf(Screens.Home.name to R.drawable.baseline_run_circle_24,
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
                            navController.navigate(v.key)}
            )
        }
    }
}