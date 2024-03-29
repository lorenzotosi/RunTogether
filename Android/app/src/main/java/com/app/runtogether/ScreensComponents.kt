package com.app.runtogether

import SessionManager
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
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

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.user.UserViewModel
import com.app.runtogether.swm.SettingsViewModel
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalNavigationDrawerSample(locationDetails: LocationDetails, mygps: Boolean, theme: String, svm : SettingsViewModel, navController: NavHostController = rememberNavController()) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route?: Screens.RunScreen.name
    val db = MyDatabase.getInstance(navController.context)
    val userId = SessionManager.getUserDetails(navController.context)
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    //if (SessionManager.isLoggedIn(navController.context))
    val username = db.userDao().getUsernameFromId(userId)
        .collectAsState(initial = String).value

    val scope = rememberCoroutineScope()
    //Log.d("uri", uriSelected.toString())
    val profilePicturePath = db.userDao().getPathFromId(userId)
        .collectAsState(initial = "").value
    /*val imagePainter = if (profilePicturePath != null) {
        // You can set image loading options here if needed
        rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = profilePicturePath).apply(block = fun ImageRequest.Builder.() {
                // You can set image loading options here if needed
                crossfade(true)
                diskCachePolicy(CachePolicy.DISABLED)
                memoryCachePolicy(CachePolicy.DISABLED)
            }).build()
        )
    } else {
        painterResource(id = R.drawable.image_profile)
    }*/

    val updatedProfilePicturePath = rememberUpdatedState(profilePicturePath)

    val imagePainter = if (updatedProfilePicturePath.value!=null && updatedProfilePicturePath.value.isNotBlank()) {
        // Load the updated profile picture with the timestamp in the path
        rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = updatedProfilePicturePath.value).apply {
                crossfade(true)
                diskCachePolicy(CachePolicy.DISABLED)
                memoryCachePolicy(CachePolicy.DISABLED)
            }.build()
        )
    } else {
        painterResource(id = R.drawable.image_profile)
    }

    // icons to mimic drawer destinations
    val items = listOf(
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
        MenuItems(id = "settings",
            title = "Settings",
            Icons.Default.Settings,
            contentDescription = "go to settings",
            Screens.Settings),
    )
    val selectedItem = remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Image(painter = imagePainter,
                    contentDescription = "profile image",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(150.dp, 150.dp))
                Text(if (SessionManager.isLoggedIn(navController.context)) "Hello $username!" else "User not logged in",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
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
                            if (!SessionManager.isLoggedIn(navController.context)){
                                if (item.screens.name == Screens.Profile.name){
                                    navController.navigate(Screens.Login.name)
                                }else {
                                    navController.navigate(item.screens.name)
                                }
                            } else {
                                navController.navigate(item.screens.name)
                            }
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
                            if (currentScreen !=  Screens.Running.name && currentScreen !=  Screens.EndRun.name && currentScreen != Screens.AddNewRun.name) {
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
                NavigationGraph(navController, it, locationDetails, mygps, theme, svm)
            }
        },
        gesturesEnabled = currentScreen != Screens.Running.name && currentScreen != Screens.EndRun.name && currentScreen != Screens.AddNewRun.name
    )
}


@HiltAndroidApp
class RunApp : Application() {
    // lazy --> the database and the repository are only created when they're needed
    val database by lazy { /* DataModule.getDb(this)*/ MyDatabase.getInstance(this)}
}

@Composable
fun GoHome(navController : NavHostController) {
    IconButton(onClick = { navController.popBackStack()
        navController.navigate(Screens.RunScreen.name) }) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_home_24),
            contentDescription = "Home",
        )
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    locationDetails: LocationDetails,
    b: Boolean,
    theme: String,
    svm : SettingsViewModel
){
    // Define a mutable state variable for the username
    var currentUsername by remember { mutableStateOf("InitialUsername") }
    val onUsernameChanged: (String) -> Unit = { newUsername ->
        currentUsername = newUsername
    }
    val users = hiltViewModel<UserViewModel>()
    NavHost(navController = navController,
        startDestination = Screens.RunScreen.name,
        modifier = Modifier) {
        composable(route = Screens.TodaysRun.name){
            CardRun(navController = navController, location = locationDetails)
        }
        composable(route = Screens.RunScreen.name){
            ShowRunScreen(navController, locationDetails, 155, false, b) { navController.navigate(Screens.Running.name) }
        }
        composable(route = Screens.Settings.name){
            ShowSettingsScreen(
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
                onUsernameChanged = onUsernameChanged,
                navController = navController,
                theme = theme,
                svm = svm
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
            ShowRunScreen(navController, locationDetails, 0, true, b) { navController.navigate(Screens.EndRun.name) }
        }
        composable(route = Screens.Profile.name){
            ShowProfilePage(navController)
        }
        composable(route = Screens.Notify.name){
            ShowNotifyPage(navController)
        }
        composable(route = Screens.Login.name){
            //Log.d("profile", "login")
            ShowLoginPage(navController)
        }
        composable(route = Screens.EndRun.name){
            ShowEndRunScreen(navController = navController)
        }
        composable(route = Screens.AddNewRun.name){
            NewRunScreen(navController = navController, locationDetails)
        }
        composable(route = Screens.RunInfo.name){
            ShowInfoRun(navController = navController)
        }
        composable(route = Screens.TrophyInfo.name){
            ShowChallengeInfo(navController = navController)
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