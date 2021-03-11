package com.stenleone.rockpaperscissors.managers.network

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.stenleone.rockpaperscissors.model.network.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileCloudFirestoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val USERS = "users"
    }

    private val store by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    fun signInOrLogin(email: String, password: String, success: (FirebaseUser) -> Unit, failureEmail: (Exception) -> Unit, failurePassword: (Exception) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    auth.currentUser?.let { it -> success(it) }
                } else {
                    val exception = it.exception
                    if (exception is FirebaseAuthInvalidCredentialsException && exception.message?.contains("password") ?: false) {
                        failurePassword(it.exception ?: Exception("не удалось ввойти"))
                    } else {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    createUserInStore(email, password, success, failureEmail)
                                }
                            }
                            .addOnFailureListener {
                                failureEmail(it)
                            }
                    }
                }
            }
            .addOnFailureListener {
                if (!(it.message?.contains("There is no user record corresponding to this identifier. The user may have been deleted.") ?: false)) {
                    failureEmail(it)
                }
            }
    }

    fun createUserInStore(email: String, password: String, success: (FirebaseUser) -> Unit, failureEmail: (Exception) -> Unit) {
        store.collection(USERS).document(email).set(
            User(
                "",
                email,
                password,
                0,
                0,
                ""
            )
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    auth.currentUser?.let { it -> success(it) }
                }
            }
            .addOnFailureListener {
                failureEmail(it)
            }
    }

    fun getProfile(success: (User) -> Unit, error: (String?) -> Unit) {
        auth.currentUser?.let {
            it.email?.let { email ->
                store.collection(USERS).document(email).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = try {
                            task.result.toObject(User::class.java)
                        } catch (e: Exception) {
                            null
                        }
                        if (user != null) {
                            success(user)
                        } else {
                            FirebaseCrashlytics.getInstance().recordException(Throwable("USER NOT FOUND OR NOT PARSE"))
                        }
                    } else {
                        error(task.exception?.message)
                    }
                }
            }
        }
    }

    fun updateUser(user: User, success: (FirebaseUser) -> Unit, failure: (Exception) -> Unit) {
        auth.currentUser?.let {
            it?.email?.let { email ->
                store.collection(USERS).document(email).set(user)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            auth.currentUser?.let { it -> success(it) }
                        }
                    }
                    .addOnFailureListener {
                        failure(it)
                    }
            }
        }
    }
}