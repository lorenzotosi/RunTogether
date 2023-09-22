package com.app.runtogether


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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(name:String, isPassword: Boolean = false){
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSignUpPage(navController : NavHostController){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
            Text(
                text = "Please enter your details below",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(35.dp))
            TextField(name = "Username")
            Spacer(modifier = Modifier.height(5.dp))
            TextField(name = "Email")
            Spacer(modifier = Modifier.height(5.dp))
            TextField(name = "Password", true)
            Spacer(modifier = Modifier.height(5.dp))
            TextField(name="Re-Type Pass", true)
            Spacer(modifier = Modifier.height(35.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(onClick = { navController.navigate(Screens.Login.name) },
                    modifier = Modifier.padding(end = 9.dp)) {
                    Text(text = "Go to Login")
                }
            }
    }

}