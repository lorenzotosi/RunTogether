package com.app.runtogether

import SessionManager
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.run.Run
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TextCard(title: String, fontSize:Int){
    Text(
        text = title,
        fontSize = fontSize.sp,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        textAlign = TextAlign.Center
    )
}

var run_id = -1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardRun(navController: NavHostController, location : LocationDetails){
    val database = MyDatabase.getInstance(navController.context)

    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        val x = Geocoder(navController.context, Locale.getDefault())
            .getFromLocation(location.latitude, location.longitude, 1)
        var city = ""
        if (x != null) {
            if (x.isNotEmpty()){
                city = x[0].locality
            }
        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = DateConverter.fromDate(calendar.time) // Start of the day (00:00)

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = DateConverter.fromDate(calendar.time) // End of the day (23:59)

        var runs  by remember { mutableStateOf<List<Run>>(value = listOf()) }
        if(startOfDay != null && endOfDay != null){
            runs = database.runDao().getRunsFromCityForToday(city, startOfDay, endOfDay).collectAsState(
                initial = listOf()
            ).value
        }
        Row(modifier = Modifier.padding(top = 155.dp)) {
            Button(onClick = {
                val myCoroutineScope = CoroutineScope(Dispatchers.IO)
                myCoroutineScope.launch {
                    if(startOfDay != null && endOfDay != null){
                        runs = database.runDao().getRunsFromCityForTodayNoFlow(city, startOfDay, endOfDay)
                    }
                }
            }) {
                Text(text = "Oggi")
            }
            Spacer(modifier = Modifier.width(5.dp))
            Button(onClick = {
                val myCoroutineScope = CoroutineScope(Dispatchers.IO)
                myCoroutineScope.launch {
                    runs = database.runDao().getAllFutureOrganizedRuns(startOfDay!!)
                }
            }) {
                Text(text = "Future")
            }
            Spacer(modifier = Modifier.width(5.dp))
            Button(onClick = {
                val myCoroutineScope = CoroutineScope(Dispatchers.IO)
                myCoroutineScope.launch {
                    runs = database.runDao().getAllOrganizedRuns()
                }
            }) {
                Text(text = "Tutte")
            }
        }






        LazyVerticalGrid(modifier = Modifier.padding(top = 0.dp),
                columns = GridCells.Fixed(1), content = {
            items(count = runs.size) {
                Card(modifier = Modifier
                    .size(150.dp, 150.dp)
                    .padding(8.dp)
                    .fillMaxWidth(),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                    onClick = { run_id = runs[it].run_id
                                //Log.e("corsa", run_id.toString())
                                navController.navigate(Screens.RunInfo.name)},
                    enabled = runs[it].day!! >= startOfDay!!
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 12.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row ( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                            runs[it].city?.let { it1 -> TextCard(title = it1, fontSize = 25) }
                            Spacer(modifier = Modifier.width(5.dp))
                            TextCard(title = "${runs[it].length_km}km", fontSize = 25)
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Row ( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            TextCard(title = "${formatter.format(runs[it].day)}, ${runs[it].startHour}", fontSize = 15)
                        }

                    }
                }
            }
        })

    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(bottom = 20.dp, end = 20.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        IconButton(
            onClick = { navController.navigate(Screens.AddNewRun.name) },
            modifier = Modifier
                .size(80.dp)
                .zIndex(10f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_24),
                contentDescription = "Start run",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(8.dp)
            )
        }
    }
}