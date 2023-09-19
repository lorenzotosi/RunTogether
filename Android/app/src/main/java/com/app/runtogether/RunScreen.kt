package com.app.runtogether

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
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
            .fillMaxSize()),
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
}
