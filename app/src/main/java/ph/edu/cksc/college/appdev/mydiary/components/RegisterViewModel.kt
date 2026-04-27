package ph.edu.cksc.college.appdev.mydiary.components

import androidx.compose.runtime.MutableState
import ph.edu.cksc.college.appdev.mydiary.diary.Registration

interface RegisterViewModel {

    var account: MutableState<Registration>

    var modified: Boolean

    fun onNameChange(newValue: String)

    fun onEmailChange(newValue: String)

    fun onPasswordChange(newValue: String)

    fun onRetypePasswordChange(newValue: String)

    suspend fun register(): String
}
