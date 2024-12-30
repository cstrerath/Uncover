package com.github.cstrerath.uncover.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R

@Composable
fun WelcomeTextScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Willkommen in Ludwigshafen und Mannheim!",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.quest_marker_image),
                contentDescription = "Hauptquest",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = " = Hauptquests",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.rand_quest_marker_image),
                contentDescription = "Nebenquest",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = " = Nebenquests",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Text(
            text = buildAnnotatedString {
                appendLine("Sammle Erfahrungspunkte durch das Abschließen von Quests und entwickle deinen Charakter weiter.")
                appendLine()
                appendLine("Wichtige Informationen:")
                appendLine("• Internetverbindung wird für das Laden der Karte benötigt")
                appendLine("• Standortfreigabe ermöglicht das Spielen von Quests in deiner Nähe")
                appendLine("• Quests werden in einem bestimmten Radius um deinen Standort freigeschaltet")
                appendLine()
                append("Datenschutz: Alle Spieldaten (Quests, Fortschritt, Charakterentwicklung) werden ausschließlich lokal auf deinem Gerät gespeichert. Kartendaten von OpenStreetMap (OSM) werden zur Reduzierung des Datenverbrauchs zwischengespeichert. Es werden keine persönlichen Standortdaten gespeichert.")
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
