package com.app.runtogether

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun ShowRunScreen(locationDetails: LocationDetails, onClickActionNavigation: () -> Unit){

    val myPosition = LatLng(locationDetails.latitude, locationDetails.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(myPosition, 15f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(compassEnabled = false,
            indoorLevelPickerEnabled = false,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = true,
            rotationGesturesEnabled = false,
            scrollGesturesEnabled = false,
            scrollGesturesEnabledDuringRotateOrZoom = false,
            tiltGesturesEnabled = false,
            zoomControlsEnabled = false,
            zoomGesturesEnabled = false)
    ) {
        Marker(
            position = myPosition,
            title = "Your position",
            snippet = "You are here"
        )
        val newPos = LatLng(locationDetails.latitude, locationDetails.longitude)
        cameraPositionState.move(CameraUpdateFactory.newLatLng(newPos))
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight().padding(bottom = 90.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick =  onClickActionNavigation ,
            modifier = Modifier
                .size(120.dp)
                .zIndex(1f)
        ) {
            Text(text = "Cliccami")
        }
    }
}
