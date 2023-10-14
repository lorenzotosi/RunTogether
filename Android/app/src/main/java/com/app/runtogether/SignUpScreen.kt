package com.app.runtogether



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.MainScope
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
        val reTypedPassword = TextField(name="Re-Type Pass", true)
        Spacer(modifier = Modifier.height(35.dp))
        val errorMessage = remember { mutableStateOf<String?>(null) }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                if (password == reTypedPassword){
                    val myCoroutineScope = MainScope()
                    myCoroutineScope.launch {
                        if (database.userDao().findByUsername(username) == null) {
                            database.userDao().insertAll(User(username = username, password = password, path = ""))
                            navController.navigate(Screens.Login.name)
                        } else {
                            errorMessage.value = "Username already exists"
                        }
                    }
                } else {
                    errorMessage.value = "Passwords do not match" // Set the error message for password mismatch
                }
            }, modifier = Modifier.padding(end = 9.dp)) {
                Text(text = "Sign Up")
            }
        }
        Button(onClick = { navController.navigate(Screens.Login.name) },
            modifier = Modifier.padding(end = 9.dp)) {
            Text(text = "Go to Login")
        }
        if (errorMessage.value != null) {
            Text(
                text = errorMessage.value!!,
                color = Color.Red, // Color of the error message
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
