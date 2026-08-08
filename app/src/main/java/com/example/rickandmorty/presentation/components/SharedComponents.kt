package com.example.rickandmorty.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rickandmorty.domain.model.Character
import com.rickmorty.presentation.common.event.displayName

@Composable
fun CharacterDetailContent(
    character: Character
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = character.imageUrl,
            contentDescription = character.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        ) 
        DetailRow(label = "Status", value = character.status.displayName())
        DetailRow(label = "Species", value = character.species)
        DetailRow(label = "Type", value = character.type)
        DetailRow(label = "Gender", value = character.gender)
        DetailRow(label = "Origin", value = character.originName)
        DetailRow(label = "Location", value = character.locationName)
        DetailRow(label = "Episodes", value = character.episodeCount.toString())
    }
}

@Composable
private fun DetailRow (
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(88.dp)
        )
        Text(
            text = "$value",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}