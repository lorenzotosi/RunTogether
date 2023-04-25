package com.app.runtogether


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(name:String, spacing: Int){
    Row(verticalAlignment = Alignment.CenterVertically) {
        val textState = remember { mutableStateOf(TextFieldValue()) }
        Text("$name:")
        Spacer(modifier = Modifier.width(spacing.dp))
        TextField(
            value = textState.value,
            onValueChange = { textState.value = it }
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSignUpPage(){
    val textName = remember { mutableStateOf(TextFieldValue("Name")) }
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = "SignUp Page",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Please enter your details below",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(50.dp))
            TextField(name = "*Name", spacing = 62)
            Spacer(modifier = Modifier.height(5.dp))
            TextField(name = "*Email", spacing = 64)
            Spacer(modifier = Modifier.height(5.dp))
            TextField(name = "*Password", spacing = 32)
            Spacer(modifier = Modifier.height(5.dp))
            TextField(name="*Re-Type Pass", spacing = 5)
            Spacer(modifier = Modifier.height(50.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Sign Up")
            }
    }
}