package com.app.runtogether

import ThemeSettingsScreen
import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalNavigationDrawerSample( locationDetails: LocationDetails, mygps: Boolean, navController : NavHostController = rememberNavController()) {

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
        MenuItems(id = "Profile",
            title = "Profile",
            Icons.Default.Person,
            contentDescription = "go to profile",
            Screens.Profile),

        MenuItems(id = "notify",
            title = "Notify",
            Icons.Default.Notifications,
            contentDescription = "go to notifications",
            Screens.Notify),

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
                        actions= {GoHome(navController)},
                        title = { Text(text = currentScreen) },
                        navigationIcon = {
                            if (currentScreen !=  Screens.Running.name) {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Toggle drawer"
                                    )
                                }
                            }
                        }
                    )

                    if(currentScreen==Screens.RunScreen.name ||
                        currentScreen==Screens.Challenges.name ||
                        currentScreen==Screens.TodaysRun.name) {
                        if (currentScreen == Screens.RunScreen.name)
                            CreateNavigationBar(navController, 0)

                        if (currentScreen == Screens.Challenges.name)
                            CreateNavigationBar(navController, 2)

                        if (currentScreen == Screens.TodaysRun.name)
                            CreateNavigationBar(navController, 1)
                    }
                }
            ) {
                NavigationGraph(navController, it, locationDetails, mygps)
            }
        },
        gesturesEnabled = currentScreen != Screens.Running.name
    )
}

@Composable
fun GoHome(navController : NavHostController) {
    IconButton(onClick = { navController.navigate(Screens.RunScreen.name) }) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_home_24),
            contentDescription = "Home",
        )
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, paddingValues: PaddingValues, locationDetails: LocationDetails, b : Boolean){
    // Define a mutable state variable for the username
    var currentUsername by remember { mutableStateOf("InitialUsername") }
    val onUsernameChanged: (String) -> Unit = { newUsername ->
        currentUsername = newUsername
    }
    NavHost(navController = navController,
        startDestination = Screens.RunScreen.name,
        modifier = Modifier) {
        composable(route = Screens.TodaysRun.name){
            CardRun(index = 10, navController = navController )
        }
        composable(route = Screens.RunScreen.name){
            ShowRunScreen(locationDetails, 155, false, b) { navController.navigate(Screens.Running.name) }
        }
        composable(route = Screens.Settings.name){
            ThemeSettingsScreen(
                currentUsername = currentUsername,
                onSaveClicked = { navController.popBackStack() },
                onThemeChanged =  { mode ->
                    if(mode){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }else{
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                },
                onDismiss = { navController.popBackStack() },
                isDarkTheme = false,
                onUsernameChanged = onUsernameChanged
            )
        }
        composable(route = Screens.Challenges.name){
            //index andra sostituito con il numero di challenge nel database
            CreateGrid(index = 10, navController = navController)
        }
        composable(route = Screens.SignUp.name) {
            ShowSignUpPage(navController)
        }
        composable(route = Screens.Running.name){
            ShowRunScreen(locationDetails, 0, true, b) { navController.navigate(Screens.SignUp.name) }
        }
        composable(route = Screens.Profile.name){
            ShowProfilePage()
        }


    }
}


@Composable
fun CreateNavigationBar(navController: NavHostController, i: Int){
    var selectedItem by remember { mutableStateOf(i) }
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