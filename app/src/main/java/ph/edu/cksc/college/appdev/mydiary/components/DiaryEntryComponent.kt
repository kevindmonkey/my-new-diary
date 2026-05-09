package ph.edu.cksc.college.appdev.mydiary.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    var moodExpanded by remember { mutableStateOf(false) }
    var starExpanded by remember { mutableStateOf(false) }
    
    val displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val date = try { LocalDateTime.parse(entry.dateTime) } catch (e: Exception) { LocalDateTime.now() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(
            text = if (id.isEmpty() || id == "null") "Add Diary Item" else "Edit Diary Item",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Metadata: Date, Mood, Rating
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = date.format(displayFormatter),
                onValueChange = {},
                readOnly = true,
                label = { Text("Date/time") },
                modifier = Modifier.weight(1f).clickable { onDateClick() },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                )
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ExposedDropdownMenuBox(
                expanded = moodExpanded,
                onExpandedChange = { moodExpanded = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = moodList[entry.mood].mood,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Mood") },
                    leadingIcon = { Icon(moodList[entry.mood].icon, null, tint = moodList[entry.mood].color) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = moodExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(expanded = moodExpanded, onDismissRequest = { moodExpanded = false }) {
                    moodList.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { 
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(item.icon, null, tint = item.color, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(item.mood)
                                }
                            },
                            onClick = { viewModel.onMoodChange(index); moodExpanded = false }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = starExpanded,
                onExpandedChange = { starExpanded = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = "★".repeat(entry.star),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rating") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = starExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(expanded = starExpanded, onDismissRequest = { starExpanded = false }) {
                    starList.forEach { star ->
                        DropdownMenuItem(
                            text = { Text("★".repeat(star)) },
                            onClick = { viewModel.onStarChange(star); starExpanded = false }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = entry.title,
            onValueChange = { viewModel.onTitleChange(it) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = entry.content,
            onValueChange = { viewModel.onContentChange(it) },
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 250.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant, 
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onSave) {
                Text("Save")
            }
        }
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}
