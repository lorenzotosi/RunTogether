package com.app.runtogether

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun ShowRunScreen(locationDetails: LocationDetails, padding : Int, mapSettings: Boolean, onClickActionNavigation: () -> Unit){

    val myPosition = LatLng(locationDetails.latitude, locationDetails.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(myPosition, 15f)
    }
    
    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = padding.dp),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(compassEnabled = false,
            indoorLevelPickerEnabled = mapSettings,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = mapSettings,
            rotationGesturesEnabled = mapSettings,
            scrollGesturesEnabled = mapSettings,
            scrollGesturesEnabledDuringRotateOrZoom = mapSettings,
            tiltGesturesEnabled = false,
            zoomControlsEnabled = false,
            zoomGesturesEnabled = mapSettings)
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
            .fillMaxHeight()
            .padding(bottom = 60.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        IconButton(
            onClick =  onClickActionNavigation,
            modifier = Modifier
                .size(120.dp)
                .zIndex(1f)
                .clickable { }
                .then(Modifier.padding(8.dp))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_run_circle_24),
                contentDescription = "Start run",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(8.dp)
            )

        }
    }
}
