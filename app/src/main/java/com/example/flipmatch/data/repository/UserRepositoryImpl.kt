package com.example.flipmatch.data.repository

import com.example.flipmatch.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val firestore: FirebaseFirestore,
        private val auth: FirebaseAuth,
    ) : UserRepository {
        override fun getUserData(): Flow<User?> =
            callbackFlow {
                val user = auth.currentUser
                if (user == null) {
                    trySend(null)
                    close()
                    return@callbackFlow
                }

                val listener =
                    firestore
                        .collection("users")
                        .document(user.uid)
                        .addSnapshotListener { snapshot, error ->
                            if (error != null) {
                                close(error)
                                return@addSnapshotListener
                            }
                            if (snapshot != null && snapshot.exists()) {
                                trySend(snapshot.toObject(User::class.java))
                            }
                        }

                awaitClose { listener.remove() }
            }

        override fun logout() {
            auth.signOut()
        }
    }
