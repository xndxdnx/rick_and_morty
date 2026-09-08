package com.example.rickandmorty.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val RickMortyDarkColorScheme = darkColorScheme(
    primary = PortalGreen,
    onPrimary = SpaceDark,
    primaryContainer = PortalGreenDark,
    onPrimaryContainer = AcidGreen,
    secondary = RickBlue,
    onSecondary = SpaceDark,
    secondaryContainer = RickBlueDark,
    onSecondaryContainer = RickBlue,
    tertiary = MortyYellow,
    onTertiary = SpaceDark,
    background = SpaceDark,
    onBackground = Color(0xFFE8EDF5),
    surface = SpaceDarkSurface,
    onSurface = Color(0xFFE8EDF5),
    surfaceVariant = SpaceDarkElevated,
    onSurfaceVariant = Color(0xFFB8C4D9),
    outline = PortalGreenDark,
)

private val RickMortyLightColorScheme = lightColorScheme(
    primary = PortalGreenDark,
    onPrimary = Color.White,
    primaryContainer = AcidGreen,
    onPrimaryContainer = SpaceDark,
    secondary = RickBlueDark,
    onSecondary = Color.White,
    secondaryContainer = RickBlue,
    onSecondaryContainer = SpaceDark,
    tertiary = MortyYellow,
    onTertiary = SpaceDark,
    background = Color(0xFFF4F7FB),
    onBackground = SpaceDark,
    surface = Color.White,
    onSurface = SpaceDark,
    surfaceVariant = Color(0xFFE8EDF5),
    onSurfaceVariant = Color(0xFF4A5568),
    outline = PortalGreenDark,
)

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */

@Composable
fun RickAndMortyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) RickMortyDarkColorScheme
    else RickMortyLightColorScheme
    
    // для получения композиции контента из android 
    val view = LocalView.current
    
    // проверка интерфейса на режим редактирования
    if (!view.isInEditMode) {
        // побочные эффекты Composable после отрисовки для подстраховки 
        SideEffect {  
            // объект window предоставляет контекст одной Activity 
            val window = (view.context as Activity).window
            
            // преобразует Compose цвет в формат андроид
            window.statusBarColor = colorScheme.background.toArgb()

            // настраиваем внешний вид статус бара
            // WindowCompat помогает взаимодействовать с андроид относительно версий
            
            WindowCompat.getInsetsController(
                window,
                view
            ).isAppearanceLightStatusBars = !darkTheme
        }
    }
    
    
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
    
    
    
}
