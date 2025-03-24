package com.example.flipmatch.data.repository

import android.util.Log
import com.example.flipmatch.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val firestore: FirebaseFirestore,
        private val auth: FirebaseAuth,
    ) : UserRepository {
        override suspend fun getUserData(): User? {
            val user = auth.currentUser ?: return null

            return try {
                firestore
                    .collection("users")
                    .document(user.uid)
                    .get()
                    .await()
                    .toObject(User::class.java)
            } catch (e: FirebaseFirestoreException) {
//                Log.e("UserRepository", "Firestore error: ${e.message}", e)
                null
            } catch (e: IOException) {
//                Log.e("UserRepository", "Network error: ${e.message}", e)
                null
            }
        }

        override fun logout() {
            auth.signOut()
        }

        override suspend fun updateUserScore(
            uid: String,
            puzzleType: String,
            newScore: Int,
        ): Boolean =
            try {
                val userRef = firestore.collection("users").document(uid)

                firestore
                    .runTransaction { transaction ->
                        val user = transaction.get(userRef).toObject(User::class.java) ?: User()

                        transaction.set(
                            userRef,
                            user.copy(
                                totalScore = user.totalScore + newScore,
                                bestScores =
                                    user.bestScores.toMutableMap().apply {
                                        this[puzzleType] = maxOf(newScore, this[puzzleType] ?: 0)
                                    },
                            ),
                        )
                    }.await()

                true // Return success
            } catch (e: FirebaseFirestoreException) {
                Log.e("UserRepository", "Firestore error while updating score: ${e.message}", e)
                false // Return failure
            } catch (e: IOException) {
                Log.e("UserRepository", "Network error while updating score: ${e.message}", e)
                false // Return failure
            }

        override suspend fun getTopUsersWithCurrent(): List<User> {
            val topUsers = getTopUsers()
            val currentUser = getCurrentUser()

            return if (currentUser != null && topUsers.none { it.uid == currentUser.uid }) {
                topUsers + currentUser // Append current user if not in the list
            } else {
                topUsers
            }
        }

        suspend fun getTopUsers(): List<User> =
            try {
                firestore
                    .collection("users")
                    .orderBy("totalScore", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .await()
                    .toObjects(User::class.java)
            } catch (e: FirebaseFirestoreException) {
                Log.e("UserRepository", "Firestore error: ${e.message}", e)
                emptyList()
            } catch (e: IOException) {
                Log.e("UserRepository", "Network error: ${e.message}", e)
                emptyList()
            }

        suspend fun getCurrentUser(): User? {
            val user = auth.currentUser ?: return null
            return try {
                firestore
                    .collection("users")
                    .document(user.uid)
                    .get()
                    .await()
                    .toObject(User::class.java)
            } catch (e: FirebaseFirestoreException) {
                Log.e("UserRepository", "Firestore error: ${e.message}", e)
                null
            } catch (e: IOException) {
                Log.e("UserRepository", "Network error: ${e.message}", e)
                null
            }
        }
    }
