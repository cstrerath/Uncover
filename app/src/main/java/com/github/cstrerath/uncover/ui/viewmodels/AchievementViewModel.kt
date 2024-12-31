package com.github.cstrerath.uncover.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.Achievement
import com.github.cstrerath.uncover.domain.achievement.AchievementManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AchievementViewModel(application: Application) : AndroidViewModel(application) {
    private val achievementDao = AppDatabase.getInstance(application).achievementDao()
    private val achievementManager = AchievementManager(application)
    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements = _achievements.asStateFlow()

    companion object {
        private const val TAG = "AchievementViewModel"
    }

    init {
        Log.d(TAG, "Initializing AchievementViewModel")
        loadAchievements()
    }

    private fun loadAchievements() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "Checking and updating achievements")
                achievementManager.checkAndUpdateAchievements()

                Log.d(TAG, "Loading achievements from database")
                _achievements.value = achievementDao.getAllAchievements()
                    .sortedBy { it.id }
                    .also { Log.d(TAG, "Loaded ${it.size} achievements") }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading achievements", e)
                _achievements.value = emptyList()
            }
        }
    }
}
