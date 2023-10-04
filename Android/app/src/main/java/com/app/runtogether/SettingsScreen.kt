import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.runtogether.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSettingsScreen(
    isDarkTheme: Boolean,
    currentUsername: String,
    onThemeChanged: (Boolean) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onSaveClicked: () -> Unit, // Callback for applying the modification,
    navController: NavHostController,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 50.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // Dark Theme switch
        Row(
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
