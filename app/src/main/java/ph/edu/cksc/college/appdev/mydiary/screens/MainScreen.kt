package ph.edu.cksc.college.appdev.mydiary.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ph.edu.cksc.college.appdev.mydiary.ABOUT_SCREEN
import ph.edu.cksc.college.appdev.mydiary.ACCOUNT_SCREEN
import ph.edu.cksc.college.appdev.mydiary.DIARY_ENTRY_SCREEN
import ph.edu.cksc.college.appdev.mydiary.THEME_SCREEN
import ph.edu.cksc.college.appdev.mydiary.components.DiaryList
import ph.edu.cksc.college.appdev.mydiary.diary.moodList
import ph.edu.cksc.college.appdev.mydiary.diary.starList
import ph.edu.cksc.college.appdev.mydiary.service.AccountService
import ph.edu.cksc.college.appdev.mydiary.service.StorageService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController, 
    storageService: StorageService,
    accountService: AccountService
) {
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf<Int?>(null) }
    var selectedStar by remember { mutableStateOf<Int?>(null) }
    var isAscending by remember { mutableStateOf(false) }

    val dataList by remember(searchQuery, selectedMood, selectedStar, isAscending) {
        storageService.getFilteredEntries(searchQuery, selectedMood, selectedStar, isAscending)
    }.collectAsState(initial = emptyList())

    var isSearchExpanded by remember { mutableStateOf(false) }
    var moodExpanded by remember { mutableStateOf(false) }
    var starExpanded by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    title = { 
                        Text(
                            "My Diary", 
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        ) 
                    },
                    actions = {
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
                        
                        IconButton(onClick = { isAscending = !isAscending }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = "Sort",
                                tint = if (isAscending) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More")
                            }
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Themes") },
                                    onClick = { 
                                        showMenu = false
                                        navController.navigate(THEME_SCREEN) 
                                    },
                                    leadingIcon = { Icon(Icons.Default.Palette, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("About") },
                                    onClick = { 
                                        showMenu = false
                                        navController.navigate(ABOUT_SCREEN) 
                                    },
                                    leadingIcon = { Icon(Icons.Default.Info, null) }
                                )
                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                                DropdownMenuItem(
                                    text = { Text("Logout") },
                                    onClick = { 
                                        showMenu = false
                                        scope.launch {
                                            accountService.signOut()
                                            navController.navigate(ACCOUNT_SCREEN) {
                                                popUpTo(0) { inclusive = true }
                                            }
                                        }
                                    },
                                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.Logout, null, tint = MaterialTheme.colorScheme.error) },
                                    colors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.error
                                    )
                                )
                            }
                        }
                    },
                )
                if (isSearchExpanded) {
                    SearchBarContent(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        moodExpanded = moodExpanded,
                        onMoodExpandedChange = { moodExpanded = it },
                        selectedMood = selectedMood,
                        onMoodSelect = { selectedMood = it },
                        starExpanded = starExpanded,
                        onStarExpandedChange = { starExpanded = it },
                        selectedStar = selectedStar,
                        onStarSelect = { selectedStar = it }
                    )
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
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            DiaryList(
                entries = dataList,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarContent(
    query: String,
    onQueryChange: (String) -> Unit,
    moodExpanded: Boolean,
    onMoodExpandedChange: (Boolean) -> Unit,
    selectedMood: Int?,
    onMoodSelect: (Int?) -> Unit,
    starExpanded: Boolean,
    onStarExpandedChange: (Boolean) -> Unit,
    selectedStar: Int?,
    onStarSelect: (Int?) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = { },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text("Search your memories...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(Icons.Default.Clear, "Clear")
                            }
                        }
                    }
                )
            },
            expanded = false,
            onExpandedChange = { },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {}

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = moodExpanded,
                onExpandedChange = onMoodExpandedChange,
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedMood?.let { moodList[it].mood } ?: "All Moods",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Mood") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(moodExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(expanded = moodExpanded, onDismissRequest = { onMoodExpandedChange(false) }) {
                    DropdownMenuItem(text = { Text("All Moods") }, onClick = { onMoodSelect(null); onMoodExpandedChange(false) })
                    moodList.forEachIndexed { index, mood ->
                        DropdownMenuItem(
                            text = {
                                Row {
                                    Icon(mood.icon, null, tint = mood.color, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(mood.mood)
                                }
                            },
                            onClick = { onMoodSelect(index); onMoodExpandedChange(false) }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = starExpanded,
                onExpandedChange = onStarExpandedChange,
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedStar?.let { "$it Stars" } ?: "All Ratings",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rating") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(starExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(expanded = starExpanded, onDismissRequest = { onStarExpandedChange(false) }) {
                    DropdownMenuItem(text = { Text("All Ratings") }, onClick = { onStarSelect(null); onStarExpandedChange(false) })
                    starList.forEach { star ->
                        DropdownMenuItem(
                            text = { Text("$star Stars") },
                            onClick = { onStarSelect(star); onStarExpandedChange(false) }
                        )
                    }
                }
            }
        }
    }
}
