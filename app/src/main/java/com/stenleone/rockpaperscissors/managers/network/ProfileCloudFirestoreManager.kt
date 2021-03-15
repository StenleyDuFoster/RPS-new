package com.stenleone.rockpaperscissors.managers.network

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.stenleone.rockpaperscissors.managers.network.base.BaseNetworkManager
import com.stenleone.rockpaperscissors.model.general.DataState
import com.stenleone.rockpaperscissors.model.network.User
import com.stenleone.stanleysfilm.model.entity.RequestError
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class ProfileCloudFirestoreManager @Inject constructor(@ApplicationContext private val context: Context) : BaseNetworkManager() {

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

    suspend fun updateUser(user: User): DataState<FirebaseUser> {
        return suspendCoroutine { continuation ->
            CoroutineScope(Main + continuation.context).launch {
                withTimeout(timeOut, {
                    auth.currentUser?.let {
                        it?.email?.let { email ->
                            store.collection(USERS).document(email).set(user)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        auth.currentUser?.let {
                                            continuation.resume(DataState.Success(it))
                                        }
                                    }
                                }
                                .addOnFailureListener {
                                    continuation.resume(DataState.Error(RequestError(RequestError.REQUEST_ERROR, message = it.message)))
                                }
                        }
                    }
                })
            }
        }
    }
}