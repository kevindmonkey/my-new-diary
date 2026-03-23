package ph.edu.cksc.college.appdev.mydiary.screens

import SampleDiaryEntries
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import ph.edu.cksc.college.appdev.mydiary.ABOUT_SCREEN
import ph.edu.cksc.college.appdev.mydiary.DIARY_ENTRY_SCREEN
import ph.edu.cksc.college.appdev.mydiary.components.DiaryList
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    val dataList = SampleDiaryEntries.entries   //viewModel.filterText(searchQuery).collectAsState(initial = emptyList())

    var isSearchExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text("App Dev Diary")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            (context as Activity).finish()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            isSearchExpanded = !isSearchExpanded
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search",
                            )
                        }
                        IconButton(onClick = {
                            navController.navigate(ABOUT_SCREEN)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "About"
                            )
                        }
                    },
                )
                if (isSearchExpanded) {
                    /*SearchBar(
                        query = searchQuery,
                        onQueryChange = { query -> searchQuery = query },
                        onSearch = { },
                        active = false,
                        onActiveChange = { },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                    }*/
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("$DIARY_ENTRY_SCREEN/")
            }) {
                Icon(Icons.Filled.Add,"")
            }
        }
    ) { innerPadding ->
        MainScrollContent(dataList, innerPadding, navController)
    }
}

@Composable
fun MainScrollContent(
    dataList: MutableList<DiaryEntry>,
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    Box(
        modifier = Modifier.padding(innerPadding)
    ) {
        DiaryList(dataList, navController)
    }
}
