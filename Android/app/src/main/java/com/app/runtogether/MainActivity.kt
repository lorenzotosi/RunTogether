package com.app.runtogether

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.app.runtogether.ui.theme.RunTogetherTheme
import com.google.android.gms.location.*


class MainActivity : ComponentActivity(), DeviceLocationTracker.DeviceLocationListener {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var requestingLocationUpdates = false

    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>

     var showSnackBar = mutableStateOf(false)
     var showAlertDialog = mutableStateOf(false)
    private val location = mutableStateOf(LocationDetails(0.toDouble(), 0.toDouble()))


    private lateinit var deviceLocationTracker: DeviceLocationTracker

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startLocationUpdates()
            } else {
                showSnackBar.value = true
            }
        }

        locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).apply {
                setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                location.value = LocationDetails(
                    p0.locations.last().latitude,
                    p0.locations.last().longitude
                )
                stopLocationUpdates()
                requestingLocationUpdates = false
            }
        }*/

        setContent {
            RunTogetherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ModalNavigationDrawerSample(location.value)
                    /*val snackbarHostState = remember { SnackbarHostState() }
                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        content = { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                //startLocationUpdates()


                                ModalNavigationDrawerSample(location.value)

                            }
                        }
                    )
                    if (showSnackBar.value) {
                        SnackBarComposable(snackbarHostState, this, showSnackBar)
                    }
                    if (showAlertDialog.value) {
                        AlertDialogComposable(this, showAlertDialog)
                    }*/
                }
            }
        }
        deviceLocationTracker= DeviceLocationTracker(this, this)
    }
    /*private fun startLocationUpdates() {
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
                    permission
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
    }*/

    override fun onDeviceLocationChanged(
        results: List<Address>?,
        resultLocationDetails: LocationDetails
    ) {
        location.value = resultLocationDetails
    }
}
