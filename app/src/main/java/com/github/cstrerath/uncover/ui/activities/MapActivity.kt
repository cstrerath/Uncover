package com.github.cstrerath.uncover.ui.activities
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.github.cstrerath.uncover.ui.screens.map.MapScreen
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.UncoverBaseScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme
import org.osmdroid.config.Configuration

class MapActivity : BaseActivity() {
    private lateinit var questLauncher: ActivityResultLauncher<Intent>

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                setContent {
                    UncoverTheme {
                        UncoverBaseScreen {
                            MapScreen(questLauncher)
                        }
                    }
                }
            }
            else -> {
                finish()
            }
        }
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

        checkLocationPermissions()
    }

    private fun checkLocationPermissions() {
        when {
            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED -> {
                setContent {
                    UncoverTheme {
                        UncoverBaseScreen {
                            MapScreen(questLauncher)
                        }
                    }
                }
            }
            shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
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
