package com.example.rickandmorty.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterStatus
import com.example.rickandmorty.ui.theme.PortalGreen
import com.example.rickandmorty.ui.theme.StatusAlive
import com.example.rickandmorty.ui.theme.StatusDead
import com.example.rickandmorty.ui.theme.StatusUnknown
import com.rickmorty.presentation.common.event.displayName

@Composable
fun CharacterCard(
    character: Character,
    isFavorite: Boolean,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onItemClick,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .size(72.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f),
                ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                StatusBadge(
                    status = character.status
                )
                Spacer(Modifier.height(4.dp))
                
                Text(
                    text = 
                        "${character.species} • ${character.locationName}"
                    ,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                onClick = onFavoriteClick,
                
            ) {
                Icon(
                    imageVector = if (isFavorite) {
                        Icons.Default.Favorite
                    }else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = null,
                    tint = if (isFavorite) {
                        PortalGreen
                    }else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            
        }
    }
}

@Composable
fun StatusBadge(status: CharacterStatus) {
    val color = when (status) {
        CharacterStatus.ALIVE -> StatusAlive
        CharacterStatus.DEAD -> StatusDead
        CharacterStatus.UNKNOWN -> StatusUnknown
    }
    Text(
        text = status.displayName(),
        style = MaterialTheme.typography.labelLarge,
        color = color,
    )
}