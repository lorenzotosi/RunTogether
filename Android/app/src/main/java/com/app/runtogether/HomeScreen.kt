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
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowHomeScreen(navController : NavHostController = rememberNavController()){
    Scaffold(
        topBar = {GenerateTopBar()}
    ) {pValues ->
        NavigationGraph(navController, pValues)

        var selectedItem by remember { mutableStateOf(0) }
        val items = listOf("Run", "Today's Runs", "Challenges")

        NavigationBar (modifier = Modifier.padding(vertical = 64.dp)) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.baseline_run_circle_24),
                                    contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateTopBar(){
    CenterAlignedTopAppBar(
        title = { Text(text = "Run") },
        

    )
}

@Composable
fun NavigationGraph(navController: NavHostController, padding: PaddingValues){}