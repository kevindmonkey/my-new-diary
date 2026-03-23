package ph.edu.cksc.college.appdev.mydiary.components

import SampleDiaryEntries
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import ph.edu.cksc.college.appdev.mydiary.diary.moodList
import ph.edu.cksc.college.appdev.mydiary.diary.starList
import ph.edu.cksc.college.appdev.mydiary.ui.theme.MyDiaryTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiaryEntryComponent(
    viewModel: DiaryEntryViewModel,
    test: Boolean
) {
    val entry by viewModel.diaryEntry
    var expanded by remember { mutableStateOf(test) }
    var starExpanded by remember { mutableStateOf(test) }
    val formatter = DateTimeFormatter.ofPattern("MMM d, yy\nh:mm a")
    val date = LocalDateTime.parse(entry.dateTime)

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    value = moodList[entry.mood].mood,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true),
                    leadingIcon = {
                        Icon(
                            imageVector = moodList[entry.mood].icon,
                            tint = moodList[entry.mood].color,
                            contentDescription = moodList[entry.mood].mood
                        )
                    },
                    label = { Text("Mood") }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    moodList.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = {
                                Row() {
                                    Icon(
                                        imageVector = item.icon,
                                        tint = item.color,
                                        contentDescription = item.mood
                                    )
                                    Text(
                                        text = item.mood,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                            },
                            onClick = {
                                viewModel.onMoodChange(index)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                text = formatter.format(date)
            )
        }
        Row {
            OutlinedTextField(
                //modifier = Modifier.fillMaxWidth(),
                value = entry.title,
                onValueChange = {
                    viewModel.onTitleChange(it)
                },
                label = { Text("Title") }
            )
            ExposedDropdownMenuBox(
                expanded = starExpanded,
                onExpandedChange = {
                    starExpanded = !starExpanded
                }
            ) {
                OutlinedTextField(
                    value = "" + entry.star,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = starExpanded) },
                    modifier = Modifier.menuAnchor(),
                    label = { Text("Star") }
                )
                ExposedDropdownMenu(
                    expanded = starExpanded,
                    onDismissRequest = { starExpanded = false }
                ) {
                    starList.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "" + item,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            },
                            onClick = {
                                viewModel.onStarChange(item)
                                starExpanded = false
                            }
                        )
                    }
                }
            }
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = entry.content,
            onValueChange = {
                viewModel.onContentChange(it)
            },
            label = { Text("Content") }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun EditDiaryPreview() {
    val navController = rememberNavController()
    MyDiaryTheme(dynamicColor = false) {
        DiaryEntryComponent(
            viewModel = object : DiaryEntryViewModel {
                @SuppressLint("UnrememberedMutableState")
                override var diaryEntry = mutableStateOf(DiaryEntry())

                init {
                    diaryEntry.value = DiaryEntry(
                        "merong-id",
                        0, 5,
                        "Lexi",
                        "Test...Test...Test...",
                        LocalDateTime.of(2024, 1, 1, 7, 30).toString()
                    )
                }

                override var modified: Boolean = true

                override fun onTitleChange(newValue: String) {
                    diaryEntry.value = diaryEntry.value.copy(title = newValue)
                }

                override fun onContentChange(newValue: String) {
                    diaryEntry.value = diaryEntry.value.copy(content = newValue)
                }

                override fun onMoodChange(newValue: Int) {
                    diaryEntry.value = diaryEntry.value.copy(mood = newValue)
                }

                override fun onStarChange(newValue: Int) {
                    diaryEntry.value = diaryEntry.value.copy(star = newValue)
                }

                override fun onDateTimeChange(newValue: LocalDateTime) {
                    val newDueDate = newValue.toString()
                    diaryEntry.value = diaryEntry.value.copy(dateTime = newDueDate)
                }

                override fun onDoneClick(popUpScreen: () -> Unit) {

                }
            },
            test = true
            //navController = navController,
            //FirebaseAuth.getInstance(), Firebase.firestore
        )
    }
}