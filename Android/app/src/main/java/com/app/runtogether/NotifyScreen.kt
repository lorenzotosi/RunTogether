package com.app.runtogether

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase

@Composable
fun ShowNotifyPage(navController: NavHostController) {
    val database = MyDatabase.getInstance(navController.context)
    val notifies = database.NotifyDao().findByUid(SessionManager.getUserDetails(navController.context)).collectAsState(
        initial = emptyList()
    ).value
    
    Log.e("Notify", notifies.toString())
    Log.e("NotifySize", "Size: ${notifies.size}")

    LazyVerticalGrid(
        columns = GridCells.Fixed(1)
    ) {
        items(count = notifies.size) {
            Log.e("NotifyItem", "Item: ${notifies[it]}")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screens.Profile.name) }
                    .padding(8.dp)
            ) {
                Text(text = notifies[it])
            }
        }
    }
}

