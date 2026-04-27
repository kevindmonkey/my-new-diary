package ph.edu.cksc.college.appdev.mydiary.service

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

var userSession: UserSession? = null

class AccountService(val supabase: SupabaseClient) {

    suspend fun signInUser(email: String, password: String): String {
        try {
            val result = supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            val session: UserSession? = supabase.auth.currentSessionOrNull()
            userSession = session
            println("User signed in successfully: ${userSession}")
            return "Success"
        } catch (e: Exception) {
            val error = e.message ?: "Login error"
            Log.e("Login", error, e)
            println("Sign-in failed: ${error}")
            return error
        }
    }

    suspend fun registerUser(name: String, email: String, password: String): String {
        try {
            val reponse = supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("full_name", name)
                    put("email", email)
                }
            }
            println("User registered successfully: ${reponse}")
            val session: UserSession? = supabase.auth.currentSessionOrNull()
            userSession = session
            println("User auto signed in successfully: ${userSession}")
            return "Success"
        } catch (e: Exception) {
            val error = e.message ?: "Sign up error"
            Log.e("Sign up", error, e)
            println("Sign-up failed: ${error}")
            return error
        }
    }
}