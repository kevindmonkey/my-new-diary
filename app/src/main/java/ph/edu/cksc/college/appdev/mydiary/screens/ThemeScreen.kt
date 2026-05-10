package ph.edu.cksc.college.appdev.mydiary.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ph.edu.cksc.college.appdev.mydiary.ui.theme.ThemeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeScreen(
    navController: NavHostController,
    currentTheme: ThemeType,
    customPrimary: Color,
    customBackground: Color,
    onThemeChange: (ThemeType) -> Unit,
    onCustomPrimaryChange: (Color) -> Unit,
    onCustomBackgroundChange: (Color) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("App Themes", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Preview Card
            ThemePreview(customPrimary, customBackground)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "Presets",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Quickly choose a pre-defined style",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            val presets = listOf(
                PresetOption("System", ThemeType.DEFAULT, Color.LightGray),
                PresetOption("Dark", ThemeType.DARK, Color(0xFF121212)),
                PresetOption("Cyber", ThemeType.CYBER, Color(0xFFFFEB3B)),
                PresetOption("Sakura", ThemeType.SAKURA, Color(0xFFF48FB1)),
                PresetOption("Forest", ThemeType.FOREST, Color(0xFF4CAF50))
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(presets) { preset ->
                    PresetCard(
                        preset = preset,
                        isSelected = currentTheme == preset.type,
                        onClick = { onThemeChange(preset.type) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Custom Theme",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Create your own color palette",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = currentTheme == ThemeType.CUSTOM,
                    onCheckedChange = { 
                        if (it) onThemeChange(ThemeType.CUSTOM) 
                        else onThemeChange(ThemeType.DEFAULT) 
                    }
                )
            }

            if (currentTheme == ThemeType.CUSTOM) {
                Spacer(modifier = Modifier.height(24.dp))
                
                Text("Primary Color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("The color used for buttons and icons", style = MaterialTheme.typography.bodySmall)
                ColorPickerGrid(
                    selectedColor = customPrimary,
                    onColorSelected = onCustomPrimaryChange
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text("Background Color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("The base color of your app screens", style = MaterialTheme.typography.bodySmall)
                ColorPickerGrid(
                    selectedColor = customBackground,
                    onColorSelected = onCustomBackgroundChange
                )
                
                Spacer(modifier = Modifier.height(40.dp))
            } else {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ThemePreview(primary: Color, background: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(30.dp).clip(CircleShape).background(primary))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Box(modifier = Modifier.width(100.dp).height(8.dp).background(primary.copy(alpha = 0.7f)))
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.width(60.dp).height(6.6.dp).background(Color.Gray.copy(alpha = 0.3f)))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = primary), modifier = Modifier.height(32.dp)) {
                    Text("Sample Button", fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun PresetCard(
    preset: PresetOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(preset.color)
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = if (preset.type == ThemeType.CYBER || preset.type == ThemeType.SAKURA) Color.Black else Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Text(
            text = preset.name,
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ColorPickerGrid(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    val colors = listOf(
        Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF673AB7),
        Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF03A9F4), Color(0xFF00BCD4),
        Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39),
        Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800), Color(0xFFFF5722),
        Color(0xFF795548), Color(0xFF9E9E9E), Color(0xFF607D8B), Color.Black, Color.White,
        Color(0xFFFFF1F6), Color(0xFFE8F5E9), Color(0xFFFFF9C4)
    )

    Column {
        val rows = colors.chunked(6)
        rows.forEach { rowColors ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowColors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selectedColor == color) 2.dp else 1.dp,
                                color = if (selectedColor == color) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                            .clickable { onColorSelected(color) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedColor == color) {
                            Icon(
                                Icons.Default.Check,
                                null,
                                tint = if (color == Color.White || color == Color(0xFFFFEB3B)) Color.Black else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                // Fill empty space if row is not full
                repeat(6 - rowColors.size) {
                    Spacer(modifier = Modifier.size(45.dp))
                }
            }
        }
    }
}

data class PresetOption(val name: String, val type: ThemeType, val color: Color)
