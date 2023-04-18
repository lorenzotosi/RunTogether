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
        val map = mapOf("Run" to R.drawable.baseline_run_circle_24,
                        "Today's Runs" to R.drawable.baseline_today_24,
                        "Challenges" to R.drawable.round_stars_24)

        NavigationBar (modifier = Modifier.padding(vertical = 64.dp)) {
            map.entries.forEachIndexed { k, v -> NavigationBarItem(
                icon = { Icon(painter = painterResource(id = v.value),
                    contentDescription = v.key) },
                label = { Text(v.key) },
                selected = selectedItem == k,
                onClick = { selectedItem = k /* todo */}
            ) }
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