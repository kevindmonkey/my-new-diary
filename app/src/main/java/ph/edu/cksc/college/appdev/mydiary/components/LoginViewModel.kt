package ph.edu.cksc.college.appdev.mydiary.components

import androidx.compose.runtime.MutableState
import ph.edu.cksc.college.appdev.mydiary.diary.Login

interface LoginViewModel {

    var account: MutableState<Login>

    var modified: Boolean

    var loginError: String

    fun onEmailChange(newValue: String)

    fun onPasswordChange(newValue: String)

    suspend fun login(): String
}