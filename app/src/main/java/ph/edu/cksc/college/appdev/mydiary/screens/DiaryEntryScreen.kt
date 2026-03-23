package ph.edu.cksc.college.appdev.mydiary.screens

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.getValue
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
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryEntryScreen(
    viewModel: DiaryEntryViewModel,
    navController: NavHostController
) {
    val entry by viewModel.diaryEntry
    val activity = LocalContext.current
    val date: LocalDateTime = LocalDateTime.parse(entry.dateTime)

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
                        if (entry.id.isEmpty())
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
                                    if (entry.id.isEmpty()) "New Entry" else "Entry updated: ${entry.id}",
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
