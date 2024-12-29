package com.github.cstrerath.uncover.domain.map.managers

import android.content.Context
import android.content.Intent
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

class QuestMarkerHandler(private val context: Context) {
    suspend fun loadPlayerAndQuestData(
        mapView: MapView?,
        questLauncher: ActivityResultLauncher<Intent>
    ) {
        val database = AppDatabase.getInstance(context)
        val player = database.gameCharacterDao().getPlayerCharacter() ?: return

        val progressManager = QuestProgressHandler(
            database.characterQuestProgressDao(),
            database.questDao(),
            XpManager(CharacterRepository(context))
        )

        val randQuestProgressHandler = RandQuestProgressHandler(
            context,
            database.characterQuestProgressDao(),
            database.randomQuestDatabaseDao(),
            XpManager(CharacterRepository(context))
        )

        val activeMainLocationIds = progressManager.getActiveQuestLocations(player.id)
        val activeRandLocationId = randQuestProgressHandler.getActiveRandQuestLocation(player.id)

        val activeLocationIds = activeMainLocationIds.toMutableList().apply {
            activeRandLocationId?.let { add(it) }
        }

        mapView?.let { map ->
            handleQuestMarkers(
                ids = activeLocationIds,
                mapView = map,
                locationOverlay = map.overlays.filterIsInstance<MyLocationNewOverlay>().firstOrNull()
                    ?: return,
                onMarkerClick = { locationId, isRandomQuest ->
                    createAndLaunchQuestIntent(questLauncher, locationId, isRandomQuest)
                }
            )
        }

    }

    private fun createAndLaunchQuestIntent(
        questLauncher: ActivityResultLauncher<Intent>,
        locationId: Int,
        isRandomQuest: Boolean
    ) {
        val intent = if (isRandomQuest) {
            Intent(context, QuestActivity::class.java).apply {
                putExtra(context.getString(R.string.quest_location_id), locationId)
            }
        } else {
            Intent(context, QuestActivity::class.java).apply {
                putExtra(context.getString(R.string.quest_location_id), locationId)
            }
        }
        questLauncher.launch(intent)
    }


    private suspend fun handleQuestMarkers(
        ids: List<Int>,
        mapView: MapView,
        locationOverlay: MyLocationNewOverlay,
        onMarkerClick: (Int,Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            removeExistingMarkers(mapView)
            addNewMarkers(ids, mapView, locationOverlay, onMarkerClick)
            refreshMap(mapView)
        }
    }

    private suspend fun removeExistingMarkers(mapView: MapView) {
        withContext(Dispatchers.Main) {
            mapView.overlays.removeAll { it is QuestMarkerOverlay }
        }
    }

    private suspend fun addNewMarkers(
        ids: List<Int>,
        mapView: MapView,
        locationOverlay: MyLocationNewOverlay,
        onMarkerClick: (Int,Boolean) -> Unit
    ) {
        val database = AppDatabase.getInstance(context)
        val locationDao = database.locationDao()

        ids.forEach { locationId ->
            val location = locationDao.getLocation(locationId) ?: return@forEach
            addMarkerToMap(location, mapView, locationOverlay, onMarkerClick)
        }
    }

    private suspend fun addMarkerToMap(
        location: Location,
        mapView: MapView,
        locationOverlay: MyLocationNewOverlay,
        onMarkerClick: (Int,Boolean) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            val marker = QuestMarkerOverlay(
                location = location,
                latitude = location.latitude,
                longitude = location.longitude,
                playerLocationProvider = { locationOverlay.myLocation },
                onMarkerClick =  onMarkerClick ,
                context = context
            )
            mapView.overlays.add(marker)
        }
    }

    private suspend fun refreshMap(mapView: MapView) {
        withContext(Dispatchers.Main) {
            mapView.invalidate()
        }
    }
}
