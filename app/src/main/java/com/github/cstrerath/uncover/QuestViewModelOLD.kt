package com.github.cstrerath.uncover

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.Quest
import com.github.cstrerath.uncover.data.database.entities.QuestStep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestViewModelOLD(application: Application) : AndroidViewModel(application) {
    private val _quests = MutableStateFlow<List<Quest>>(emptyList())
    val quests = _quests.asStateFlow()

    private val _questSteps = MutableStateFlow<List<QuestStep>>(emptyList())
    val questSteps = _questSteps.asStateFlow()

    fun loadQuestsForCharacter(questId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val database = AppDatabase.getInstance(getApplication())
            _quests.value = database.questDao().getAllQuests()
            _questSteps.value = database.questStepDao().getStepsForQuest(questId)
        }
    }
}