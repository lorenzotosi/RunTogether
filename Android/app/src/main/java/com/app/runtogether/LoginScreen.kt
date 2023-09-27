package com.app.runtogether

import SessionManager
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Please enter your details below",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(35.dp))
        val username = TextField(name = "Username or Email")
        Spacer(modifier = Modifier.height(5.dp))
        val password = TextField(name = "Password", true)
        Spacer(modifier = Modifier.height(5.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { navController.navigate(Screens.SignUp.name) }) {
                Text(text = "Go to Sign Up")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    val myCoroutineScope = CoroutineScope(Dispatchers.IO)
                    var user: User? = null
                    myCoroutineScope.launch {
                        user = database.userDao()
                            .findByUsername(username)
                        Log.d("LoginScreen", "User: $user")
                        if ((user != null) && (user?.username == username) && (user?.password == password)) {
                            SessionManager.createLoginSession(navController.context, user!!.user_id.toString())
                            withContext(Dispatchers.Main) {
                                navController.navigate(Screens.RunScreen.name)
                            }
                        }
                    }
                },
                modifier = Modifier.padding(end = 9.dp)
            ) {
                Text(text = "Login")

            }

        }
    }
}
