package com.app.runtogether

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun ShowRunScreen(){
    val myPosition = LatLng(44.0678288, 12.5695158)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(myPosition, 800f)
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