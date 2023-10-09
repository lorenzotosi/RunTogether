package com.app.runtogether

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.runtogether.swm.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSettingsScreen(
    isDarkTheme: Boolean,
    currentUsername: String,
    onThemeChanged: (Boolean) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onSaveClicked: () -> Unit, // Callback for applying the modification,
    navController: NavHostController,
    onDismiss: () -> Unit,
    theme: String,
    svm : SettingsViewModel
) {




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 50.dp
            ),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        /*Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Theme")
            Spacer(modifier = Modifier.width(16.dp))
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { newThemeState ->
                    onThemeChanged(newThemeState)
                }
            )
        }*/

        val radioOptions = listOf("Light", "Dark")
        Column(Modifier.selectableGroup()) {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (text == theme),
                            onClick = {
                                svm.saveTheme(text)
                            },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == theme),
                        onClick = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }





        Spacer(modifier = Modifier.height(10.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                SessionManager.logoutUser(navController.context)
                navController.navigate(Screens.Login.name)
            }) {
                Text("Logout") // Change the label as needed
            }
        }

    }

}
