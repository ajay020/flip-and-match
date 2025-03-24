package com.example.flipmatch.data.repository

import com.example.flipmatch.MainDispatcherRule
import com.example.flipmatch.data.model.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Transaction
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.unmockkObject
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class UserRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mocks
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userRepository: UserRepositoryImpl
    private lateinit var mockDocumentRef: DocumentReference
    private lateinit var mockCollectionRef: CollectionReference
    private lateinit var mockSnapshot: DocumentSnapshot
    private lateinit var mockTransaction: Transaction

    @Before
    fun setUp() {
        // Initialize mocks
        firestore = mockk()
        auth = mockk()
        mockDocumentRef = mockk()
        mockCollectionRef = mockk()
        mockSnapshot = mockk()
        mockTransaction = mockk(relaxed = true)

        userRepository = UserRepositoryImpl(firestore, auth)

        every { firestore.collection("users") } returns mockCollectionRef
        every { mockCollectionRef.document(any()) } returns mockDocumentRef
    }

    @After
    fun tearDown() {
        unmockkObject(userRepository)
    }

    @Test
    fun `getUserData should return User object when successful`() =
        runTest {
            val auth: FirebaseAuth = mockk()
            val firestore: FirebaseFirestore = mockk()
            val user: FirebaseUser = mockk()
            val documentSnapshot: DocumentSnapshot = mockk()
            val expectedUser = User(uid = "test_uid", totalScore = 100, bestScores = mapOf("puzzle1" to 50))

            every { auth.currentUser } returns user
            every { user.uid } returns "test_uid"
            every { firestore.collection("users").document("test_uid").get() } returns Tasks.forResult(documentSnapshot)
            every { documentSnapshot.toObject(User::class.java) } returns expectedUser

            val userRepository = UserRepositoryImpl(firestore, auth)
            val result = userRepository.getUserData()

            assertEquals(expectedUser, result)
        }

    @Test
    fun `getUserData should return null when no current user`() =
        runTest {
            val auth: FirebaseAuth = mockk()
            val firestore: FirebaseFirestore = mockk()

            every { auth.currentUser } returns null

            val userRepository = UserRepositoryImpl(firestore, auth)
            val result = userRepository.getUserData()

            assertNull(result)
        }

    @Test
    fun `getUserData should return null when FirebaseFirestoreException occurs`() =
        runTest {
            val auth: FirebaseAuth = mockk()
            val firestore: FirebaseFirestore = mockk()
            val user: FirebaseUser = mockk()

            every { auth.currentUser } returns user
            every { user.uid } returns "test_uid"
            every { firestore.collection("users").document("test_uid").get() } returns
                Tasks.forException(
                    FirebaseFirestoreException("Firestore error", FirebaseFirestoreException.Code.ABORTED),
                )

            val userRepository = UserRepositoryImpl(firestore, auth)
            val result = userRepository.getUserData()

            assertNull(result)
        }

    @Test
    fun `getUserData should return null when IOException occurs`() =
        runTest {
            val auth: FirebaseAuth = mockk()
            val firestore: FirebaseFirestore = mockk()
            val user: FirebaseUser = mockk()

            every { auth.currentUser } returns user
            every { user.uid } returns "test_uid"
            every { firestore.collection("users").document("test_uid").get() } returns
                Tasks.forException(
                    IOException("Network error"),
                )

            val userRepository = UserRepositoryImpl(firestore, auth)
            val result = userRepository.getUserData()

            assertNull(result)
        }

    @Test
    fun `getUserData should return null when no user is logged in`() =
        runTest {
            every { auth.currentUser } returns null

            val result = userRepository.getUserData()

            assertNull(result)
        }

    @Test
    fun `logout should call auth signOut`() {
        every { auth.signOut() } just runs

        userRepository.logout()

        verify { auth.signOut() }
    }

    @Test
    fun `getTopUsersWithCurrent should return list of top users including current user`() =
        runTest {
            val topUsers = listOf(User("1", "Alice"), User("2", "Bob"))
            val currentUser = User("3", "Charlie")

            mockkObject(userRepository)
            coEvery { userRepository.getTopUsers() } returns topUsers
            coEvery { userRepository.getCurrentUser() } returns currentUser

            val result = userRepository.getTopUsersWithCurrent()

            assertEquals(3, result.size)
            assertTrue(result.contains(currentUser))
        }

    @Test
    fun `getTopUsersWithCurrent should return only top users when current user is null`() =
        runTest {
            val topUsers = listOf(User("1", "Alice"), User("2", "Bob"))

            mockkObject(userRepository)
            coEvery { userRepository.getTopUsers() } returns topUsers
            coEvery { userRepository.getCurrentUser() } returns null

            val result = userRepository.getTopUsersWithCurrent()

            assertEquals(2, result.size)
            assertFalse(result.any { it.uid == "3" })
        }

    @Test
    fun `updateUserScore should return true when transaction succeeds`() =
        runTest {
            val uid = "test_uid"
            val puzzleType = "puzzle1"
            val newScore = 50
            val mockUser = User(uid, "John Doe", totalScore = 100, bestScores = mapOf(puzzleType to 40))

            // Mock Firestore transaction
            every { mockTransaction.get(mockDocumentRef) } returns mockSnapshot
            every { mockSnapshot.toObject(User::class.java) } returns mockUser
            every { firestore.runTransaction(any<Transaction.Function<Unit>>()) } returns Tasks.forResult(Unit)

            val result = userRepository.updateUserScore(uid, puzzleType, newScore)

            assertTrue(result) // Ensure success
        }

//    @Test
//    fun `updateUserScore should return false when FirestoreException occurs`() =
//        runTest {
//            println("🔥 Test started") // Check if test execution reaches here
//
//            val uid = "test_uid"
//            val puzzleType = "puzzle1"
//            val newScore = 50
//
//            coEvery { firestore.runTransaction<Unit>(any()) } answers {
//                throw FirebaseFirestoreException(
//                    "Test Exception",
//                    FirebaseFirestoreException.Code.ABORTED,
//                )
//            }
//
//            val result = userRepository.updateUserScore(uid, puzzleType, newScore)
//            assertFalse(result)
//
//            println("🔥 Method execution completed")
//        }
//
//    @Test
//    fun `updateUserScore should return false when IOException occurs`() =
//        runTest {
//            val uid = "test_uid"
//            val puzzleType = "puzzle1"
//            val newScore = 50
//
//            every { firestore.runTransaction(any<Transaction.Function<Unit>>()) } returns
//                Tasks.forException(
//                    FirebaseFirestoreException(
//                        "Firestore error",
//                        FirebaseFirestoreException.Code.ABORTED,
//                    ),
//                )
//
//            val result = userRepository.updateUserScore(uid, puzzleType, newScore)
//
//            assertFalse(result) // Ensure failure is handled
//        }
}
