package ph.edu.cksc.college.appdev.mydiary.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.jan.supabase.postgrest.from
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import ph.edu.cksc.college.appdev.mydiary.ABOUT_SCREEN
import ph.edu.cksc.college.appdev.mydiary.DIARY_ENTRY_SCREEN
import ph.edu.cksc.college.appdev.mydiary.THEME_SCREEN
import ph.edu.cksc.college.appdev.mydiary.components.DiaryList
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import ph.edu.cksc.college.appdev.mydiary.diary.moodList
import ph.edu.cksc.college.appdev.mydiary.diary.starList
import ph.edu.cksc.college.appdev.mydiary.service.StorageService
import ph.edu.cksc.college.appdev.mydiary.supabase

@Serializable
data class Entry(
    val id: String,
    val user_id: String,
    val created_at: Instant,
    val title: String,
    val content: String,
    val mood: Int,
    val star: Int,
    val photo_urls: List<String> = emptyList(),
    val voice_memo_urls: List<String> = emptyList(),
    val view_count: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, storageService: StorageService) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf<Int?>(null) }
    var selectedStar by remember { mutableStateOf<Int?>(null) }
    var isAscending by remember { mutableStateOf(false) } // Default newest first

    val dataList by storageService.getFilteredEntries(searchQuery, selectedMood, selectedStar, isAscending)
        .collectAsState(initial = emptyList())

    var isSearchExpanded by remember { mutableStateOf(false) }
    var moodExpanded by remember { mutableStateOf(false) }
    var starExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    title = {
                        Text("My Diary", fontWeight = FontWeight.SemiBold)
                    },
                    navigationIcon = {
                        IconButton(onClick = { (context as Activity).finish() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isAscending = !isAscending }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = "Sort",
                                tint = if (isAscending) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { navController.navigate(THEME_SCREEN) }) {
                            Icon(Icons.Default.Brush, contentDescription = "Themes")
                        }
                        IconButton(onClick = {
                            isSearchExpanded = !isSearchExpanded
                            if (!isSearchExpanded) {
                                searchQuery = ""
                                selectedMood = null
                                selectedStar = null
                            }
                        }) {
                            Icon(
                                imageVector = if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                        IconButton(onClick = { navController.navigate(ABOUT_SCREEN) }) {
                            Icon(Icons.Default.Info, "About")
                        }
                    },
                )
                if (isSearchExpanded) {
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        SearchBar(
                            inputField = {
                                SearchBarDefaults.InputField(
                                    query = searchQuery,
                                    onQueryChange = { searchQuery = it },
                                    onSearch = { },
                                    expanded = false,
                                    onExpandedChange = { },
                                    placeholder = { Text("Search title or content...") },
                                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                    trailingIcon = {
                                        if (searchQuery.isNotEmpty()) {
                                            IconButton(onClick = { searchQuery = "" }) {
                                                Icon(Icons.Default.Clear, contentDescription = "Clear search")
                                            }
                                        }
                                    }
                                )
                            },
                            expanded = false,
                            onExpandedChange = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            colors = SearchBarDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {}

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Mood Dropdown
                            ExposedDropdownMenuBox(
                                expanded = moodExpanded,
                                onExpandedChange = { moodExpanded = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedMood?.let { moodList[it].mood } ?: "All Moods",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Mood") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = moodExpanded) },
                                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = moodExpanded,
                                    onDismissRequest = { moodExpanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("All Moods") },
                                        onClick = { selectedMood = null; moodExpanded = false }
                                    )
                                    moodList.forEachIndexed { index, mood ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(mood.icon, null, tint = mood.color, modifier = Modifier.size(20.dp))
                                                    Spacer(Modifier.width(8.dp))
                                                    Text(mood.mood)
                                                }
                                            },
                                            onClick = { selectedMood = index; moodExpanded = false }
                                        )
                                    }
                                }
                            }

                            // Star Rating Dropdown
                            ExposedDropdownMenuBox(
                                expanded = starExpanded,
                                onExpandedChange = { starExpanded = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedStar?.let { "$it Stars" } ?: "All Ratings",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Rating") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = starExpanded) },
                                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = starExpanded,
                                    onDismissRequest = { starExpanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("All Ratings") },
                                        onClick = { selectedStar = null; starExpanded = false }
                                    )
                                    starList.forEach { star ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFB400), modifier = Modifier.size(18.dp))
                                                    Spacer(Modifier.width(8.dp))
                                                    Text("$star Stars")
                                                }
                                            },
                                            onClick = { selectedStar = star; starExpanded = false }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { navController.navigate("$DIARY_ENTRY_SCREEN/") }
            ) {
                Icon(Icons.Filled.Add, "Add Entry")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            DiaryList(dataList, navController)
        }
    }
}