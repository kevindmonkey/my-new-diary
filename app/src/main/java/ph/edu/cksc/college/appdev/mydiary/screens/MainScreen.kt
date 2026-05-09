package ph.edu.cksc.college.appdev.mydiary.screens

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.jan.supabase.postgrest.from
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import ph.edu.cksc.college.appdev.mydiary.ABOUT_SCREEN
import ph.edu.cksc.college.appdev.mydiary.DIARY_ENTRY_SCREEN
import ph.edu.cksc.college.appdev.mydiary.components.DiaryList
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
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
    val star: Int
)

// just a demo to read entries
suspend fun initDiaryList(): List<Entry> {
    val list = supabase.from("entries").select().decodeList<Entry>()
    return list
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, storageService: StorageService) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    val dataList by storageService.getFilteredEntries(searchQuery).collectAsState(initial = emptyList())
    var isSearchExpanded by remember { mutableStateOf(false) }

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
                        IconButton(onClick = { 
                            isSearchExpanded = !isSearchExpanded 
                            if (!isSearchExpanded) searchQuery = ""
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
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = { },
                        active = false,
                        onActiveChange = { },
                        placeholder = { Text("Search entries...") },
                        colors = SearchBarDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {}
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
