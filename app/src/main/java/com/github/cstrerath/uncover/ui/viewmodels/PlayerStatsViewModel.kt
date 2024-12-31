package com.github.cstrerath.uncover.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.progression.CharacterProgression
import kotlinx.coroutines.delay

class PlayerStatsViewModel(
    private val context: Context,
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

        if (currentPlayer.level >= 25) {
            _dialogState.value = DialogState(
                show = true,
                message = context.getString(R.string.level_up_max_reached)
            )
            return
        }

        val remainingXp = characterProgression.getRemainingXp(
            currentPlayer.level,
            currentPlayer.experience
        )

        if (remainingXp <= 0) {
            characterProgression.tryLevelUp()
            _dialogState.value = DialogState(
                show = true,
                message = context.getString(R.string.level_up_sucessful, currentPlayer.level + 1)
            )
        } else {
            _dialogState.value = DialogState(
                show = true,
                message = context.getString(R.string.level_up_failed, remainingXp)
            )
        }
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
