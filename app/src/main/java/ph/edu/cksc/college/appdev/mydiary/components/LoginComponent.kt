package ph.edu.cksc.college.appdev.mydiary.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ph.edu.cksc.college.appdev.mydiary.diary.Login
import ph.edu.cksc.college.appdev.mydiary.diary.Registration
import ph.edu.cksc.college.appdev.mydiary.ui.theme.MyDiaryTheme

@Composable
fun LoginComponent(
    viewModel: LoginViewModel,
    test: Boolean
) {
    val entry by viewModel.account
    var error by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(4.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = entry.email,
            onValueChange = {
                viewModel.onEmailChange(it)
            },
            label = { Text("Email") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = entry.password,
            onValueChange = {
                viewModel.onPasswordChange(it)
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            OutlinedButton(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    // TODO
                }
            ) {
                Text("Cancel")
            }
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    error = ""
                    if (entry.password.isEmpty() || entry.email.isEmpty()) {
                        error = "Email and Password are required"
                    }
                }
            ) {
                Text("Login")
            }
        }
        Text(color = Color.Red, text = error)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun LoginComponentPreview() {
    MyDiaryTheme(dynamicColor = false) {
        LoginComponent(
            viewModel = object : LoginViewModel {
                @SuppressLint("UnrememberedMutableState")
                override var account = mutableStateOf(Login())

                override var modified: Boolean = true

                override fun onEmailChange(newValue: String) {
                    account.value = account.value.copy(email = newValue)
                }

                override fun onPasswordChange(newValue: String) {
                    account.value = account.value.copy(password = newValue)
                }
            },
            test = true
        )
    }
}