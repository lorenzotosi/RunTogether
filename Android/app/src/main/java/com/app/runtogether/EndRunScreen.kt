package com.app.runtogether

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.run.Run
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.compose.*

@Composable
fun ShowEndRunScreen(navController: NavHostController){

    val db = MyDatabase.getInstance(navController.context)
    val points = GetPolyLines(db)
    val run = db.runDao().getLastRunDistance().collectAsState(initial = null).value
    Log.e("run", run.toString())
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
            .padding(top = 600.dp)
            .fillMaxWidth()){
            if (run != null) {
                Text(text = run.length_km.toString())
            }
        }
    }

}

@Composable
fun GetPolyLines(db: MyDatabase): List<LatLng> {

    val gson = Gson()
    val string: String =
        db.runDao().getOnlyPolyFromId().collectAsState(initial = listOf<LatLng>()).value.toString()
    Log.e("json", string)
    val typeToken = object : TypeToken<List<LatLng>>() {}.type

    //Log.e("json", points.toString())
    return gson.fromJson(string, typeToken)
}