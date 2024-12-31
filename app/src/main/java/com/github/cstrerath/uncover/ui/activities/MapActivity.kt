package com.github.cstrerath.uncover.ui.activities
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.UncoverBaseScreen
import com.github.cstrerath.uncover.ui.screens.map.MapScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.util.BoundingBox
import java.net.HttpURLConnection
import java.net.URL


class MapActivity : BaseActivity() {
    private lateinit var questLauncher: ActivityResultLauncher<Intent>
    private var internetCheckKey by mutableIntStateOf(0)
    private var locationCheckKey by mutableIntStateOf(0)
    private var hasLocationPermission by mutableStateOf(false)


    private fun isInternetAvailable(): Boolean {
        return try {
            val url = URL("https://www.google.com")
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.connect()
            connection.responseCode == 200
        } catch (e: Exception) {
            false
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun createBoundingBox() = BoundingBox(
        49.56415, 8.55226,
        49.42672, 8.38477
    )

    private fun isLocationInBounds(location: android.location.Location): Boolean {
        val bounds = createBoundingBox()
        return location.latitude <= bounds.latNorth &&
                location.latitude >= bounds.latSouth &&
                location.longitude >= bounds.lonWest &&
                location.longitude <= bounds.lonEast
    }

    private fun getCurrentLocation(): android.location.Location? {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return try {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))

        questLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { _ ->
            finish()
            startActivity(intent)
        }

        setContent {
            UncoverTheme {
                UncoverBaseScreen(0.dp) {
                    MapScreen(questLauncher)

                    var internetAvailable by remember { mutableStateOf(true) }
                    var isLocationEnabled by remember { mutableStateOf(true) }
                    var isOutOfBounds by remember { mutableStateOf(false) }
                    var showOutOfBoundsDialog by remember { mutableStateOf(true) }
                    var showLocationServiceDialog by remember { mutableStateOf(true) }
                    var noLocationAvailable by remember { mutableStateOf(false) }
                    var showNoLocationDialog by remember { mutableStateOf(true) }
                    var showNoPermissionDialog by remember { mutableStateOf(true) }

                    LaunchedEffect(internetCheckKey) {
                        withContext(Dispatchers.IO) {
                            internetAvailable = isInternetAvailable()
                        }
                    }
                    LaunchedEffect(locationCheckKey) {
                        isLocationEnabled = isLocationEnabled()
                        val currentLocation = getCurrentLocation()
                        if (currentLocation == null && hasLocationPermission && isLocationEnabled) {
                            noLocationAvailable = true
                        } else {
                            currentLocation?.let { location ->
                                isOutOfBounds = !isLocationInBounds(location)
                            }
                        }
                    }


                    when {
                        !internetAvailable -> {
                            AlertDialog(
                                onDismissRequest = { },
                                title = { Text(stringResource(R.string.map_no_internet)) },
                                text = { Text(stringResource(R.string.map_no_internet_info)) },
                                confirmButton = {
                                    Button(onClick = {
                                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                                        internetCheckKey++
                                    }) {
                                        Text(stringResource(R.string.map_no_internet_settings))
                                    }
                                },
                                dismissButton = {
                                    Button(onClick = { internetAvailable = true }) {
                                        Text(stringResource(R.string.map_continue_anyway))
                                    }
                                }
                            )
                        }
                        (!hasLocationPermission && showNoPermissionDialog) -> {
                            AlertDialog(
                                onDismissRequest = { showNoPermissionDialog = false },
                                title = { Text(stringResource(R.string.map_no_location)) },
                                text = { Text(stringResource(R.string.map_no_location_info)) },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                                data = Uri.fromParts("package", packageName, null)
                                            })
                                        }
                                    ) {
                                        Text(stringResource(R.string.map_no_location_settings))
                                    }
                                },
                                dismissButton = {
                                    Button(onClick = { showNoPermissionDialog = false }) {
                                        Text(stringResource(R.string.map_no_location_dismiss))
                                    }
                                }
                            )
                        }
                        (!isLocationEnabled() && showLocationServiceDialog) -> {
                            AlertDialog(
                                onDismissRequest = { showLocationServiceDialog = false },
                                title = { Text(stringResource(R.string.map_location_disabled)) },
                                text = { Text(stringResource(R.string.map_location_disabled_info)) },
                                confirmButton = {
                                    Button(onClick = { startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }) {
                                        Text(stringResource(R.string.map_location_settings))
                                    }
                                },
                                dismissButton = {
                                    Button(onClick = { showLocationServiceDialog = false }) {
                                        Text(stringResource(R.string.map_continue_anyway))
                                    }
                                }
                            )
                        }
                        (noLocationAvailable && showNoLocationDialog) -> {
                            AlertDialog(
                                onDismissRequest = { showNoLocationDialog = false },
                                title = { Text(stringResource(R.string.map_no_location_signal)) },
                                text = { Text(stringResource(R.string.map_no_location_signal_info)) },
                                confirmButton = {
                                    Button(onClick = { showNoLocationDialog = false }) {
                                        Text(stringResource(R.string.map_continue_anyway))
                                    }
                                }
                            )
                        }
                        (isOutOfBounds && showOutOfBoundsDialog) -> {
                            AlertDialog(
                                onDismissRequest = { showOutOfBoundsDialog = false},
                                title = { Text(stringResource(R.string.map_outside_bounds)) },
                                text = { Text(stringResource(R.string.map_outside_bounds_info)) },
                                confirmButton = {
                                    Button(onClick = { showOutOfBoundsDialog = false }) {
                                        Text(stringResource(R.string.map_continue_anyway))
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        checkLocationPermissions()
    }

    override fun onResume() {
        super.onResume()
        internetCheckKey++
        locationCheckKey++
    }

    private fun checkLocationPermissions() {
        when {
            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                hasLocationPermission = true
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                requestLocationPermissions()
            }
            else -> {
                requestLocationPermissions()
            }
        }
    }

    private fun requestLocationPermissions() {
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }
}
