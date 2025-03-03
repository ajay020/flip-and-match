package com.example.flipmatch.ui.login

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipmatch.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "login view model"

@HiltViewModel
class LoginViewModel
    @Inject
    constructor() : ViewModel() {
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()
        private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
        val user: StateFlow<FirebaseUser?> = _user

        fun signInWithGoogle(
            context: Context,
            onSuccess: (String) -> Unit,
            onError: (String) -> Unit,
        ) {
            // Instantiate a Google sign-in request
            val googleIdOption =
                GetGoogleIdOption
                    .Builder()
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
//                    .setFilterByAuthorizedAccounts(false) // Allow new sign-ins
//                    .setAutoSelectEnabled(true) // Enables auto sign-in
                    .build()

            // Create the Credential Manager request
            val request =
                GetCredentialRequest
                    .Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

            val credentialManager = CredentialManager.create(context)

            viewModelScope.launch {
                try {
                    val response: GetCredentialResponse = credentialManager.getCredential(context, request)
                    val googleIdTokenCredential = response.credential as? GoogleIdTokenCredential
                    googleIdTokenCredential?.idToken?.let { idToken ->
                        firebaseAuthWithGoogle(idToken, onSuccess, onError)
                    } ?: onError("Google Sign-In failed")
                } catch (e: Exception) {
                    onError("Sign-in failed: ${e.message}")
                    Log.d("LoginViewModel", "Sign in failed: $e.message")
                }
            }
        }

        private fun firebaseAuthWithGoogle(
            idToken: String,
            onSuccess: (String) -> Unit,
            onError: (String) -> Unit,
        ) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = auth.currentUser
                    onSuccess("Sign-in successful")
                } else {
                    onError("Firebase Authentication failed: ${task.exception?.message}")
                }
            }
        }

//        private fun signOut() {
//            // Firebase sign out
//            auth.signOut()
//
//            // When a user signs out, clear the current user credential state from all credential providers.
//            viewModelScope.launch {
//                try {
//                    val clearRequest = ClearCredentialStateRequest()
//                    credentialManager.clearCredentialState(clearRequest)
//                } catch (e: ClearCredentialException) {
//                    Log.e(TAG, "Couldn't clear user credentials: ${e.localizedMessage}")
//                }
//            }
//        }
    }
