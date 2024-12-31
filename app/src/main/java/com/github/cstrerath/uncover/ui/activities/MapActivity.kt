package com.github.cstrerath.uncover.ui.activities
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.UncoverBaseScreen
import com.github.cstrerath.uncover.ui.screens.map.LocationValidationScreen
import com.github.cstrerath.uncover.ui.screens.map.MapScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme
import com.github.cstrerath.uncover.utils.location.LocationValidator
import org.osmdroid.config.Configuration

class MapActivity : BaseActivity() {
    private lateinit var questLauncher: ActivityResultLauncher<Intent>
    private lateinit var locationValidator: LocationValidator
    private var internetCheckKey by mutableIntStateOf(0)
    private var locationCheckKey by mutableIntStateOf(0)
    private var hasLocationPermission by mutableStateOf(false)
    private var isPermissionCheckComplete by mutableStateOf(false)

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions.getOrDefault(
            Manifest.permission.ACCESS_FINE_LOCATION,
            false
        )
        isPermissionCheckComplete = true
        Log.d(TAG, "Location permission granted: $hasLocationPermission")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Creating Map Activity")
        initializeComponents()
        setContent {
            UncoverTheme {
                if (isPermissionCheckComplete) {
                    UncoverBaseScreen(0.dp) {
                        MapScreen(questLauncher)
                        LocationValidationScreen(
                            locationValidator = locationValidator,
                            hasLocationPermission = hasLocationPermission,
                            internetCheckKey = internetCheckKey,
                            locationCheckKey = locationCheckKey
                        )
                    }
                }
            }
        }
        checkLocationPermissions()
    }

    private fun checkLocationPermissions() {
        when {
            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED -> {
                hasLocationPermission = true
                isPermissionCheckComplete = true
                Log.d(TAG, "Location permission already granted")
            }
            else -> {
                Log.d(TAG, "Requesting location permissions")
                requestLocationPermissions()
            }
        }
    }

    private fun initializeComponents() {
        Log.d(TAG, "Initializing components")
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))
        locationValidator = LocationValidator(this)
        initializeQuestLauncher()
    }

    private fun initializeQuestLauncher() {
        questLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { _ -> restartActivity() }
    }

    private fun restartActivity() {
        Log.d(TAG, "Restarting Map Activity")
        finish()
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Resuming Map Activity")
        refreshLocationChecks()
    }

    private fun refreshLocationChecks() {
        internetCheckKey++
        locationCheckKey++
    }


    private fun requestLocationPermissions() {
        Log.d(TAG, "Requesting location permissions")
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    companion object {
        private const val TAG = "MapActivity"
    }
}
