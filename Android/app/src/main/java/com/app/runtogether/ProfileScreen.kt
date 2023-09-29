package com.app.runtogether

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.user.UserViewModel
import kotlinx.coroutines.withContext

@Composable
fun ShowProfilePage(navController: NavHostController){
    val users = hiltViewModel<UserViewModel>()
    val db = MyDatabase.getInstance(navController.context)
    val userId = SessionManager.getUserDetails(navController.context)
    val numberOfTrophies = db.UserWithTrophiesDao().getNumberOfTrophies(userId)
        .collectAsState(initial = Int).value
    val numberOfRuns = db.RunWithUsersDao().getNumberOfRunsJoined(userId)
        .collectAsState(initial = Int).value
    val username = db.userDao().getUsernameFromId(userId)
        .collectAsState(initial = String).value
    val trophies = db.UserWithTrophiesDao().getTrophyHave(userId)
        .collectAsState(initial = listOf()).value

    Log.d("db", db.UserWithTrophiesDao().getUserWithTrophies().collectAsState(initial = listOf()).value.toString())


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 100.dp),
        contentAlignment = Alignment.TopStart
    ){
        Text(

            text = "$username",

            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 25.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 50.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = painterResource(id = R.drawable.image_profile),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(8.dp)
            )


            Text(

                text = "$numberOfTrophies Trophies",

                fontSize = 24.sp, // Increase the font size as desired
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )

            Text(
                text = "$numberOfRuns runs",
                fontSize = 24.sp, // Increase the font size as desired
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        LazyHorizontalGrid(modifier=Modifier,rows = GridCells.Fixed(1) , content ={
            items(count = trophies.size){
                trophies[it].path?.let { it1 -> painterResource(id = it1) }?.let { it2 ->
                    Image(
                        painter = it2,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
        } )
    }
}