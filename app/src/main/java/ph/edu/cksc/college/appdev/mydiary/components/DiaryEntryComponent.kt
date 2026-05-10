package ph.edu.cksc.college.appdev.mydiary.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.cksc.college.appdev.mydiary.diary.moodList
import ph.edu.cksc.college.appdev.mydiary.diary.starList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiaryEntryComponent(
    id: String,
    viewModel: DiaryEntryViewModel,
    onDateClick: () -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    val entry by viewModel.diaryEntry
    
    val displayFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
    val date = try { LocalDateTime.parse(entry.dateTime) } catch (e: Exception) { LocalDateTime.now() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        // Subtle Date Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDateClick() }
        ) {
            Text(
                text = date.format(displayFormatter).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = date.format(timeFormatter),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Large Seamless Title
        TextField(
            value = entry.title,
            onValueChange = { viewModel.onTitleChange(it) },
            placeholder = { 
                Text(
                    "Entry Title", 
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                ) 
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Metadata Chips Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MoodSelectorChip(
                selectedMoodIndex = entry.mood,
                onMoodSelected = { viewModel.onMoodChange(it) }
            )
            
            StarSelectorChip(
                selectedStars = entry.star,
                onStarSelected = { viewModel.onStarChange(it) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Seamless Content Body
        TextField(
            value = entry.content,
            onValueChange = { viewModel.onContentChange(it) },
            placeholder = { 
                Text(
                    "Start writing here...", 
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                ) 
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 400.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 28.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Clean Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(), 
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Discard", color = MaterialTheme.colorScheme.outline)
            }
            
            Button(
                onClick = onSave,
                modifier = Modifier.weight(1.2f),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Icon(Icons.Default.Done, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Save Entry", fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodSelectorChip(
    selectedMoodIndex: Int,
    onMoodSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currentMood = moodList[selectedMoodIndex]

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        Surface(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            onClick = { expanded = true }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(currentMood.icon, null, tint = currentMood.color, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(currentMood.mood, style = MaterialTheme.typography.labelLarge)
                Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(18.dp))
            }
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            moodList.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(item.icon, null, tint = item.color, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(10.dp))
                            Text(item.mood)
                        }
                    },
                    onClick = { onMoodSelected(index); expanded = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarSelectorChip(
    selectedStars: Int,
    onStarSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        Surface(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            onClick = { expanded = true }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Star, null, tint = Color(0xFFFFB400), modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("$selectedStars Stars", style = MaterialTheme.typography.labelLarge)
                Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(18.dp))
            }
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            starList.forEach { star ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, null, tint = Color(0xFFFFB400), modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(10.dp))
                            Text("$star Stars")
                        }
                    },
                    onClick = { onStarSelected(star); expanded = false }
                )
            }
        }
    }
}
