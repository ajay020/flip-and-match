package com.example.flipmatch.data.repository

import com.example.flipmatch.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
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

        override fun updateUserScore(
            uid: String,
            puzzleType: String,
            newScore: Int,
        ): Flow<Boolean> =
            flow {
                try {
                    val userRef = firestore.collection("users").document(uid)

                    firestore
                        .runTransaction { transaction ->
                            val snapshot = transaction.get(userRef)
                            val user = snapshot.toObject(User::class.java) ?: User()

                            // Update total score by adding the new score
                            val updatedTotalScore = user.totalScore + newScore

                            // Update best score only if newScore is greater
                            val currentBestScore = user.bestScores[puzzleType] ?: 0
                            val updatedBestScores =
                                user.bestScores.toMutableMap().apply {
                                    if (newScore > currentBestScore) {
                                        this[puzzleType] = newScore
                                    }
                                }

                            // Save updated values
                            transaction.set(
                                userRef,
                                user.copy(totalScore = updatedTotalScore, bestScores = updatedBestScores),
                            )
                        }.await() // Suspend function to ensure completion

                    emit(true) // Emit success
                } catch (e: Exception) {
                    emit(false) // Emit failure
                }
            }
    }
