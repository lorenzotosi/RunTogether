package com.app.runtogether

import DateConverter
import SessionManager
import android.location.Geocoder
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
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
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.run.Run
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Math.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow


@Composable
fun ShowRunScreen(
    navController: NavHostController,
    locationDetails: LocationDetails,
    padding: Int,
    mapSettings: Boolean,
    myLocation: Boolean,
    onClickActionNavigation: () -> Unit
) {

    val myPosition = LatLng(locationDetails.latitude, locationDetails.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(myPosition, 15f)
    }

    val gson = Gson()

    val myId = if (mapSettings) R.drawable.stop_button else R.drawable.baseline_run_circle_24
    var waypoints by remember { mutableStateOf<List<LatLng>>(value = listOf()) }

    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val current = formatter.format(time)

    val startTime by remember { mutableStateOf(System.currentTimeMillis()) }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = padding.dp),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            compassEnabled = false,
            indoorLevelPickerEnabled = mapSettings,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = true,
            rotationGesturesEnabled = mapSettings,
            scrollGesturesEnabled = mapSettings,
            scrollGesturesEnabledDuringRotateOrZoom = mapSettings,
            tiltGesturesEnabled = false,
            zoomControlsEnabled = false,
            zoomGesturesEnabled = mapSettings
        ),
        properties = MapProperties(isMyLocationEnabled = myLocation,
                                    minZoomPreference = 10f,
                                    maxZoomPreference = 20f)
    ) {

        if (myLocation) {
            val newPos = LatLng(locationDetails.latitude, locationDetails.longitude)
            //cameraPositionState.move(CameraUpdateFactory.newLatLng(newPos))
            LaunchedEffect(newPos) {
                cameraPositionState.centerOnLocation(newPos)
            }

            if (mapSettings) {
                if (!waypoints.contains(newPos) && newPos != LatLng(0.toDouble(), 0.toDouble())) {
                    waypoints = waypoints + newPos
                }
                UpdatePolyline(waypoints = waypoints)
            }
        } else {
            val newPos = LatLng(44.06, 12.56)
            LaunchedEffect(newPos) {
                cameraPositionState.centerOnLocation(newPos)
            }
        }

    }
    if (myLocation) {
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
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart // Align text to the start (left) within the Box
                ) {
                    if (mapSettings) {
                        Text(
                            text = current,
                            fontSize = 24.sp, // Increase the font size as desired
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.Black
                        )
                    }
                }

                IconButton(
                    onClick = {
                        if (mapSettings) {
                            val x = Geocoder(navController.context).getFromLocation(
                                waypoints[0].latitude,
                                waypoints[0].longitude,
                                1
                            )?.get(0)?.locality.toString()
                            val myCoroutineScope = CoroutineScope(Dispatchers.IO)
                            myCoroutineScope.launch {
                                val db = MyDatabase.getInstance(navController.context)
                                db.runDao().insertRun(
                                    Run(
                                        user_id = SessionManager.getUserDetails(navController.context),
                                        city = x,
                                        description = "descrizione prova",
                                        length_km = calculateTotalDistance(waypoints),
                                        day = DateConverter.fromDate(Date()),
                                        polyline = gson.toJson(waypoints),
                                        organized = false,
                                        startHour = formatTime(startTime),
                                        endHour = current
                                    )
                                )
                            }
                        }
                        onClickActionNavigation.invoke()
                    },
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
                            text = "${(calculateTotalDistance(waypoints))} km",
                            fontSize = 24.sp, // Increase the font size as desired
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 8.dp),
                            color = Color.Black
                        )
                    }
                }
            }

        }
    }

}

suspend fun CameraPositionState.centerOnLocation(location: LatLng) = animate(
    update = CameraUpdateFactory.newLatLng(
        location
    )
)

fun formatTime(timeInMillis: Long): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timeInMillis))
}

@Composable
fun UpdatePolyline(waypoints: List<LatLng>) {
    Polyline(points = waypoints)
}

fun calculateTotalDistance(points: List<LatLng>): Double {
    var totalDistance = 0.0

    for (i in 0 until points.size - 1) {
        val lat1 = points[i].latitude
        val lon1 = points[i].longitude
        val lat2 = points[i + 1].latitude
        val lon2 = points[i + 1].longitude

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        val radiusOfEarth = 6371.0 // Radius of the Earth in kilometers

        val distance = radiusOfEarth * c // Distance in kilometers
        totalDistance += distance
    }
    val formattedDistance = String.format("%.3f", totalDistance)


    return formattedDistance.toDouble()
}

