import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsScreen(
    isDarkTheme: Boolean,
    currentUsername: String,
    onThemeChanged: (Boolean) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onSaveClicked: () -> Unit, // Callback for applying the modification
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
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

        Spacer(modifier = Modifier.height(16.dp))

        // Apply button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = currentUsername,
                onValueChange = { newUsername -> onUsernameChanged(newUsername) },
                label = { Text("Username") },
                modifier = Modifier
                    .widthIn(max = 200.dp) // Set the maximum width for the input field
            )
            TextButton(onClick = { onSaveClicked() }) {
                Text("Apply") // Change the label as needed
            }
        }
    }
}
