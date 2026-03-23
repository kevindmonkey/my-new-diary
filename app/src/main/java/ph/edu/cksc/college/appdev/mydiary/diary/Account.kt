package ph.edu.cksc.college.appdev.mydiary.diary

import java.time.LocalDateTime

data class Account(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val dateTime: String = LocalDateTime.now().toString()
)

data class Registration(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val retypePassword: String = "",

)


data class Login (
    val email: String = "",
    val password: String = "",
)

