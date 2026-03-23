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
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ph.edu.cksc.college.appdev.mydiary.LOGIN_SCREEN
import ph.edu.cksc.college.appdev.mydiary.MAIN_SCREEN
import ph.edu.cksc.college.appdev.mydiary.REGISTRATION_SCREEN

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavHostController) {
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
        AccountScrollContent(innerPadding, navController)
    }
}

@Composable
fun AccountScrollContent(innerPadding: PaddingValues, navController: NavHostController) {
    Box(
        modifier = Modifier.padding(innerPadding)
    ) {
        Column() {
            Button( onClick = {
                navController.navigate(REGISTRATION_SCREEN)

            }) {
                Text("Register")
            }
            Button( onClick = {
                navController.navigate(LOGIN_SCREEN)

            }) {
                Text("Login")
            }
            Button( onClick = {
                navController.navigate(MAIN_SCREEN)
            }) {
                Text("Guest lang po")
            }
        }
    }
}
