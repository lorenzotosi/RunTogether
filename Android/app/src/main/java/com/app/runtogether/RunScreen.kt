package com.app.runtogether

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@Composable
fun ShowRunScreen(locationDetails: LocationDetails){

    val myPosition = LatLng(locationDetails.latitude, locationDetails.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(myPosition, 15f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize().padding(bottom = 90.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            position = myPosition,
            title = "Your position",
            snippet = "You are here"
        )


            val newPos = LatLng(locationDetails.latitude, locationDetails.longitude)
            cameraPositionState.move(CameraUpdateFactory.newLatLng(newPos))
    }
}

suspend fun UpdatePos(locationDetails: LocationDetails) {

}

/*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ShowRunScreen(locationDetails: LocationDetails) {
    var myPosition by remember { mutableStateOf(LatLng(locationDetails.latitude, locationDetails.longitude)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(myPosition, 15f) // 15f is the zoom level
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        scope.launch {
            while (true) {
                // Simulate getting updated position data from your source (e.g., locationDetails)
                // Replace the values below with your actual updated location
                val updatedLatitude = locationDetails.latitude
                val updatedLongitude = locationDetails.longitude
                myPosition = LatLng(updatedLatitude, updatedLongitude)

                // Update the camera position with the new position
                cameraPositionState.position = CameraPosition.fromLatLngZoom(myPosition, 15f)

                // Wait for 5 seconds before the next update
                delay(5000)
            }
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxWidth().height(500.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            position = myPosition,
            title = "Your position",
            snippet = "You are here"
        )
    }
}
*/
