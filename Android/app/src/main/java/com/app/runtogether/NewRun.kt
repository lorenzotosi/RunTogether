package com.app.runtogether

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

@Composable
fun NewRunScreen(navController : NavHostController){
    val database = MyDatabase.getInstance(navController.context)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Please enter run details below",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(35.dp))
        val nome = TextField(name = "Nome Corsa")
        Spacer(modifier = Modifier.height(5.dp))
        val descrizione = TextField(name = "Descrizione")
        Spacer(modifier = Modifier.height(5.dp))
        val data = TextField(name = "Data")
        Spacer(modifier = Modifier.height(5.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
            val myCoroutineScope = CoroutineScope(Dispatchers.IO)
            myCoroutineScope.launch {
                /*if (database.userDao().findByUsername(username) == null){
                    database.userDao().insertAll(User(username = username, email = email, password = password))
                }else{
                    Log.d("SignUpScreen", "Username already exists")
                }*/
            }
                             },
                modifier = Modifier.padding(end = 9.dp)) {
                Text(text = "Crea!")
            }
        }
    }
}