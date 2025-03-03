package com.example.flipmatch.data.repository

import com.example.flipmatch.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

        override fun getTopUsers(): Flow<List<User>> =
            flow {
                try {
                    val topUsersSnapshot =
                        firestore
                            .collection("users")
                            .orderBy("totalScore", Query.Direction.DESCENDING)
                            .limit(10)
                            .get()
                            .await()

                    val topUsers = topUsersSnapshot.toObjects(User::class.java)
                    val currentUser = auth.currentUser

                    if (currentUser != null) {
                        val currentUserSnapshot =
                            firestore
                                .collection("users")
                                .document(currentUser.uid)
                                .get()
                                .await()

                        val currentUserData = currentUserSnapshot.toObject(User::class.java)

                        if (currentUserData != null && topUsers.none { it.uid == currentUser.uid }) {
                            // If current user is not in top 10, add them at the end
                            val updatedList = topUsers.toMutableList().apply { add(currentUserData) }
                            emit(updatedList)
                        } else {
                            emit(topUsers)
                        }
                    } else {
                        emit(topUsers)
                    }
                } catch (e: Exception) {
                    emit(emptyList()) // Return empty list if an error occurs
                    e.printStackTrace()
                }
            }
    }
