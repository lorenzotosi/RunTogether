package com.app.runtogether

import SessionManager
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.app.runtogether.ui.theme.RunTogetherTheme
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var requestingLocationUpdates = false

    //private lateinit var locationPermissionRequest: ActivityResultLauncher<String>
    private lateinit var locationPermissionRequest : ActivityResultLauncher<Array<String>>

    private var showSnackBar = mutableStateOf(false)
    private var showAlertDialog = mutableStateOf(false)
    //maybe set a real location instead of 0 0
    private val location = mutableStateOf(LocationDetails(0.toDouble(), 0.toDouble()))

    private var loc = false

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        //SessionManager.logoutUser(applicationContext)

        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted ->

            when{
                isGranted.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    startLocationUpdates()
                }
                else -> {
                    showSnackBar.value = true
                }
            }

            /*if (isGranted) {
                loc = true
                startLocationUpdates()
            } else {
                loc = false
                showSnackBar.value = true
            }*/
        }

        locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1500).apply {
                setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                location.value = LocationDetails(
                    p0.locations.last().latitude,
                    p0.locations.last().longitude
                )
                loc = true
                //stopLocationUpdates()
                //requestingLocationUpdates = false
            }
        }

        setContent {
            RunTogetherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        content = { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                startLocationUpdates()
                                ModalNavigationDrawerSample(location.value, loc)


                            }
                        }
                    )
                    if (showSnackBar.value) {
                        SnackBarComposable(snackbarHostState, this, showSnackBar)
                    }
                    if (showAlertDialog.value) {
                        AlertDialogComposable(this, showAlertDialog)
                    }
                }
            }
        }

    }
    private fun startLocationUpdates() {
        requestingLocationUpdates = true
        val permission = Manifest.permission.ACCESS_FINE_LOCATION

        when {
            //permission already granted
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                val gpsEnabled = checkGPS()
                if (gpsEnabled) {
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                } else {
                    showAlertDialog.value = true
                }

            }
            //permission already denied
            shouldShowRequestPermissionRationale(permission) -> {
                showSnackBar.value = true
            }
            else -> {
                //first time: ask for permissions
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_CALENDAR)
                )
            }
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun checkGPS(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }
}
