package ph.edu.cksc.college.appdev.mydiary

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import ph.edu.cksc.college.appdev.mydiary.screens.*
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
        val storageService = remember { StorageService(supabase) }
        val accountService = remember { AccountService(supabase) }
        val snackbarHostState = remember { SnackbarHostState() }
        val navController = rememberNavController()

        val viewModel = remember {
            object: DiaryEntryViewModel {
                @SuppressLint("UnrememberedMutableState")
                override var diaryEntry = mutableStateOf(DiaryEntry())
                override var modified: Boolean = false

                override fun onTitleChange(newValue: String) {
                    diaryEntry.value = diaryEntry.value.copy(title = newValue)
                    modified = true
                }

                override fun onContentChange(newValue: String) {
                    if (diaryEntry.value.content != newValue) {
                        diaryEntry.value = diaryEntry.value.copy(content = newValue)
                        modified = true
                    }
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
                    diaryEntry.value = diaryEntry.value.copy(dateTime = newValue.toString())
                    modified = true
                }

                override fun onDoneClick(popUpScreen: () -> Unit) {
                    scope.launch {
                        try {
                            val entry = diaryEntry.value
                            if (entry.id.isBlank()) {
                                storageService.save(entry)
                            } else {
                                storageService.update(entry)
                            }
                            modified = false
                            popUpScreen()
                        } catch (e: Exception) {
                            Log.e("Save", "Error saving entry", e)
                        }
                    }
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            NavHost(
                navController = navController, 
                startDestination = ACCOUNT_SCREEN,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(ACCOUNT_SCREEN) { AccountScreen(navController) }
                composable(MAIN_SCREEN) { MainScreen(navController, storageService) }
                composable(ABOUT_SCREEN) { AboutScreen(navController) }
                composable(LOGIN_SCREEN) { LoginScreen(navController, snackbarHostState, accountService) }
                composable(REGISTRATION_SCREEN) { RegistrationScreen(navController, snackbarHostState, accountService) }
                composable(
                    "$DIARY_ENTRY_SCREEN/{id}",
                    arguments = listOf(navArgument("id") { 
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id") ?: ""
                    DiaryEntryScreen(id, viewModel, navController, storageService)
                }
            }
        }
    }
}
