package ph.edu.cksc.college.appdev.mydiary.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import ph.edu.cksc.college.appdev.mydiary.components.DateDialog
import ph.edu.cksc.college.appdev.mydiary.components.DiaryEntryComponent
import ph.edu.cksc.college.appdev.mydiary.components.DiaryEntryViewModel
import ph.edu.cksc.college.appdev.mydiary.components.TimeDialog
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import ph.edu.cksc.college.appdev.mydiary.service.StorageService
import java.time.LocalDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryEntryScreen(
    id: String,
    viewModel: DiaryEntryViewModel,
    navController: NavHostController,
    storageService: StorageService
) {
    val entries = remember { mutableStateListOf(DiaryEntry()) }
    LaunchedEffect(Unit) {
        Log.d("Dinner", "Breakfast")
        val entry = if (id != "")
            storageService.getDiaryEntry(id) ?: DiaryEntry()
        else
            DiaryEntry()
        entries.add(0, entry)
        Log.d("Entry", entries[0].toString())
        viewModel.diaryEntry = mutableStateOf(entry)
    }
    val activity = LocalContext.current
    val date: LocalDateTime = LocalDateTime.parse(entries[0].dateTime)

    var showDatePicker by remember { mutableStateOf(false) }
    DateDialog(
        showDatePicker = showDatePicker, onShowDatePickerChange = { showDatePicker = it},
        date = date, onDateChange = { viewModel.onDateTimeChange(it) }
    )

    var showTimePicker by remember { mutableStateOf(false) }
    TimeDialog(
        showTimePicker = showTimePicker, onShowTimePickerChange = { showTimePicker = it},
        date = date, onDateChange = { viewModel.onDateTimeChange(it) }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        if (entries[0].id.isEmpty())
                            "Add Diary Entry"
                        else
                            "Edit Diary Entry"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // check if modified...
                        if (viewModel.modified) {
                            viewModel.onDoneClick {
                                Toast.makeText(
                                    activity,
                                    if (entries[0].id.isEmpty()) "New Entry" else "Entry updated: ${entries[0].id}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.popBackStack()
                            }
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showDatePicker = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Date",
                        )
                    }
                    IconButton(onClick = {
                        showTimePicker = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.AccessTime,
                            contentDescription = "Time"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            DiaryEntryComponent(
                viewModel = viewModel,
                test = false
            )
        }
    }
    val openDialog = remember { mutableStateOf(false)  }
    BackHandler(
        enabled = true
    ) {
        if (viewModel.modified) {
            openDialog.value = true
        } else {
            navController.popBackStack()
        }
    }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Diary Entry")
            },
            text = {
                Text("Discard changes?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        navController.popBackStack()
                    }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text("No")
                }
            }
        )
    }
}
