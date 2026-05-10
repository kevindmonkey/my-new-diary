package ph.edu.cksc.college.appdev.mydiary.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ph.edu.cksc.college.appdev.mydiary.LOGIN_SCREEN
import ph.edu.cksc.college.appdev.mydiary.MAIN_SCREEN
import ph.edu.cksc.college.appdev.mydiary.R
import ph.edu.cksc.college.appdev.mydiary.REGISTRATION_SCREEN
import ph.edu.cksc.college.appdev.mydiary.supabase

@Serializable
data class Profile (
    val id: String,
    val email: String,
    @SerialName("full_name")  val fullName: String)

fun getSession(): String {
    try {
        val session: UserSession? = supabase.auth.currentSessionOrNull()
        userSession = session
        return "Success"
    } catch (e: Exception) {
        return e.message ?: "Session error"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo - Using Icon directly without any Surface or background circle so it blends perfectly
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "App Icon",
                modifier = Modifier.size(140.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "My Diary",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = (-1).sp
            )
            Text(
                text = "Capture your thoughts, preserve your soul.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(56.dp))

            val scope = rememberCoroutineScope()

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                onClick = { navController.navigate(LOGIN_SCREEN) },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Sign In", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                onClick = { navController.navigate(REGISTRATION_SCREEN) },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Create New Account", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                modifier = Modifier.clickable {
                    scope.launch {
                        val result = getSession()
                        if (result == "Success" && userSession != null) {
                            navController.navigate(MAIN_SCREEN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                }.padding(8.dp),
                text = "Resume Session",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
