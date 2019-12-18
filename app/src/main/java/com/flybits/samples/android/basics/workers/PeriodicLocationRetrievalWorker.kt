package com.flybits.samples.android.basics.workers

import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.flybits.context.plugins.location.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import java.util.concurrent.ExecutionException

class PeriodicLocationRetrievalWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters){

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun doWork(): Result {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        try {
            var isNetworkEnabled = false
            var isGpsEnabled = false
            val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE)

            if (locationManager is LocationManager) {

                isNetworkEnabled =
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                isGpsEnabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    locationManager.isLocationEnabled
                } else {
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                            || locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)
                }
            }

            if (isGpsEnabled || isNetworkEnabled) {
                // Using Tasks.await to get the result synchronously without callback.
                val lastLocation = Tasks.await(fusedLocationClient.lastLocation)
                if (lastLocation != null) {
                    Log.d("LocationReceiver", "Received new location: ${lastLocation.latitude}, ${lastLocation.longitude}")
                    LocationData(lastLocation)
                } else {
                    return Result.retry()
                }
            } else return Result.failure()
        } catch (e: ExecutionException) {
            //Something went wrong
        } catch (e: InterruptedException) {
            //Something went wrong
        }
        return Result.success()
    }
}