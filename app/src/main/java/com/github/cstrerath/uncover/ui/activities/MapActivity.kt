package com.github.cstrerath.uncover.ui.activities
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.UncoverBaseScreen
import com.github.cstrerath.uncover.ui.screens.map.MapScreen
import com.github.cstrerath.uncover.ui.screens.map.dialog.LocationServiceDialog
import com.github.cstrerath.uncover.ui.theme.UncoverTheme
import com.github.cstrerath.uncover.utils.location.LocationValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import com.github.cstrerath.uncover.ui.screens.map.dialog.NoInternetDialog
import com.github.cstrerath.uncover.ui.screens.map.dialog.NoLocationDialog
import com.github.cstrerath.uncover.ui.screens.map.dialog.OutOfBoundsDialog
import com.github.cstrerath.uncover.ui.screens.map.dialog.PermissionDialog


// app/src/main/java/com/example/uncover/ui/map/MapActivity.kt
class MapActivity : BaseActivity() {
    private lateinit var questLauncher: ActivityResultLauncher<Intent>
    private lateinit var locationValidator: LocationValidator
    private var internetCheckKey by mutableIntStateOf(0)
    private var locationCheckKey by mutableIntStateOf(0)
    private var hasLocationPermission by mutableStateOf(false)

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions.getOrDefault(
            Manifest.permission.ACCESS_FINE_LOCATION,
            false
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
        setContent {
            UncoverTheme {
                UncoverBaseScreen(0.dp) {
                    MapScreen(questLauncher)
                    LocationValidationDialogs()
                }
            }
        }
        checkLocationPermissions()
    }

    private fun initializeComponents() {
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))
        locationValidator = LocationValidator(this)
        questLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { _ ->
            finish()
            startActivity(intent)
        }
    }

    @Composable
    private fun LocationValidationDialogs() {
        var internetAvailable by remember { mutableStateOf(true) }
        var isLocationEnabled by remember { mutableStateOf(true) }
        var isOutOfBounds by remember { mutableStateOf(false) }
        var noLocationAvailable by remember { mutableStateOf(false) }

        var showOutOfBoundsDialog by remember { mutableStateOf(true) }
        var showLocationServiceDialog by remember { mutableStateOf(true) }
        var showNoLocationDialog by remember { mutableStateOf(true) }
        var showNoPermissionDialog by remember { mutableStateOf(true) }

        CheckLocationStatus(
            internetAvailable = { internetAvailable = it },
            locationEnabled = { isLocationEnabled = it },
            locationAvailable = { noLocationAvailable = it },
            outOfBounds = { isOutOfBounds = it }
        )

        when {
            !internetAvailable -> {
                NoInternetDialog (
                    onDismiss = { internetAvailable = true }
                )
            }
            (!hasLocationPermission && showNoPermissionDialog) -> {
                PermissionDialog(
                    onDismiss = { showNoPermissionDialog = false }
                )
            }
            (!isLocationEnabled && showLocationServiceDialog) -> {
                LocationServiceDialog(
                    onDismiss = { showLocationServiceDialog = false }
                )
            }
            (noLocationAvailable && showNoLocationDialog) -> {
                NoLocationDialog(
                    onDismiss = { showNoLocationDialog = false }
                )
            }
            (isOutOfBounds && showOutOfBoundsDialog) -> {
                OutOfBoundsDialog(
                    onDismiss = { showOutOfBoundsDialog = false }
                )
            }
        }
    }




    @Composable
    private fun CheckLocationStatus(
        internetAvailable: (Boolean) -> Unit,
        locationEnabled: (Boolean) -> Unit,
        locationAvailable: (Boolean) -> Unit,
        outOfBounds: (Boolean) -> Unit
    ) {
        LaunchedEffect(internetCheckKey) {
            withContext(Dispatchers.IO) {
                internetAvailable(locationValidator.isInternetAvailable())
            }
        }

        LaunchedEffect(locationCheckKey) {
            val isEnabled = locationValidator.isLocationEnabled()
            locationEnabled(isEnabled)

            val currentLocation = locationValidator.getCurrentLocation()
            if (currentLocation == null && hasLocationPermission && isEnabled) {
                locationAvailable(true)
            } else {
                currentLocation?.let { location ->
                    outOfBounds(!locationValidator.isLocationInBounds(location))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        internetCheckKey++
        locationCheckKey++
    }

    private fun checkLocationPermissions() {
        when {
            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED -> {
                hasLocationPermission = true
            }
            else -> requestLocationPermissions()
        }
    }

    private fun requestLocationPermissions() {
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }
}

