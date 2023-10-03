package com.app.runtogether

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.run.Run
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.compose.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ShowEndRunScreen(navController: NavHostController){

    val db = MyDatabase.getInstance(navController.context)
    val points = getPolyLines(db)
    val run = db.runDao().getLastRunDistance().collectAsState(initial = null).value
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 15f)
    }
    Box(modifier = Modifier
        .fillMaxSize()){
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)){
            GoogleMap(cameraPositionState = cameraPositionState) {
                Polyline(points = points)
            }
            LaunchedEffect(points){
                if (points.isNotEmpty())
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(points.first(), 15f))
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 400.dp, start = 90.dp)){
            Button(onClick = { cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(points.first(), 15f)) }) {
                Text(text = "Torna al punto di partenza")
            }
        }
        Row(modifier = Modifier
            .padding(top = 480.dp, start = 25.dp, end = 25.dp)
            .fillMaxWidth()){
            if (run != null) {
                Text(text = "Distanza percorsa: ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold)
                Text(text = "${run.length_km} KM",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold)
            }
        }
        Row(modifier = Modifier
            .padding(top = 540.dp, start = 25.dp, end = 25.dp)
            .fillMaxWidth()){
            if (run != null) {
                Text(text = "Orario di inizio ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold)
            }
            if (run != null) {
                Text(text = "${run.startHour}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold)
            }
        }
        Row(modifier = Modifier
            .padding(top = 600.dp, start = 25.dp, end = 25.dp)
            .fillMaxWidth()){
            if (run != null) {
                Text(text = "Orario di fine ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold)
            }
            if (run != null) {
                Text(text = "${run.endHour}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold)
            }
        }
        Row(modifier = Modifier
            .padding(top = 660.dp, start = 25.dp, end = 25.dp)
            .fillMaxWidth()){
            if (run != null) {
                Text(text = "Andatura media ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold)
            }


            if (run != null) {

                val orarioFine = run.endHour
                val orarioInizio = run.startHour
                val distance = run.length_km

                val formatter = DateTimeFormatter.ofPattern("HH:mm")

                val inizio = LocalTime.parse(orarioInizio, formatter)
                val fine = LocalTime.parse(orarioFine, formatter)

                val differenzaInMinuti = fine.toSecondOfDay() / 60 - inizio.toSecondOfDay() / 60

                if (differenzaInMinuti < 0) {
                    // Se l'orario di fine è prima di quello di inizio (es. giorno successivo), aggiungi 24 ore
                    differenzaInMinuti + 24 * 60
                }

                var velMed = 0.0

                if (differenzaInMinuti != 0){
                    if (distance != null) {
                        velMed = (distance)*(60/differenzaInMinuti)
                    }
                }

                Text(text = " ${String.format("%.2f", velMed)} km/h",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold)
            }
        }
    }

}

@Composable
fun getPolyLines(db: MyDatabase): List<LatLng> {
    val gson = Gson()
    val string: String =
        db.runDao().getOnlyPolyFromId().collectAsState(initial = listOf<LatLng>()).value.toString()
    val typeToken = object : TypeToken<List<LatLng>>() {}.type
    return gson.fromJson(string, typeToken)
}