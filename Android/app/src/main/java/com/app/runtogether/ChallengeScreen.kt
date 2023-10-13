package com.app.runtogether

import SessionManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase

var myChallenge = -1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGrid(index: Int, navController: NavHostController) {

    val database = MyDatabase.getInstance(navController.context)
    val userId = SessionManager.getUserDetails(navController.context)
    val trophies = database.UserWithTrophiesDao().getTrophyNotHave(userId)
        .collectAsState(initial = listOf()).value
    val trophiesFavorite = database.FavoriteTrophyUserDao().getFavoriteTrophyUser(userId).collectAsState(
        initial = listOf()
    ).value
    val trophiesNotFavorite = database.FavoriteTrophyUserDao().getNotFavoriteTrophyUser(userId).collectAsState(
        initial = listOf()
    ).value

    LazyVerticalGrid(
        modifier = Modifier.padding(top = 155.dp),
        columns = GridCells.Fixed(2),
        content = {
            items(count = trophiesFavorite.size) {
                Card(modifier = Modifier
                    .size(150.dp, 150.dp)
                    .padding(8.dp)
                    .fillMaxWidth(),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                    onClick = {
                        myChallenge = trophiesFavorite[it].trophy_id
                        navController.navigate(Screens.TrophyInfo.name)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 12.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        trophiesFavorite[it].path?.let { it1 -> painterResource(id = it1) }?.let { it2 ->
                            Image(
                                painter = it2,
                                contentDescription = "travel image",
                                modifier = Modifier
                                    .clip(shape = CircleShape)
                                    .size(size = 50.dp),
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSecondaryContainer)
                            )
                        }
                        Text(
                            text = "Trofeo ${trophiesFavorite[it].km} KM",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            items(count = trophiesNotFavorite.size) {
                Card(modifier = Modifier
                    .size(150.dp, 150.dp)
                    .padding(8.dp)
                    .fillMaxWidth(),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer),
                    onClick = {
                        myChallenge = trophiesNotFavorite[it].trophy_id
                        navController.navigate(Screens.TrophyInfo.name)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 12.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        trophiesNotFavorite[it].path?.let { it1 -> painterResource(id = it1) }?.let { it2 ->
                            Image(
                                painter = it2,
                                contentDescription = "travel image",
                                modifier = Modifier
                                    .clip(shape = CircleShape)
                                    .size(size = 50.dp),
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSecondaryContainer)
                            )
                        }
                        Text(
                            text = "Trofeo ${trophiesNotFavorite[it].km} KM",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        })
}


