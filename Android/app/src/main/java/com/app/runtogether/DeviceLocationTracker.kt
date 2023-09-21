package com.app.runtogether

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*
import kotlin.coroutines.CoroutineContext

class DeviceLocationTracker(context: Context, deviceLocationListener: DeviceLocationListener) : LocationListener, CoroutineScope {
    private var deviceLocation: Location? = null
    private val context: WeakReference<Context>
    private var locationManager: LocationManager? = null
    private var deviceLocationListener: DeviceLocationListener
    private val job = Job()






    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    init {
        this.context = WeakReference(context)
        this.deviceLocationListener = deviceLocationListener
        initializeLocationProviders()
    }
    private fun initializeLocationProviders() {
        //Init Location Manger if not already initialized
        if (null == locationManager) {
            locationManager = context.get()
                ?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        locationManager?.apply {
            // flag for GPS status
            val isGPSEnabled = isProviderEnabled(LocationManager.GPS_PROVIDER)
            // flag for network status
            val isNetworkEnabled = isProviderEnabled(LocationManager.PASSIVE_PROVIDER)
            //If we have permission
            if (ActivityCompat.checkSelfPermission(context.get()!!, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context.get()!!, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                //First Try GPS
                if (isGPSEnabled) {
                    requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        UPDATE_FREQUENCY_TIME,
                        UPDATE_FREQUENCY_DISTANCE.toFloat(), this@DeviceLocationTracker)
                    deviceLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } else {
                    //MainActivity().showSnackBar.value = true
                    // Show alert to open GPS
                    /*context.get()?.apply {
                        AlertDialog.Builder(this)
                            .setTitle(getString(R.string.title_enable_gps))
                            .setMessage(getString(R.string.desc_enable_gps))
                            .setPositiveButton(getString(R.string.btn_settings)
                            ) { dialog, which ->
                                val intent = Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                startActivity(intent)
                            }.setNegativeButton(getString(R.string.btn_cancel))
                            { dialog, which -> dialog.cancel() }.show()
                    }*/
                }
                //If failed try using NetworkManger
                if(null==deviceLocation && isNetworkEnabled) {
                    requestLocationUpdates(
                        LocationManager.PASSIVE_PROVIDER,
                        0, 0f,
                        this@DeviceLocationTracker)
                    deviceLocation = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
            }
        }
    }
    /**
     * Stop using GPS listener
     * Must call this function to stop using GPS
     */
    fun stopUpdate() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@DeviceLocationTracker)
        }
    }
    override fun onLocationChanged(newDeviceLocation: Location) {
        deviceLocation = newDeviceLocation
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                var addressList: List<Address?>? = null

                val location = LocationDetails(newDeviceLocation.latitude,
                    newDeviceLocation.longitude)

                try {
                    addressList = context.get()?.let {
                        Geocoder(
                            it,
                            Locale.ENGLISH).getFromLocation(
                            newDeviceLocation.latitude,
                            newDeviceLocation.longitude,
                            1)
                    }
                    deviceLocationListener.onDeviceLocationChanged(addressList, location)
                    Log.i(TAG, "Fetch address list"+addressList)
                } catch (e: IOException) {
                    Log.e(TAG, "Failed Fetched Address List")
                }
            }
        }
    }
    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    interface DeviceLocationListener {
        fun onDeviceLocationChanged(resultsAddress: List<Address>?, resultLocationDetails: LocationDetails)
    }
    companion object {
        // The minimum distance to change Updates in meters
        private const val UPDATE_FREQUENCY_DISTANCE: Long = 1 // 10 meters
        // The minimum time between updates in milliseconds
        private const val UPDATE_FREQUENCY_TIME: Long = 1 // 1 minute
        private val TAG = DeviceLocationTracker::class.java.simpleName
    }
}