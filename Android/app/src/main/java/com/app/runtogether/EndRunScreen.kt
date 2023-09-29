package com.app.runtogether

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.compose.*
import java.lang.Thread.sleep

@Composable
fun ShowEndRunScreen(navController: NavHostController){

    val points = GetPolyLines(navController)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 20f)
        //maybe set a real location
    }

    GoogleMap(cameraPositionState = cameraPositionState) {
        Polyline(points = points)
    }

    LaunchedEffect(points){
        if (points.isNotEmpty())
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(points.first(), 20f))
    }

    //TODO pulsante per centrare la posizione sulle coordinate della polyline

}

@Composable
fun GetPolyLines(navController: NavHostController): List<LatLng> {
    val db = MyDatabase.getInstance(navController.context)
    val gson = Gson()
    val string: String =
        db.runDao().getOnlyPolyFromId().collectAsState(initial = listOf<LatLng>()).value.toString()
    Log.e("json", string)
    val typeToken = object : TypeToken<List<LatLng>>() {}.type

    //Log.e("json", points.toString())
    return gson.fromJson(string, typeToken)
}