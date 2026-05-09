package ph.edu.cksc.college.appdev.mydiary.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
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
    
    // Logic to load data if ID exists (Edit Mode) or clear state (Add Mode)
    LaunchedEffect(id) {
        if (id.isNotEmpty() && id != "null") {
            val loadedEntry = storageService.getDiaryEntry(id)
            if (loadedEntry != null) {
                viewModel.diaryEntry.value = loadedEntry
            }
        } else {
            viewModel.diaryEntry.value = DiaryEntry()
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
                        text = if (id.isEmpty() || id == "null") "Add Diary Item" else "Edit Diary Item",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Filled.DateRange, "Date")
                    }
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(Icons.Filled.AccessTime, "Time")
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            DiaryEntryComponent(
                id = id,
                viewModel = viewModel,
                onDateClick = { showDatePicker = true },
                onCancel = { navController.popBackStack() },
                onSave = {
                    viewModel.onDoneClick {
                        Toast.makeText(context, "Entry saved successfully", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
            )
        }
    }

    var showDiscardDialog by remember { mutableStateOf(false) }
    BackHandler(enabled = viewModel.modified) {
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
                    navController.popBackStack()
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
