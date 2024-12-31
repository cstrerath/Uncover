package com.github.cstrerath.uncover.ui.screens.welcome

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R

private const val TAG = "WelcomeTextScreen"

@Composable
fun WelcomeTextScreen(
    modifier: Modifier = Modifier
) {
    Log.d(TAG, "Initializing Welcome Text Screen")
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WelcomeTitle()
        QuestTypesCard()
        ExperienceText()
        InfoSection()
        PrivacySection()
    }
}

@Composable
private fun WelcomeTitle() {
    Text(
        text = stringResource(R.string.welcome_text_title),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun QuestTypesCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceDim
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            QuestTypeRow(
                imageRes = R.drawable.quest_marker_image,
                descriptionRes = R.string.welcome_text_mainquest,
                textRes = R.string.welcome_text_mainquests
            )
            QuestTypeRow(
                imageRes = R.drawable.rand_quest_marker_image,
                descriptionRes = R.string.welcome_text_randquest,
                textRes = R.string.welcome_text_randquests,
                isLast = true
            )
        }
    }
}

@Composable
private fun QuestTypeRow(
    imageRes: Int,
    descriptionRes: Int,
    textRes: Int,
    isLast: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            top = 4.dp,
            bottom = if (isLast) 4.dp else 0.dp
        )
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = stringResource(descriptionRes),
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}


@Composable
private fun ExperienceText() {
    Text(
        text = stringResource(R.string.welcome_text_experience),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun InfoSection() {
    Text(
        text = stringResource(R.string.welcome_text_info),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Column(modifier = Modifier.padding(start = 8.dp)) {
        listOf(
            R.string.welcome_text_internet,
            R.string.welcome_text_location,
            R.string.welcome_text_fog_of_war
        ).forEach { textRes ->
            Text(
                text = stringResource(textRes),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun PrivacySection() {
    Text(
        text = stringResource(R.string.welcome_text_data_privacy),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 16.dp)
    )
    Text(
        text = stringResource(R.string.welcome_text_disclaimer),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}
