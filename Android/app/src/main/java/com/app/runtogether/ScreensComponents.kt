package com.app.runtogether

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.material3.Icon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalNavigationDrawerSample( locationDetails: LocationDetails, navController : NavHostController = rememberNavController()) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route?: Screens.RunScreen.name

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // icons to mimic drawer destinations
    val items = listOf(
        MenuItems(id = "settings",
            title = "Settings",
            Icons.Default.Settings,
            contentDescription = "go to settings",
            Screens.Settings),

        //MenuItems(id = "profile", title = "Profile", Icons.Default.Person, contentDescription = "go to profile", Screens.Settings)
    )
    val selectedItem = remember { mutableStateOf(items[0]) }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Drawer title", modifier = Modifier.padding(16.dp))
                Spacer(Modifier.height(12.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, item.contentDescription) },
                        label = { Text(item.title) },
                        selected = item == selectedItem.value,
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                            selectedItem.value = item
                            /* --------------------------------------- */
                            navController.navigate(item.screens.name)
                            /* --------------------------------------- */
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text(text = currentScreen) },
                        navigationIcon = {
                            if (currentScreen !=  Screens.SignUp.name) {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Toggle drawer"
                                    )
                                }
                            }
                        }
                    )
                    if(currentScreen!=Screens.SignUp.name) {
                        CreateNavigationBar(navController)
                    } }
            ) {
                NavigationGraph(navController, it, locationDetails)
            }
        },
        gesturesEnabled = currentScreen != Screens.SignUp.name
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
            ShowRunScreen(locationDetails) { navController.navigate(Screens.SignUp.name) }
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
                            navController.popBackStack(navController.graph.id, inclusive = true)
                            navController.navigate(v.key)
                }
            )
        }
    }
}
