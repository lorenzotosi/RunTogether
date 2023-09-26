package com.app.runtogether


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(name:String, isPassword: Boolean = false) : String{
        val textState = remember { mutableStateOf(TextFieldValue()) }
        OutlinedTextField(
            value = textState.value,
            placeholder = { Text("$name") },
            label = { Text("$name:") },
            onValueChange = { textState.value = it } ,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
            )
    return textState.value.text
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSignUpPage(navController : NavHostController){
    val database = MyDatabase.getInstance(navController.context)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
            Text(
                text = "Please enter your details below",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(35.dp))
            val username = TextField(name = "Username")
            Spacer(modifier = Modifier.height(5.dp))
            val email = TextField(name = "Email")
            Spacer(modifier = Modifier.height(5.dp))
            val password = TextField(name = "Password", true)
            Spacer(modifier = Modifier.height(5.dp))
            TextField(name="Re-Type Pass", true)
            Spacer(modifier = Modifier.height(35.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    val myCoroutineScope = CoroutineScope(Dispatchers.IO)

                    myCoroutineScope.launch {
                        /*if (database.userDao().findByUsername(username).username == null){
                            database.userDao().insertAll(User(username = username, email = email, password = password))
                        }*/
                        Log.d("username", database.userDao().findByUsername(username).toString())
                    }
                    navController.navigate(Screens.Login.name)},
                    modifier = Modifier.padding(end = 9.dp)) {
                    Text(text = "Go to Login")
                }
            }
    }

}