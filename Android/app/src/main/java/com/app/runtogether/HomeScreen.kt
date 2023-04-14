package com.app.runtogether

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowHomeScreen(navController : NavHostController = rememberNavController()){
    Scaffold(
        topBar = {GenerateTopBar()}
    ) {pValues ->
        NavigationGraph(navController, pValues)

        var selectedItem by remember { mutableStateOf(0) }
        val items = listOf("Songs", "Artists", "Playlists")

        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        }
    }

}

@Composable
fun GenerateTopBar(){

}

@Composable
fun NavigationGraph(navController: NavHostController, padding: PaddingValues){}