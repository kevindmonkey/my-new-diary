package ph.edu.cksc.college.appdev.mydiary.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext

enum class ThemeType {
    DEFAULT, DARK, CYBER, SAKURA, FOREST, CUSTOM
}

private val DarkColorScheme = darkColorScheme(
    primary = LightBlue80,
    secondary = LightBlueGrey80,
    tertiary = LightBlueAccent80
)

private val LightColorScheme = lightColorScheme(
    primary = LightBlue40,
    secondary = LightBlueGrey40,
    tertiary = LightBlueAccent40
)

private val CyberColorScheme = darkColorScheme(
    primary = YellowPrimary,
    secondary = YellowSecondary,
    background = BlackBackground,
    surface = BlackSurface,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color.White.copy(alpha = 0.7f),
    outline = Color.White.copy(alpha = 0.5f)
)

private val SakuraColorScheme = lightColorScheme(
    primary = PinkSecondary,
    secondary = PinkPrimary,
    background = PinkBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    surfaceVariant = Color(0xFFFFF8FA),
    onSurfaceVariant = Color.Black.copy(alpha = 0.7f),
    outline = Color.Black.copy(alpha = 0.3f)
)

private val ForestColorScheme = lightColorScheme(
    primary = GreenSecondary,
    secondary = GreenPrimary,
    background = GreenBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    surfaceVariant = Color(0xFFF1F8F2),
    onSurfaceVariant = Color.Black.copy(alpha = 0.7f),
    outline = Color.Black.copy(alpha = 0.3f)
)

@Composable
fun MyDiaryTheme(
    themeType: ThemeType = ThemeType.DEFAULT,
    customPrimary: Color = LightBlue40,
    customBackground: Color = Color.White,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeType) {
        ThemeType.DARK -> DarkColorScheme
        ThemeType.CYBER -> CyberColorScheme
        ThemeType.SAKURA -> SakuraColorScheme
        ThemeType.FOREST -> ForestColorScheme
        ThemeType.CUSTOM -> {
            val isLightBackground = customBackground.luminance() > 0.5
            val isLightPrimary = customPrimary.luminance() > 0.5
            val onBackground = if (isLightBackground) Color.Black else Color.White
            val onPrimary = if (isLightPrimary) Color.Black else Color.White
            
            if (isLightBackground) {
                lightColorScheme(
                    primary = customPrimary,
                    onPrimary = onPrimary,
                    background = customBackground,
                    onBackground = onBackground,
                    surface = customBackground,
                    onSurface = onBackground,
                    secondary = customPrimary.copy(alpha = 0.8f),
                    onSecondary = onPrimary,
                    primaryContainer = customPrimary.copy(alpha = 0.2f),
                    onPrimaryContainer = onBackground,
                    surfaceVariant = if (isLightBackground) Color.Black.copy(alpha = 0.05f) else Color.White.copy(alpha = 0.1f),
                    onSurfaceVariant = onBackground.copy(alpha = 0.7f),
                    outline = onBackground.copy(alpha = 0.4f)
                )
            } else {
                darkColorScheme(
                    primary = customPrimary,
                    onPrimary = onPrimary,
                    background = customBackground,
                    onBackground = onBackground,
                    surface = customBackground,
                    onSurface = onBackground,
                    secondary = customPrimary.copy(alpha = 0.8f),
                    onSecondary = onPrimary,
                    primaryContainer = customPrimary.copy(alpha = 0.2f),
                    onPrimaryContainer = onBackground,
                    surfaceVariant = Color.White.copy(alpha = 0.1f),
                    onSurfaceVariant = onBackground.copy(alpha = 0.7f),
                    outline = onBackground.copy(alpha = 0.4f)
                )
            }
        }
        ThemeType.DEFAULT -> {
            val context = LocalContext.current
            if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            } else {
                if (darkTheme) DarkColorScheme else LightColorScheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
