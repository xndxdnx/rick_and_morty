package com.example.rickandmorty.presentation.components

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
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

@Composable
fun CharacterAvatar(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appContext = remember { context.applicationContext }
    
    // Это указывает какой конкретно лоадер использовать для выполнения запроса
    val imageLoader = context.imageLoader
    
    val request = remember (imageUrl) { 
        ImageRequest.Builder(appContext)
            .data(imageUrl)
            .diskCacheKey(imageUrl)
            .memoryCacheKey(imageUrl)
            .allowHardware(false)
            // Кеширование в оперативке и на диске и при повторном запросе будет браться из ram 
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .build()
    }
    
    // Coil Composable - даёт больше контроля над процессом загрузки
    SubcomposeAsyncImage(
        model = request,
        imageLoader = imageLoader,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(color = MaterialTheme.colorScheme.surfaceVariant),
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) 
    
}