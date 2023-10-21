package com.app.runtogether

import SessionManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ShowLoginPage(navController: NavHostController) {
    val database = MyDatabase.getInstance(navController.context)

    // Step 1: Create a variable to hold the error message
    var errorMessage by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Please enter your details below",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(35.dp))
        val username = TextField(name = "Username")
        Spacer(modifier = Modifier.height(5.dp))
        val password = TextField(name = "Password", true)
        Spacer(modifier = Modifier.height(5.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = {
                    val myCoroutineScope = CoroutineScope(Dispatchers.IO)
                    var user: User? = null
                    myCoroutineScope.launch {
                        user = database.userDao().findByUsername(username)
                        if (user != null && user!!.username == username && user!!.password == password) {
                            // Successful login
                            SessionManager.createLoginSession(navController.context, user!!.user_id)
                            withContext(Dispatchers.Main) {
                                navController.navigate(Screens.RunScreen.name)
                            }
                        } else {
                            // Step 2: Update the error message
                            errorMessage = "Invalid username or password"
                        }
                    }
                },
                modifier = Modifier.padding(end = 9.dp)
            ) {
                Text(text = "Login")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = { navController.navigate(Screens.SignUp.name) }) {
                Text(text = "Sign Up")
            }
        }

        // Step 3: Display the error message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red, // You can choose an appropriate color
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}