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
import com.github.cstrerath.uncover.ui.screens.map.LocationDialogStates
import com.github.cstrerath.uncover.ui.screens.map.LocationServiceDialog
import com.github.cstrerath.uncover.ui.screens.map.MapScreen
import com.github.cstrerath.uncover.ui.screens.map.NoInternetDialog
import com.github.cstrerath.uncover.ui.screens.map.NoLocationDialog
import com.github.cstrerath.uncover.ui.screens.map.OutOfBoundsDialog
import com.github.cstrerath.uncover.ui.screens.map.PermissionDialog
import com.github.cstrerath.uncover.ui.theme.UncoverTheme
import com.github.cstrerath.uncover.utils.location.LocationValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration

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
        setMapContent()
        checkLocationPermissions()
    }

    private fun initializeComponents() {
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))
        locationValidator = LocationValidator(this)
        initializeQuestLauncher()
    }

    private fun initializeQuestLauncher() {
        questLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { _ ->
            restartActivity()
        }
    }

    private fun restartActivity() {
        finish()
        startActivity(intent)
    }

    private fun setMapContent() {
        setContent {
            UncoverTheme {
                UncoverBaseScreen(0.dp) {
                    MapContent()
                }
            }
        }
    }

    @Composable
    private fun MapContent() {
        MapScreen(questLauncher)
        LocationValidationDialogs()
    }

    @Composable
    private fun LocationValidationDialogs() {
        val dialogStates = rememberLocationDialogStates()

        CheckLocationStatus(
            dialogStates = dialogStates,
            hasLocationPermission = hasLocationPermission
        )

        ShowLocationDialogs(dialogStates)
    }

    @Composable
    private fun rememberLocationDialogStates() = remember {
        LocationDialogStates(
            internetAvailable = mutableStateOf(true),
            isLocationEnabled = mutableStateOf(true),
            isOutOfBounds = mutableStateOf(false),
            noLocationAvailable = mutableStateOf(false),
            showOutOfBoundsDialog = mutableStateOf(true),
            showLocationServiceDialog = mutableStateOf(true),
            showNoLocationDialog = mutableStateOf(true),
            showNoPermissionDialog = mutableStateOf(true)
        )
    }

    @Composable
    private fun ShowLocationDialogs(states: LocationDialogStates) {
        when {
            !states.internetAvailable.value -> {
                NoInternetDialog(
                    onDismiss = { states.internetAvailable.value = true }
                )
            }
            (!hasLocationPermission && states.showNoPermissionDialog.value) -> {
                PermissionDialog(
                    onDismiss = { states.showNoPermissionDialog.value = false }
                )
            }
            (!states.isLocationEnabled.value && states.showLocationServiceDialog.value) -> {
                LocationServiceDialog(
                    onDismiss = { states.showLocationServiceDialog.value = false }
                )
            }
            (states.noLocationAvailable.value && states.showNoLocationDialog.value) -> {
                NoLocationDialog(
                    onDismiss = { states.showNoLocationDialog.value = false }
                )
            }
            (states.isOutOfBounds.value && states.showOutOfBoundsDialog.value) -> {
                OutOfBoundsDialog(
                    onDismiss = { states.showOutOfBoundsDialog.value = false }
                )
            }
        }
    }

    @Composable
    private fun CheckLocationStatus(
        dialogStates: LocationDialogStates,
        hasLocationPermission: Boolean
    ) {
        LaunchedEffect(internetCheckKey) {
            withContext(Dispatchers.IO) {
                dialogStates.internetAvailable.value = locationValidator.isInternetAvailable()
            }
        }

        LaunchedEffect(locationCheckKey) {
            checkLocationStatus(dialogStates, hasLocationPermission)
        }
    }

    private fun checkLocationStatus(
        dialogStates: LocationDialogStates,
        hasLocationPermission: Boolean
    ) {
        val isEnabled = locationValidator.isLocationEnabled()
        dialogStates.isLocationEnabled.value = isEnabled

        val currentLocation = locationValidator.getCurrentLocation()
        if (currentLocation == null && hasLocationPermission && isEnabled) {
            dialogStates.noLocationAvailable.value = true
        } else {
            currentLocation?.let { location ->
                dialogStates.isOutOfBounds.value = !locationValidator.isLocationInBounds(location)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshLocationChecks()
    }

    private fun refreshLocationChecks() {
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


