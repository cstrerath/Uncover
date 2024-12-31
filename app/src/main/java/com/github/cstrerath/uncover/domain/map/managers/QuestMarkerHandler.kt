package com.github.cstrerath.uncover.domain.map.managers

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.Location
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.progression.XpManager
import com.github.cstrerath.uncover.domain.map.overlays.QuestMarkerOverlay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import com.github.cstrerath.uncover.domain.quest.mainquest.QuestProgressHandler
import com.github.cstrerath.uncover.domain.quest.randquest.RandQuestProgressHandler
import com.github.cstrerath.uncover.ui.activities.QuestActivity
import com.github.cstrerath.uncover.ui.activities.RandQuestActivity

class QuestMarkerHandler(private val context: Context) {
    private val tag = "QuestMarkerHandler"
    private val database = AppDatabase.getInstance(context)

    suspend fun loadPlayerAndQuestData(
        mapView: MapView?,
        questLauncher: ActivityResultLauncher<Intent>
    ) {
        Log.d(tag, "Loading player and quest data")
        try {
            val player = database.gameCharacterDao().getPlayerCharacter() ?: run {
                Log.w(tag, "No player character found")
                return
            }

            val progressManager = createProgressManager()
            val randQuestProgressHandler = createRandQuestProgressHandler()

            val activeLocationIds = getActiveLocations(
                player.id,
                progressManager,
                randQuestProgressHandler
            )

            mapView?.let { map ->
                val locationOverlay = findLocationOverlay(map) ?: run {
                    Log.e(tag, "Location overlay not found")
                    return
                }
                handleQuestMarkers(activeLocationIds, map, locationOverlay) { locationId, isRandomQuest ->
                    createAndLaunchQuestIntent(questLauncher, locationId, isRandomQuest)
                }
            } ?: Log.w(tag, "MapView is null")
        } catch (e: Exception) {
            Log.e(tag, "Error loading quest data: ${e.message}")
        }
    }

    private fun createProgressManager() = QuestProgressHandler(
        database.characterQuestProgressDao(),
        database.questDao(),
        XpManager(CharacterRepository(context))
    )

    private fun createRandQuestProgressHandler() = RandQuestProgressHandler(
        context,
        database.characterQuestProgressDao(),
        database.randomQuestDatabaseDao(),
        XpManager(CharacterRepository(context))
    )

    private suspend fun getActiveLocations(
        playerId: String,
        progressManager: QuestProgressHandler,
        randQuestProgressHandler: RandQuestProgressHandler
    ): List<Int> {
        Log.d(tag, "Getting active locations for player: $playerId")
        val mainLocations = progressManager.getActiveQuestLocations(playerId)
        val randLocation = randQuestProgressHandler.getActiveRandQuestLocation(playerId)

        return mainLocations.toMutableList().apply {
            randLocation?.let { add(it) }
        }.also {
            Log.d(tag, "Found ${it.size} active locations")
        }
    }

    private fun findLocationOverlay(mapView: MapView): MyLocationNewOverlay? =
        mapView.overlays.filterIsInstance<MyLocationNewOverlay>().firstOrNull()

    private fun createAndLaunchQuestIntent(
        questLauncher: ActivityResultLauncher<Intent>,
        locationId: Int,
        isRandomQuest: Boolean
    ) {
        Log.d(tag, "Creating intent for location $locationId (Random: $isRandomQuest)")
        val intent = if (isRandomQuest) {
            Intent(context, RandQuestActivity::class.java)
        } else {
            Intent(context, QuestActivity::class.java)
        }.apply {
            putExtra(context.getString(R.string.quest_location_id), locationId)
        }
        questLauncher.launch(intent)
    }

    private suspend fun handleQuestMarkers(
        ids: List<Int>,
        mapView: MapView,
        locationOverlay: MyLocationNewOverlay,
        onMarkerClick: (Int, Boolean) -> Unit
    ) {
        Log.d(tag, "Handling ${ids.size} quest markers")
        withContext(Dispatchers.IO) {
            removeExistingMarkers(mapView)
            addNewMarkers(ids, mapView, locationOverlay, onMarkerClick)
            refreshMap(mapView)
        }
    }

    private suspend fun removeExistingMarkers(mapView: MapView) {
        Log.d(tag, "Removing existing markers")
        withContext(Dispatchers.Main) {
            val removedCount = mapView.overlays.count { it is QuestMarkerOverlay }
            mapView.overlays.removeAll { it is QuestMarkerOverlay }
            Log.d(tag, "Removed $removedCount markers")
        }
    }

    private suspend fun addNewMarkers(
        ids: List<Int>,
        mapView: MapView,
        locationOverlay: MyLocationNewOverlay,
        onMarkerClick: (Int, Boolean) -> Unit
    ) {
        Log.d(tag, "Adding new markers")
        val locationDao = database.locationDao()
        var addedCount = 0

        ids.forEach { locationId ->
            locationDao.getLocation(locationId)?.let { location ->
                addMarkerToMap(location, mapView, locationOverlay, onMarkerClick)
                addedCount++
            } ?: Log.w(tag, "Location not found for ID: $locationId")
        }
        Log.d(tag, "Added $addedCount markers")
    }

    private suspend fun addMarkerToMap(
        location: Location,
        mapView: MapView,
        locationOverlay: MyLocationNewOverlay,
        onMarkerClick: (Int, Boolean) -> Unit
    ) {
        Log.v(tag, "Adding marker for location ${location.id}")
        withContext(Dispatchers.Main) {
            val marker = QuestMarkerOverlay(
                location = location,
                latitude = location.latitude,
                longitude = location.longitude,
                playerLocationProvider = { locationOverlay.myLocation },
                onMarkerClick = onMarkerClick,
                context = context
            )
            mapView.overlays.add(marker)
        }
    }

    private suspend fun refreshMap(mapView: MapView) {
        Log.v(tag, "Refreshing map view")
        withContext(Dispatchers.Main) {
            mapView.invalidate()
        }
    }
}

