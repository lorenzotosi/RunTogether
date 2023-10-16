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
        modifier = Modifier
            .padding(top = 75.dp),
        columns = GridCells.Fixed(1)
    ) {
        items(count = notifies.size) {
            val isRun = notifies[it].run_id != null
            val isChallenge = notifies[it].challenge_id != null


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if(isRun){
                            run_id = notifies[it].run_id!!
                            navController.navigate(Screens.RunInfo.name)
                        }
                        else if(isChallenge){
                            myChallenge = notifies[it].challenge_id!!
                            navController.navigate(Screens.TrophyInfo.name)
                        }
                    }
                    .padding(8.dp)
            ) {
                notifies[it].text?.let { it1 -> Text(text = it1) }
            }
        }
    }
}

