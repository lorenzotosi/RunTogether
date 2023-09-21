package com.app.runtogether

import android.content.Context
import android.location.LocationManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.*

@Composable
fun ShowRunScreen(locationDetails: LocationDetails, padding : Int, mapSettings: Boolean, myLocation : Boolean,  onClickActionNavigation: () -> Unit){

    val myPosition = LatLng(locationDetails.latitude, locationDetails.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(myPosition, 15f)
    }
    val myId = if (mapSettings) R.drawable.stop_button else R.drawable.baseline_run_circle_24
    val waypoints = remember { mutableStateListOf<LatLng>() }


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
            zoomGesturesEnabled = mapSettings),
        properties = MapProperties(isMyLocationEnabled = myLocation),
    ) {
        val newPos = LatLng(locationDetails.latitude, locationDetails.longitude)
        cameraPositionState.move(CameraUpdateFactory.newLatLng(newPos))


        if (!waypoints.contains(newPos)){
            waypoints.add(newPos)
            //PolylineOptions().add(newPos)

        }

        for (p in waypoints){
            println(p.toString())
            println(waypoints.size)
            //PolylineOptions().points.add(newPos)
            Polyline(points = waypoints)
        }

        

    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(bottom = 60.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart // Align text to the start (left) within the Box
            ) {
                if (mapSettings) {
                    Text(
                        text = "15.44",
                        fontSize = 24.sp, // Increase the font size as desired
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }



            IconButton(
                onClick =  onClickActionNavigation,
                modifier = Modifier
                    .size(120.dp)
                    .zIndex(1f)
            ) {
                Icon(
                    painter = painterResource(id = myId),
                    contentDescription = "Start run",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(8.dp)
                )
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd // Align text to the end (right) within the Box
            ) {
                if (mapSettings) {
                    Text(
                        text = "10 km/h",
                        fontSize = 24.sp, // Increase the font size as desired
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }

    }

    LaunchedEffect(Unit) {
        cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPositionState.position))
    }
}
