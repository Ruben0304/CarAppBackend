package com.example.implementations

import com.example.enums.OAuthProvider
import com.example.models.User
import com.example.services.AuthService
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.*
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.Phone
import io.github.jan.supabase.auth.user.UserInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SupabaseAuthImpl : AuthService, KoinComponent {
    private val supabaseClient: SupabaseClient by inject()

    override suspend fun signInWithEmail(email: String, password: String): User? {
        supabaseClient.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        return null
    }

    override suspend fun signInWithPhone(phone: String, password: String): User? {
        supabaseClient.auth.signInWith(Phone) {
            this.phone = phone
            this.password = password
        }
        return null
    }

    override suspend fun signInWithOAuth(provider: OAuthProvider, redirectUrl: String?): User? {

        val supabaseProvider = when (provider) {
            OAuthProvider.FACEBOOK -> Facebook
            OAuthProvider.GOOGLE -> Google
            OAuthProvider.APPLE -> Apple
            OAuthProvider.TWITTER -> Twitter
        }

        supabaseClient.auth.signInWith(supabaseProvider)
        return null
    }

    override suspend fun retrieveUser(jwt: String): User {
        val userInfo = supabaseClient.auth.retrieveUser(jwt)
        return User(id = userInfo.id, email = userInfo.email!!)
    }

    override suspend fun updateUser(phone: String?, email: String? , password: String?): User? {
        val userInfo = supabaseClient.auth.updateUser {
             if (phone != null) this.phone = phone
             if (email != null) this.email = email
             if (password != null) this.password = password
        }

        return User(id = userInfo.id, email = userInfo.email!!)
    }

    override suspend fun registerUser(email: String?, phone: String?, password: String): User? {

        val user = if(phone != null) supabaseClient.auth.signUpWith(Phone) {
            this.phone = phone
            this.password = password
        }
        else if (email !=null ) supabaseClient.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        } else null

        println(user)
        return null
    }

    override suspend fun confirmUser(email: String?, phone: String?, code: String) {
        if (email != null)
           supabaseClient.auth.verifyEmailOtp(type = OtpType.Email.EMAIL, email = email, token = code)
        if (phone != null)
           supabaseClient.auth.verifyPhoneOtp(type = OtpType.Phone.SMS, phone = phone, token = code)
    }
}

