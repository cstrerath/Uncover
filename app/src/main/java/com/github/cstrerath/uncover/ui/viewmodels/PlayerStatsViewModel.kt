package com.github.cstrerath.uncover.ui.viewmodels

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.domain.character.progression.CharacterProgression
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.ui.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerStatsViewModel(
    private val characterRepository: CharacterRepository,
    private val characterProgression: CharacterProgression
) {
    private var _player = mutableStateOf<GameCharacter?>(null)
    val player = _player

    private var _dialogState = mutableStateOf(DialogState())
    val dialogState = _dialogState

    suspend fun loadPlayer() {
        _player.value = characterRepository.getPlayer()
    }

    suspend fun tryLevelUp() {
        val currentPlayer = _player.value ?: return
        val remainingXp = characterProgression.getRemainingXp(
            currentPlayer.level,
            currentPlayer.experience
        )

        if (remainingXp <= 0) {
            characterProgression.tryLevelUp()
            _dialogState.value = DialogState(
                show = true,
                message = "Levelaufstieg erfolgreich! Du bist nun Level ${currentPlayer.level + 1}"
            )
        } else {
            _dialogState.value = DialogState(
                show = true,
                message = "Levelaufstieg nicht möglich! Dir fehlen noch $remainingXp XP bis zum nächsten Level."
            )
        }
        delay(100)
        loadPlayer()
    }

    suspend fun addTestXp() {
        characterProgression.addTestXp()
        _dialogState.value = DialogState(
            show = true,
            message = "250 XP hinzugefügt!"
        )
        delay(100)
        loadPlayer()
    }

    fun dismissDialog() {
        _dialogState.value = DialogState(show = false)
    }
}

data class DialogState(
    val show: Boolean = false,
    val message: String = ""
)
