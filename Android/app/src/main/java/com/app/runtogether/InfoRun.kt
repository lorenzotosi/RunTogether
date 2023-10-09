package com.app.runtogether

import DateConverter
import SessionManager
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.runToUser.RunUserCrossRef
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.compose.*
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
            Text(
                text = "Citta: ${run.city}", fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Descrizione: ${run.description}", fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Distanza: ${run.length_km} KM", fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Giorno e ora: ${
                    DateConverter.toDate(run.day)?.let { formatter.format(it) }
                } ${run.startHour}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            val gson = Gson()
            val string: String = run.polyline.toString()
            val typeToken = object : TypeToken<List<LatLng>>() {}.type
            val x: List<LatLng> = gson.fromJson<List<LatLng>>(string, typeToken)
            val point = x[0]
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(point, 15f)
            }
            GoogleMap(
                modifier = Modifier
                    .height(250.dp)
                    .padding(start = 10.dp, end = 10.dp),
                cameraPositionState = cameraPositionState
            ) {
                cameraPositionState.move(CameraUpdateFactory.newLatLng(point))
                if (run.organized == true) {
                    Marker(position = LatLng(point.latitude, point.longitude))
                } else {
                    Polyline(points = x)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (SessionManager.isLoggedIn(navController.context) && run.organized == true) {

                val info = database.RunWithUsersDao()
                    .getRunsIdFromUserId(SessionManager.getUserDetails(navController.context))
                    .collectAsState(
                        initial = listOf()
                    ).value

                if (info.contains(run.run_id)) {
                    Button(onClick = {
                        val myCoroutineScope = CoroutineScope(Dispatchers.IO)
                        myCoroutineScope.launch {
                            database.RunWithUsersDao()
                                .deleteFromDb(
                                    RunUserCrossRef(
                                        run.run_id,
                                        SessionManager.getUserDetails(navController.context)
                                    )
                                )
                        }
                    }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
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
                    }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(text = "Partecipa!")
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            if (run.organized == true) {
                Button(onClick = {
                    val hour = run.startHour
                    val parti = hour?.split(":")


                    val ore = parti?.get(0)?.toIntOrNull() ?: 0
                    val minuti = parti?.getOrNull(1)?.toIntOrNull() ?: 0

                    val date: Date? = DateConverter.toDate(run.day)


                    val calendar = Calendar.getInstance().apply {
                        time = date
                    }
                    val year = calendar.get(Calendar.YEAR)
                    val month =
                        calendar.get(Calendar.MONTH) + 1
                    val day = calendar.get(Calendar.DAY_OF_MONTH)


                    val startMillis: Long = Calendar.getInstance().run {
                        set(year, month, day, ore, minuti)
                        timeInMillis
                    }

                    Log.e("tempo", "$ore $minuti, ${run.startHour}")


                    val intent = Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                        .putExtra(CalendarContract.Events.TITLE, "Corsa RunTogether")
                        .putExtra(CalendarContract.Events.DESCRIPTION, run.description)
                    startActivity(navController.context, intent, null)


                }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = "Aggiungi l'evento al calendario!")
                }

            }
        }

    }
}