package com.app.runtogether

import DateConverter
import SessionManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
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
fun ShowInfoRun(navController: NavHostController){
    val database = MyDatabase.getInstance(navController.context)
    val run = database.runDao().getRunFromId(run_id).collectAsState(initial = null).value

    Column(
        modifier = Modifier.padding(top = 30.dp, start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.Start
    ){
        if (run != null) {
            val formatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            Spacer(modifier = Modifier.height(35.dp))
            Text(text = "Citta: ${run.city}",fontSize = 24.sp,
                fontWeight = FontWeight.Bold,)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Descrizione: ${run.description}",fontSize = 24.sp,
                fontWeight = FontWeight.Bold,)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Distanza: ${run.length_km} KM",fontSize = 24.sp,
                fontWeight = FontWeight.Bold,)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Giorno e ora: ${DateConverter.toDate(run.day)?.
                let { formatter.format(it) }} ${run.startHour}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,)
            Spacer(modifier = Modifier.height(10.dp))
            val gson = Gson()
            val string: String = run.polyline.toString()
            val typeToken = object : TypeToken<List<LatLng>>() {}.type
            val x = gson.fromJson<List<LatLng>>(string, typeToken)
            val point = x[0]
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(point, 15f)
            }
            GoogleMap(modifier = Modifier
                .height(250.dp)
                .padding(start = 10.dp, end = 10.dp),
            cameraPositionState = cameraPositionState) {
                cameraPositionState.move(CameraUpdateFactory.newLatLng(point))
                Marker(position = LatLng(point.latitude, point.longitude))
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (SessionManager.isLoggedIn(navController.context)) {

                val info = database.RunWithUsersDao()
                    .getRunsIdFromUserId(SessionManager.getUserDetails(navController.context)).collectAsState(
                        initial = listOf()
                    ).value

                if (info.contains(run.run_id)){
                    Button(onClick = {
                        val myCoroutineScope = CoroutineScope(Dispatchers.IO)
                        myCoroutineScope.launch {
                            database.RunWithUsersDao()
                                .deleteFromDb(RunUserCrossRef(run.run_id,
                                    SessionManager.getUserDetails(navController.context)))
                        }
                    }) {
                        Text(text = "Disiscriviti")
                    }
                } else {
                    Button(onClick = {
                        val myCoroutineScope = CoroutineScope(Dispatchers.IO)
                        myCoroutineScope.launch {
                            database.RunWithUsersDao()
                                .insertRunUserCrossRef(
                                    RunUserCrossRef(
                                        run_id = run.run_id,
                                        SessionManager.getUserDetails(navController.context)
                                    )
                                )
                        }
                    }) {
                        Text(text = "Partecipa!")
                    }
                }
            }

        }

    }
}