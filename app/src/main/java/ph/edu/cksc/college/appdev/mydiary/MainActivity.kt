package ph.edu.cksc.college.appdev.mydiary

import SampleDiaryEntries
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.launch
import ph.edu.cksc.college.appdev.mydiary.components.DiaryEntryViewModel
import ph.edu.cksc.college.appdev.mydiary.diary.DiaryEntry
import ph.edu.cksc.college.appdev.mydiary.screens.AboutScreen
import ph.edu.cksc.college.appdev.mydiary.screens.AccountScreen
import ph.edu.cksc.college.appdev.mydiary.screens.DiaryEntryScreen
import ph.edu.cksc.college.appdev.mydiary.screens.LoginScreen
import ph.edu.cksc.college.appdev.mydiary.screens.MainScreen
import ph.edu.cksc.college.appdev.mydiary.screens.RegistrationScreen
import ph.edu.cksc.college.appdev.mydiary.service.AccountService
import ph.edu.cksc.college.appdev.mydiary.service.StorageService
import ph.edu.cksc.college.appdev.mydiary.ui.theme.MyDiaryTheme
import java.time.LocalDateTime

val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.SUPABASE_URL,
    supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY
) {
    install(Auth)
    install(Postgrest)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyDiaryTheme {
                AppNavigation()
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun AppNavigation() {
        val scope = rememberCoroutineScope()
        val storageService = StorageService(supabase)
        val accountService = AccountService(supabase)
        val snackbarHostState = remember { SnackbarHostState() }
        val navController = rememberNavController()
        val viewModel = object: DiaryEntryViewModel {
            @SuppressLint("UnrememberedMutableState")
            override var diaryEntry = mutableStateOf(DiaryEntry())
            init {
                diaryEntry.value = DiaryEntry(
                    "",
                    0,
                    5,
                    "Lexi",
                    "Test...Test...Test...",
                    LocalDateTime.  of(2024, 1, 1, 7, 30).toString()
                )
            }
            override var modified: Boolean = false

            override fun onTitleChange(newValue: String) {
                diaryEntry.value = diaryEntry.value.copy(title = newValue)
                modified = true
            }
            override fun onContentChange(newValue: String) {
                diaryEntry.value = diaryEntry.value.copy(content = newValue)
                modified = true
            }
            override fun onMoodChange(newValue: Int) {
                diaryEntry.value = diaryEntry.value.copy(mood = newValue)
                modified = true
            }
            override fun onStarChange(newValue: Int) {
                diaryEntry.value = diaryEntry.value.copy(star = newValue)
                modified = true
            }
            override fun onDateTimeChange(newValue: LocalDateTime) {
                val newDueDate = newValue.toString()
                diaryEntry.value = diaryEntry.value.copy(dateTime = newDueDate)
                modified = true
            }
            override fun onDoneClick(popUpScreen: () -> Unit) {
                scope.launch {
                    val editedEntry = diaryEntry.value
                    if (editedEntry.id.isBlank()) {
                        storageService.save(editedEntry)
                    } else {
                        storageService.update(editedEntry)
                    }
                    popUpScreen()
                }
                //navController.popBackStack()
            }
        }
        Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerPadding ->
            NavHost(navController = navController, startDestination = ACCOUNT_SCREEN) {
                composable(ACCOUNT_SCREEN) { AccountScreen(navController = navController) }
                composable(MAIN_SCREEN) { MainScreen(navController = navController, storageService = storageService) }
                composable(ABOUT_SCREEN) { AboutScreen(navController = navController) }
                composable(LOGIN_SCREEN) { LoginScreen(navController = navController, snackbarHostState = snackbarHostState, accountService = accountService) }
                composable(REGISTRATION_SCREEN) { RegistrationScreen(navController = navController, snackbarHostState = snackbarHostState, accountService = accountService) }
                composable(
                    "$DIARY_ENTRY_SCREEN/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val id = arguments.getString("id") ?: "1"
                    Log.d("Test", "id: " + id)
                    for (entry in SampleDiaryEntries.entries) {
                        if (entry.id == id) {
                            viewModel.diaryEntry = mutableStateOf(entry)
                            break
                        }
                    }
                    //viewModel.diaryEntry
                    //val viewModel: DiaryEntryViewModel = hiltViewModel()
                    DiaryEntryScreen(id, navController = navController, viewModel = viewModel, storageService = storageService)
                }
            }
        }
    }
}