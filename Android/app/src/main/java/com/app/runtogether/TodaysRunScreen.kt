package com.app.runtogether

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ShowButton(navController: NavHostController){
    Button(onClick = { navController.navigate(Screens.SignUp.name) }) {
        Text(text = "cliccami")
    }
}
