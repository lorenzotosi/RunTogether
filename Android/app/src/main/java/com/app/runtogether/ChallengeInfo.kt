package com.app.runtogether

import DateConverter
import SessionManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.favorite.Favorite
import com.app.runtogether.db.runToUser.RunUserCrossRef
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ShowChallengeInfo(navController: NavHostController) {
    val database = MyDatabase.getInstance(navController.context)
    val trophy =
        database.trophyDao().getTrophyFromId(myChallenge).collectAsState(initial = null).value
    val favoriteNum = database.FavoriteTrophyUserDao().getFavoriteTrophyUser(SessionManager.getUserDetails(navController.context), myChallenge).collectAsState(
        initial = null).value
    val favorite = favoriteNum != null && favoriteNum > 0
    var completed = false

    if (SessionManager.isLoggedIn(navController.context)) {
        completed = database.UserWithTrophiesDao()
            .hasUserGotTrophy(SessionManager.getUserDetails(navController.context), myChallenge)
            .collectAsState(
                initial = false
            ).value
    }

    Column(
        modifier = Modifier.padding(top = 30.dp, start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (trophy != null) {

            Spacer(modifier = Modifier.height(35.dp))
            Text(
                text = "Nome: Trofeo ${trophy.km}KM", fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Descrizione: ${trophy.description}", fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Obiettivo: ${trophy.km} KM", fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Completata: ${if (completed) "SI" else "NO"}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            trophy.path?.let { painterResource(it) }
                ?.let {
                    Image(
                        painter = it,
                        contentDescription = trophy.description,
                        modifier = Modifier.size(200.dp)
                    )
                }
            Spacer(modifier = Modifier.height(10.dp))
            // add to favorite button
            if(!completed) {
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (!favorite) {
                            database.FavoriteTrophyUserDao().insertFavoriteTrophyUser(
                                Favorite(
                                    SessionManager.getUserDetails(navController.context),
                                    myChallenge
                                )
                            )
                        } else {
                            database.FavoriteTrophyUserDao().deleteFavoriteTrophyUser(
                                Favorite(
                                    SessionManager.getUserDetails(navController.context),
                                    myChallenge
                                )
                            )
                        }
                    }
                }) {
                    if (!favorite) {
                        Text(text = "Aggiungi ai preferiti")
                    } else {
                        Text(text = "Rimuovi dai preferiti")
                    }
                }
            }
        }
    }
}