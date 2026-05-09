package ph.edu.cksc.college.appdev.mydiary.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ph.edu.cksc.college.appdev.mydiary.diary.Registration
import ph.edu.cksc.college.appdev.mydiary.ui.theme.MyDiaryTheme

@Composable
fun RegisterComponent(
    viewModel: RegisterViewModel,
    onCancel: () -> Unit,
    test: Boolean
) {
    val entry by viewModel.account
    var error by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Join us to start your diary",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = entry.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Name") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = entry.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = entry.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = entry.retypePassword,
            onValueChange = { viewModel.onRetypePasswordChange(it) },
            label = { Text("Retype Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        if (error.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.error,
                text = error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                error = ""
                if (entry.name.isEmpty() || entry.email.isEmpty()) {
                    error = "Name and email are required"
                } else if (entry.password != entry.retypePassword) {
                    error = "Passwords do not match"
                } else {
                    scope.launch(Dispatchers.IO) {
                        val result = viewModel.register()
                        if (result != "Success") {
                            error = result
                        }
                    }
                }
            }
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onCancel() }
        ) {
            Text("Cancel")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun RegisterComponentPreview() {
    MyDiaryTheme(dynamicColor = false) {
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
                override suspend fun register(): String = "Success"
            },
            test = true,
            onCancel = {}
        )
    }
}