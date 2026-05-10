package ph.edu.cksc.college.appdev.mydiary.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import ph.edu.cksc.college.appdev.mydiary.components.DateDialog
import ph.edu.cksc.college.appdev.mydiary.components.DiaryEntryComponent
import ph.edu.cksc.college.appdev.mydiary.components.DiaryEntryViewModel
import ph.edu.cksc.college.appdev.mydiary.components.TimeDialog
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import ph.edu.cksc.college.appdev.mydiary.service.StorageService
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryEntryScreen(
    id: String,
    viewModel: DiaryEntryViewModel,
    navController: NavHostController,
    storageService: StorageService
) {
    val context = LocalContext.current
    val entry by viewModel.diaryEntry

    // New state for Edit vs View mode
    var isEditing by remember { mutableStateOf(id.isEmpty() || id == "null") }

    // Logic to load data if ID exists (Edit Mode) or clear state (Add Mode)
    LaunchedEffect(id) {
        if (id.isNotEmpty() && id != "null") {
            val loadedEntry = storageService.getDiaryEntry(id)
            if (loadedEntry != null) {
                viewModel.diaryEntry.value = loadedEntry
                // Increment view count when viewing
                storageService.incrementViewCount(loadedEntry.id, loadedEntry.viewCount)
            }
        } else {
            viewModel.diaryEntry.value = DiaryEntry()
            isEditing = true
        }
        viewModel.modified = false
    }

    val date = try {
        LocalDateTime.parse(entry.dateTime)
    } catch (e: Exception) {
        LocalDateTime.now()
    }

    var showDatePicker by remember { mutableStateOf(false) }
    DateDialog(
        showDatePicker = showDatePicker, onShowDatePickerChange = { showDatePicker = it },
        date = date, onDateChange = { viewModel.onDateTimeChange(it) }
    )

    var showTimePicker by remember { mutableStateOf(false) }
    TimeDialog(
        showTimePicker = showTimePicker, onShowTimePickerChange = { showTimePicker = it },
        date = date, onDateChange = { viewModel.onDateTimeChange(it) }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                title = {
                    Text(
                        text = if (id.isEmpty() || id == "null") "New Entry" else if (isEditing) "Edit Entry" else "View Entry",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isEditing && id.isNotEmpty() && id != "null") {
                            isEditing = false // Go back to viewing
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, "Edit")
                        }
                    } else {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Filled.DateRange, "Date")
                        }
                        IconButton(onClick = { showTimePicker = true }) {
                            Icon(Icons.Filled.AccessTime, "Time")
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            DiaryEntryComponent(
                id = id,
                viewModel = viewModel,
                storageService = storageService,
                isEditing = isEditing, // Pass isEditing state
                onDateClick = { if (isEditing) showDatePicker = true },
                onCancel = {
                    if (id.isEmpty() || id == "null") {
                        navController.popBackStack()
                    } else {
                        isEditing = false
                    }
                },
                onSave = {
                    viewModel.onDoneClick {
                        Toast.makeText(context, "Entry saved successfully", Toast.LENGTH_SHORT).show()
                        isEditing = false // Switch back to viewing mode after save
                    }
                }
            )
        }
    }

    var showDiscardDialog by remember { mutableStateOf(false) }
    BackHandler(enabled = viewModel.modified && isEditing) {
        showDiscardDialog = true
    }

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Discard changes?") },
            text = { Text("You have unsaved changes. Are you sure you want to leave?") },
            confirmButton = {
                TextButton(onClick = {
                    showDiscardDialog = false
                    if (id.isEmpty() || id == "null") {
                        navController.popBackStack()
                    } else {
                        isEditing = false
                    }
                }) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}