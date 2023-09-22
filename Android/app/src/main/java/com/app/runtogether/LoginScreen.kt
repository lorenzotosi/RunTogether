package com.app.runtogether

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ShowLoginPage(navController: NavHostController){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Please enter your details below",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(35.dp))
        TextField(name = "Username or Email")
        Spacer(modifier = Modifier.height(5.dp))
        TextField(name = "Password", true)
        Spacer(modifier = Modifier.height(5.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { navController.navigate(Screens.SignUp.name) }) {
                Text(text = "Go to Sign Up")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = { navController.navigate(Screens.RunScreen.name) },
                modifier = Modifier.padding(end = 9.dp)) {
                Text(text = "Login")
            }

        }
    }
}
