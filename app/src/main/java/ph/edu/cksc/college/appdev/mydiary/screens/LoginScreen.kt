package ph.edu.cksc.college.appdev.mydiary.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.launch
import ph.edu.cksc.college.appdev.mydiary.MAIN_SCREEN
import ph.edu.cksc.college.appdev.mydiary.components.LoginComponent
import ph.edu.cksc.college.appdev.mydiary.components.LoginViewModel
import ph.edu.cksc.college.appdev.mydiary.diary.Login
import ph.edu.cksc.college.appdev.mydiary.service.AccountService

var userSession: UserSession? = null

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    accountService: AccountService
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                title = { Text("Login", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        val scope = rememberCoroutineScope()
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            LoginComponent(
                viewModel = object : ViewModel(), LoginViewModel {
                    @SuppressLint("UnrememberedMutableState")
                    override var account = mutableStateOf(Login())
                    override var modified: Boolean = true
                    override var loginError: String = ""

                    override fun onEmailChange(newValue: String) {
                        account.value = account.value.copy(email = newValue)
                    }
                    override fun onPasswordChange(newValue: String) {
                        account.value = account.value.copy(password = newValue)
                    }
                    override suspend fun login(): String {
                        val result = accountService.signInUser(account.value.email, account.value.password)
                        if (result == "Success") {
                            scope.launch {
                                snackbarHostState.showSnackbar("Welcome back!")
                                navController.navigate(MAIN_SCREEN)
                            }
                        }
                        return result
                    }
                },
                test = false,
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
