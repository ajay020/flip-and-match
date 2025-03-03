package com.example.flipmatch.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.flipmatch.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val firestore: FirebaseFirestore,
        private val auth: FirebaseAuth,
    ) : ViewModel() {
        private val _userData = MutableStateFlow<User?>(null)
        val userData: StateFlow<User?> = _userData

        init {
            fetchUserData()
        }

        private fun fetchUserData() {
            val user = auth.currentUser
            user?.let {
                firestore
                    .collection("users")
                    .document(it.uid)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Log.e("ProfileViewModel", "Error fetching user data: ${error.message}")
                            return@addSnapshotListener
                        }
                        if (snapshot != null && snapshot.exists()) {
                            _userData.value = snapshot.toObject(User::class.java)
                        }
                    }
            }
        }

        fun logout(onLogoutSuccess: () -> Unit) {
            auth.signOut()
            onLogoutSuccess() // Call navigation after logout
        }
    }
