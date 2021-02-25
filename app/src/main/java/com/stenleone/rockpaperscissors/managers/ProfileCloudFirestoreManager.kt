package com.stenleone.rockpaperscissors.managers

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
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

    fun signInOrLogin(email: String, password: String, name: String, success: (FirebaseUser) -> Unit, failure: (Exception) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    auth.currentUser?.let { it -> success(it) }
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                auth.currentUser?.let { it -> success(it) }
                                setUserName(name)
                            } else {
                                failure(it.exception ?: Exception("не удалось создать пользователя"))
                            }
                        }
                        .addOnFailureListener {
                            failure(it)
                        }
                    failure(it.exception ?: Exception("не удалось ввойти"))
                }
            }
            .addOnFailureListener {
                failure(it)
            }
    }

    fun signInOrLogin(email: String, password: String, success: (FirebaseUser) -> Unit, failure: (Exception) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    auth.currentUser?.let { it -> success(it) }
                } else {
                    val exception = it.exception
                    if (exception is FirebaseAuthInvalidCredentialsException && exception.message?.contains("password") ?: false) {
                        failure(it.exception ?: Exception("не удалось ввойти"))
                    } else {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    auth.currentUser?.let { it -> success(it) }
                                } else {
                                    failure(it.exception ?: Exception("не удалось создать пользователя"))
                                }
                            }
                            .addOnFailureListener {
                                failure(it)
                            }
                    }
                }
            }
            .addOnFailureListener {
                if (!(it.message?.contains("There is no user record corresponding to this identifier. The user may have been deleted.") ?: false)) {
                    failure(it)
                }
            }
    }

    fun setUserName(name: String) {
        auth.currentUser?.let {
            store.document(USERS).set(name)
        }
    }
}