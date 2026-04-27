package ph.edu.cksc.college.appdev.mydiary.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ph.edu.cksc.college.appdev.mydiary.LOGIN_SCREEN
import ph.edu.cksc.college.appdev.mydiary.MAIN_SCREEN
import ph.edu.cksc.college.appdev.mydiary.REGISTRATION_SCREEN
import ph.edu.cksc.college.appdev.mydiary.supabase

@Serializable
data class Profile (
    val id: String,
    val email: String,
    @SerialName("full_name")  val fullName: String)

// just a demo to read profiles
suspend fun init() {
    val list = supabase.from("profiles").select().decodeList<Profile>()
    Log.d("AccountScreen", list.toString())
}

fun getSettion(): String {
    try {
        val session: UserSession? = supabase.auth.currentSessionOrNull()
        userSession = session
        println("User signed in previously successfully: ${userSession}")
        return "Success"
    } catch (e: Exception) {
        val error = e.message ?: "Session error"
        Log.e("Session", error, e)
        println("Get Session failed: ${error}")
        return error
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavHostController) {
    // if you want automatic, uncomment these
    /*val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            val result = getSettion()
            if (result == "Success") {
                navController.navigate(MAIN_SCREEN)
            }
        }
    }*/
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
    val scope = rememberCoroutineScope()
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
                scope.launch {
                    supabase.auth.signOut()
                }
            }) {
                Text("Log out")
            }
            Button( onClick = {
                scope.launch {
                    val result = getSettion()
                    if (result == "Success") {
                        navController.navigate(MAIN_SCREEN)
                    }
                }
            }) {
                Text("Resume if already signed in")
            }
        }
    }
}
