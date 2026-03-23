package ph.edu.cksc.college.appdev.mydiary.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ph.edu.cksc.college.appdev.mydiary.MAIN_SCREEN
import ph.edu.cksc.college.appdev.mydiary.components.RegisterComponent
import ph.edu.cksc.college.appdev.mydiary.components.RegisterViewModel
import ph.edu.cksc.college.appdev.mydiary.diary.Registration

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("App Dev Demo")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        RegistrationScrollContent(innerPadding, navController)
    }
}

@Composable
fun RegistrationScrollContent(innerPadding: PaddingValues, navController: NavHostController) {
    Box(
        modifier = Modifier.padding(innerPadding)
    ) {
        RegisterComponent(
            viewModel = object : RegisterViewModel {
                @SuppressLint("UnrememberedMutableState")
                override var account = mutableStateOf(Registration())

                override var modified: Boolean = true

                override fun onNameChange(newValue: String) {
                    account.value = account.value.copy(name = newValue)
                }

                override fun onEmailChange(newValue: String) {
                    account.value = account.value.copy(email = newValue)
                }

                override fun onPasswordChange(newValue: String) {
                    account.value = account.value.copy(password = newValue)
                }

                override fun onRetypePasswordChange(newValue: String) {
                    account.value = account.value.copy(retypePassword = newValue)
                }
            },
            test = false
        )
    }
}
